package me.wjy.bill.service;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.po.UserDO;
import me.wjy.bill.pojo.dto.UserDTO;

/**
 * @author 王金义
 */
public interface UserService {
    UserDO getUser(UserDTO userDTO) throws ServiceException;
}
