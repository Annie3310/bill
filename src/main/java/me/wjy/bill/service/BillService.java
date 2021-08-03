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
     * @param billDTO
     * @return
     * @throws ServiceException
     */
    PublicResponse income(BillDTO billDTO) throws ServiceException;
    /**
     * 收入
     * @param billDTO 账单条目相关信息
     * @return 执行信息
     * @throws ServiceException
     */
    PublicResponse expense(BillDTO billDTO) throws ServiceException;

    /**
     * 转账
     * @param transferDTO 转账两账户信息
     * @return 执行信息
     * @throws ServiceException
     */
    PublicResponse transfer(TransferDTO transferDTO) throws ServiceException;

    /**
     * 获取资产总合
     * @param userId
     * @return 执行信息
     * @throws ServiceException
     */
    PublicResponse getSum(String userId) throws ServiceException;
}
