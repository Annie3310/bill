package me.wjy.bill.service.impl;

import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.enums.UUIDConfig;
import me.wjy.bill.exception.ServiceException;
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
        if (userDTO.getUserId() == null || userDTO.getPassword() == null) {
            logger.warn("getUser 未提供用户名或密码");
            throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "用户名或密码未输入", null);
        }
        logger.info("getUser 获取用户");
        UserDO user = userMapper.getUser(userDTO.getUserId());
        if (user == null) {
            logger.warn("getUser 未获取到用户");
            throw new ServiceException(
                    ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode()
                    , "用户不存在"
                    , null);
        }
        String userDTOPassword = encryptedPassword(userDTO.getPassword(), user.getSalt());
        String userPassword = user.getPassword();
        if (!Objects.equals(userPassword, userDTOPassword)) {
            logger.warn("getUser 用户名或密码错误");
            throw new ServiceException(
                    ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode()
                    , "认证失败, 用户名或密码不正确"
                    , null);
        }
        return user;
    }

    @Override
    public PublicResponse updateName(UserDTO userDTO) throws ServiceException {
        userDTO.setPassword(null);
        Integer update = userMapper.update(userDTO);
        if (update < 1) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "修改未成功", null);
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("修改成功")
                .build();
    }

    @Override
    public PublicResponse updatePassword(UserDTO userDTO) throws ServiceException {
        userDTO.setName(null);
        userDTO.setPassword(SHA256.getSHA256(userDTO.getPassword()));
        Integer update = userMapper.update(userDTO);
        if (update < 1) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "修改未成功", null);
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("修改成功")
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse register(UserDTO userDTO) throws ServiceException {
        if (userDTO.getName() == null || userDTO.getPassword() == null) {
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "name 或 password 为空", null);
        }
        userDTO.setUserId(UUIDUtil.getUUID(UUIDConfig.UUID_LEN));
        HashMap<String, String> saltAndPassword = getSaltAndPassword(userDTO.getPassword());
        userDTO.setSalt(Long.valueOf(saltAndPassword.get("salt")));
        userDTO.setPassword(saltAndPassword.get("password"));
        Integer i = userMapper.save(userDTO);
        if (i == null || i < 1) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未成功注册", null);
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
        logger.info("一次密码: {}", sha256Password1);
        objectObjectHashMap.put("password", SHA256.getSHA256(sha256Password1));
        return objectObjectHashMap;
    }

    private String encryptedPassword(String password, Long salt) {
        String passwordPlusSalt = password + salt;
        String sha256Password1 = SHA256.getSHA256(passwordPlusSalt);
        logger.info("一次密码: {}", sha256Password1);
        return SHA256.getSHA256(sha256Password1);
    }
}
