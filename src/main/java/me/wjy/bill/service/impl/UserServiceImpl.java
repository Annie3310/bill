package me.wjy.bill.service.impl;

import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.enums.UUIDConfig;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.mapper.AccountMapper;
import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.dto.AccountUpdateDTO;
import me.wjy.bill.pojo.po.AccountDO;
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
import java.util.List;
import java.util.Objects;

/**
 * TODO 用户插入/删除/更改账户时, 应同时增加对应的账单
 * @author 王金义
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final AccountMapper accountMapper;

    public UserServiceImpl(UserMapper userMapper, AccountMapper accountMapper) {
        this.userMapper = userMapper;
        this.accountMapper = accountMapper;
    }

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDO getUser(UserDTO userDTO) throws ServiceException {
        if (userDTO.getUserId() == null || userDTO.getPassword() == null) {
            logger.warn("getUser 未找到用户名或密码");
            throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "用户名或密码未输入", null);
        }
        logger.info("getUser 获取用户");
        String userDTOPassword = userDTO.getPassword();
        userDTO.setPassword(SHA256.getSHA256(userDTOPassword));
        UserDO user = userMapper.getUser(userDTO.getUserId());
        if (user == null) {
            logger.warn("getUser 未获取到用户");
            throw new ServiceException(
                    ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode()
                    , "用户不存在"
                    , null);
        }
        userDTOPassword = userDTO.getPassword();
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
    public PublicResponse addAccount(AccountDTO accountDTO) throws ServiceException {
        AccountDO name = accountMapper.getByName(accountDTO);
        if (name != null) {
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "该账户已经存在", null);
        }
        Integer integer = accountMapper.addAccount(accountDTO);
        if (integer == null || integer < 1) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "插入未成功", null);
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("添加成功")
                .build();
    }

    @Override
    public PublicResponse rmAccount(AccountDTO accountDTO) throws ServiceException {
        Integer integer = accountMapper.rmAccount(accountDTO);
        if (integer == null || integer < 1) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "删除未成功, 可能是该账户不存在", null);
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("删除成功")
                .build();
    }

    @Override
    public PublicResponse updateAccount(AccountUpdateDTO accountUpdateDTO) throws ServiceException {
        AccountDTO accountDTO = new AccountDTO();
        if (accountUpdateDTO.getBalance() != null) {
            accountDTO.setBalance(accountUpdateDTO.getBalance());
        }
        if (accountUpdateDTO.getOldName() != null) {
            accountDTO.setName(accountUpdateDTO.getOldName());
        }
        accountDTO.setUserId(accountUpdateDTO.getUserId());
        AccountDO name = accountMapper.getByName(accountDTO);
        if (name == null) {
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "未找到该账户", null);
        }
        Integer integer = accountMapper.updateAccount(accountUpdateDTO);
        if (integer == null || integer < 1) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "更新未成功", null);
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("更新成功")
                .build();
    }

    @Override
    public PublicResponse getAllAccount(String userId) throws ServiceException {
        List<AccountDO> allAccount = accountMapper.getAllAccount(userId);
        if (allAccount.isEmpty()) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未获取成功", null);
        }
        HashMap<String, Double> mapOfVO = new HashMap<>(allAccount.size());
        for (AccountDO accountDO : allAccount) {
            mapOfVO.put(accountDO.getName(),accountDO.getBalance());
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("获取成功")
                .result(mapOfVO)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse register(UserDTO userDTO) throws ServiceException {
        if (userDTO.getName() == null || userDTO.getPassword() == null) {
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "name 或 password 为空", null);
        }
        userDTO.setUserId(UUIDUtil.getUUID(UUIDConfig.UUID_LEN));
        userDTO.setPassword(SHA256.getSHA256(userDTO.getPassword()));
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
}
