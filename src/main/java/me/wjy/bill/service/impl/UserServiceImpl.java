package me.wjy.bill.service.impl;

import me.wjy.bill.enums.ErrorCodeEnum;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.utils.SHA256;
import me.wjy.bill.mapper.UserMapper;
import me.wjy.bill.pojo.po.UserDO;
import me.wjy.bill.pojo.dto.UserDTO;
import me.wjy.bill.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author 王金义
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public UserDO getUser(UserDTO userDTO) throws ServiceException {
        // TODO 只用 userId 查用户, 再在 Service 层验证密码
        logger.info("getUser 获取用户");
        userDTO.setPassword(SHA256.SHA256(userDTO.getPassword()));
        UserDO user = userMapper.getUser(userDTO);
        if (user == null) {
            logger.warn("getUser 未获取到用户");
            throw new ServiceException(ErrorCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode()
                    , "认证失败, 用户名或密码不正确, 或用户不存在",
                    null);
        }
        return user;
    }
}
