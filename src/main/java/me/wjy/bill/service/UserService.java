package me.wjy.bill.service;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.dto.AccountUpdateDTO;
import me.wjy.bill.pojo.dto.UpdatePasswordDTO;
import me.wjy.bill.pojo.po.UserDO;
import me.wjy.bill.pojo.dto.UserDTO;
import me.wjy.bill.response.PublicResponse;

/**
 * @author 王金义
 */
public interface UserService {
    /**
     * 获取请求头中传入参数中的 user 实体
     *
     * @param userDTO require: userId, password
     * @return 找到的 user
     * @throws ServiceException 通用异常类
     */
    UserDO getUser(UserDTO userDTO) throws ServiceException;

    /**
     * 注册
     * @param userDTO require: name, password
     * @return 通用返回体
     * @throws ServiceException 通用异常类
     */
    PublicResponse register(UserDTO userDTO) throws ServiceException;

    /**
     * 更改密码
     * @param updatePasswordDTO 新的信息
     * @return 通用返回体
     * @throws ServiceException 通用异常类
     */
    PublicResponse updatePassword(UpdatePasswordDTO updatePasswordDTO) throws ServiceException;

    /**
     * 更改姓名
     * @param userDTO 新的信息
     * @return 通用返回体
     * @throws ServiceException 通用异常类
     */
    PublicResponse updateName(UserDTO userDTO) throws ServiceException;
}
