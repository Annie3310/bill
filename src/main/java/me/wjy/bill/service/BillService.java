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
     * 支出
     * @param billDTO 账单条目相关信息
     * @return 执行信息
     */
    PublicResponse income(BillDTO billDTO) throws ServiceException;
    /**
     * 收入
     * @param billDTO 账单条目相关信息
     * @return 执行信息
     */
    PublicResponse expense(BillDTO billDTO) throws ServiceException;

    /**
     * 转账
     * @param transferDTO 转账两账户信息
     * @return 执行信息
     */
    PublicResponse transfer(TransferDTO transferDTO) throws ServiceException;

    /**
     * 获取资产总合
     * @return 执行信息
     */
    PublicResponse getSum(String userId) throws ServiceException;
}
