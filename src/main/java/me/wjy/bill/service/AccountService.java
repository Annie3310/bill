package me.wjy.bill.service;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.dto.AccountUpdateDTO;
import me.wjy.bill.response.PublicResponse;
import org.springframework.stereotype.Service;

/**
 * @author 王金义
 * @date 2021/8/11
 */
public interface AccountService {
    /**
     * 添加账户
     * @param accountDTO require: name
     * @return 是否成功
     * @throws ServiceException 通用异常
     */
    PublicResponse addAccount(AccountDTO accountDTO) throws ServiceException;

    /**
     * 删除一个账户
     * @param accountDTO require: name
     * @return 是否成功
     * @throws ServiceException 通用异常
     */
    public PublicResponse rmAccount(AccountDTO accountDTO) throws ServiceException;

    /**
     * 更新账户, 可改名或改余额
     * @param accountUpdateDTO require: oldName
     * @return 是否成功
     * @throws ServiceException 通用返回体
     */
    public PublicResponse updateAccount(AccountUpdateDTO accountUpdateDTO) throws ServiceException;

    /**
     * 获取所有账户
     * @param userId 用户 ID
     * @return 用户当前账号下的所有账户及余额
     * @throws ServiceException 通用异常
     */
    public PublicResponse getAllAccount(String userId) throws ServiceException;
}
