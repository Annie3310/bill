package me.wjy.bill.service;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.dto.AccountUpdateDTO;
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
     * @param userDTO
     * @return
     * @throws ServiceException
     */
    UserDO getUser(UserDTO userDTO) throws ServiceException;

    /**
     * 注册
     *
     * @param userDTO
     * @return
     * @throws ServiceException
     */
    PublicResponse register(UserDTO userDTO) throws ServiceException;

    PublicResponse addAccount(AccountDTO accountDTO) throws ServiceException;

    PublicResponse rmAccount(AccountDTO accountDTO) throws ServiceException;

    PublicResponse updateAccount(AccountUpdateDTO accountUpdateDTO) throws ServiceException;

    PublicResponse getAllAccount(String userId) throws ServiceException;
}
