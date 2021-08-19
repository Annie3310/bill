package me.wjy.bill.service.impl;

import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.enums.UUIDConfig;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.UpdatePasswordDTO;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.utils.SHA256;
import me.wjy.bill.mapper.UserMapper;
import me.wjy.bill.pojo.po.UserDO;
import me.wjy.bill.pojo.dto.UserDTO;
import me.wjy.bill.service.UserService;
import me.wjy.bill.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.CRC32;

/**
 * @author 王金义
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDO getUser(UserDTO userDTO) throws ServiceException {
        String className = getClass().getName();
        if (userDTO.getUserId() == null || userDTO.getPassword() == null) {
            logger.warn("{}.getUser 未提供用户名或密码", className);
            throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "用户名或密码未输入");
        }
        logger.info("getUser 获取用户");
        UserDO user = userMapper.getUser(userDTO.getUserId());
        if (user == null) {
            logger.warn("{}.getUser 未获取到用户", className);
            throw new ServiceException(
                    ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode()
                    , "用户不存在");
        }
        String userDTOPassword = encryptedPassword(userDTO.getPassword(), user.getSalt());
        String userPassword = user.getPassword();
        if (!Objects.equals(userPassword, userDTOPassword)) {
            logger.warn("{}.getUser 用户名或密码错误",className);
            throw new ServiceException(
                    ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode()
                    , "认证失败, 用户名或密码不正确");
        }
        return user;
    }

    @Override
    public PublicResponse updateName(UserDTO userDTO) throws ServiceException {
        String className = getClass().getName();
        if (userDTO.getName() == null) {
            logger.warn("{}.updateName: 用户未输入要修改的用户名", className);
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "未输入要修改的用户名");
        }
        if (verifyPassword(userDTO)) {
            logger.warn("{}.updatePassword: 用户提供的密码不正确, 修改用户名失败", className);
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "认证失败, 输入的密码不正确");
        }
        userDTO.setPassword(null);
        Integer update = userMapper.update(userDTO);
        if (update < 1) {
            logger.warn("{}.updateName: 用户名未修改成功, 数据库执行结果不为 1", className);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "修改未成功");
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("用户名修改成功")
                .build();
    }

    @Override
    public PublicResponse updatePassword(UpdatePasswordDTO updatePasswordDTO) throws ServiceException {
        String className = getClass().getName();
        if (updatePasswordDTO.getNewPassword() == null) {
            logger.warn("{}.updatePassword: 用户未输入要修改的密码", className);
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "未输入要修改的密码");
        }
        UserDTO userDTO = UserDTO
                .builder()
                .password(updatePasswordDTO.getOldPassword())
                .build();
        userDTO.setUserId(updatePasswordDTO.getUserId());
        if (verifyPassword(userDTO)) {
            logger.warn("{}.updatePassword: 用户提供的密码不正确, 修改密码失败", className);
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "认证失败, 输入的密码不正确");
        }
        HashMap<String, String> saltAndPassword = getSaltAndPassword(updatePasswordDTO.getNewPassword());
        userDTO.setPassword(saltAndPassword.get("password"));
        userDTO.setSalt(Long.valueOf(saltAndPassword.get("salt")));
        Integer update = userMapper.update(userDTO);
        logger.info("{}.updatePassword: 更新密码: {}",className, update);
        if (update < 1) {
            logger.warn("{}.updatePassword: 密码未修改成功, 数据库执行结果不为 1", className);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "修改未成功");
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("密码修改成功")
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse register(UserDTO userDTO) throws ServiceException {
        String className = getClass().getName();
        if (userDTO.getName() == null) {
            logger.warn("{}.register: 用户未输入 name, 信息不完整", className);
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "用户输入的 name 为空");
        }
        userDTO.setUserId(UUIDUtil.getUUID(UUIDConfig.UUID_LEN));
        HashMap<String, String> saltAndPassword = getSaltAndPassword(userDTO.getPassword());
        userDTO.setSalt(Long.valueOf(saltAndPassword.get("salt")));
        userDTO.setPassword(saltAndPassword.get("password"));
        Integer i = userMapper.save(userDTO);
        logger.info("{}.register: 新增用户: {}",className, i);
        if (i == null || i < 1) {
            logger.warn("{}.register: 未注册成功, 数据库执行结果不为 1", className);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未成功注册");
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("注册成功")
                .result("userId: " + userDTO.getUserId() + ", " + "name: " + userDTO.getName())
                .build();
    }

    private HashMap<String, String> getSaltAndPassword(String password) {
        HashMap<String, String> objectObjectHashMap = new HashMap<>(2);
        CRC32 crc32 = new CRC32();
        crc32.update(UUID.randomUUID().toString().getBytes());
        Long salt = crc32.getValue();
        objectObjectHashMap.put("salt", String.valueOf(salt));
        // SHA256(SHA256(密码 + 盐))
        String passwordPlusSalt = password + salt;
        String sha256Password1 = SHA256.getSHA256(passwordPlusSalt);
        objectObjectHashMap.put("password", SHA256.getSHA256(sha256Password1));
        return objectObjectHashMap;
    }

    private String encryptedPassword(String password, Long salt) {
        return SHA256.getSHA256(SHA256.getSHA256(password + salt));
    }

    private boolean verifyPassword(UserDTO userDTO) {
        UserDO user = userMapper.getUser(userDTO.getUserId());
        String userDTOPassword = encryptedPassword(userDTO.getPassword(), user.getSalt());
        // 密码正确返回 false, 因为调用这个方法的逻辑是密码错误时抛出异常, 如果密码正确返回 true, 则调用方会一直需要一个 ! 符号
        return !Objects.equals(userDTOPassword, user.getPassword());
    }
}
