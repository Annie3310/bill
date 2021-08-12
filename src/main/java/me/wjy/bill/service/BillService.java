package me.wjy.bill.service;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.BillDTO;
import me.wjy.bill.pojo.dto.TransferDTO;
import me.wjy.bill.response.PublicResponse;

/**
 * @author 王金义
 */
public interface BillService {
    /**
     * 收入
     * @param billDTO require: account, money
     * @return 如果没有异常则返回响应信息
     * @throws ServiceException 通用异常类
     */
    PublicResponse income(BillDTO billDTO) throws ServiceException;
    /**
     * 收入
     * @param billDTO 账单条目相关信息
     * @return 执行信息
     * @throws ServiceException 通用异常类
     */
    PublicResponse expense(BillDTO billDTO) throws ServiceException;

    /**
     * 转账
     * @param transferDTO 转账两账户信息
     * @return 执行信息
     * @throws ServiceException 通用异常类
     */
    PublicResponse transfer(TransferDTO transferDTO) throws ServiceException;

    /**
     * 获取资产总合
     * @param userId 用户 ID
     * @return 执行信息
     * @throws ServiceException 通用异常类
     */
    PublicResponse getSum(String userId) throws ServiceException;

    /**
     * 回滚上一条记录
     * @param billDTO 用户 ID
     * @return 是否回滚成功
     */
    PublicResponse rollback(BillDTO billDTO);
}
