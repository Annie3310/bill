package me.wjy.bill.service.impl;

import me.wjy.bill.enums.BillResponseEnum;
import me.wjy.bill.enums.BillTypeEnum;
import me.wjy.bill.enums.ErrorCodeEnum;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.mapper.AccountMapper;
import me.wjy.bill.mapper.BillMapper;
import me.wjy.bill.pojo.dto.BillDTO;
import me.wjy.bill.pojo.dto.SelectBillDTO;
import me.wjy.bill.pojo.dto.TransferDTO;
import me.wjy.bill.pojo.po.BillDO;
import me.wjy.bill.pojo.vo.AccountVO;
import me.wjy.bill.pojo.vo.BillVO;
import me.wjy.bill.response.GetSumResponse;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 王金义
 */
@Service
public class BillServiceImpl implements BillService {
    private final AccountMapper accountMapper;
    private final BillMapper billMapper;
    private final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    public BillServiceImpl(AccountMapper accountMapper, BillMapper billMapper) {
        this.accountMapper = accountMapper;
        this.billMapper = billMapper;
    }

    @Override
    public PublicResponse getSum(String userId) throws ServiceException {
        List<AccountVO> sumDetails;
        logger.info("getSum 获取账户余额详情:");
        sumDetails = accountMapper.getSumDetails(userId);
        if (sumDetails.isEmpty()) {
            logger.warn("getSum 获取账户余额失败: {}", sumDetails);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未获取到账户详细信息", null);
        }
        logger.info("getSum 获取账户总和");
        Double sum = accountMapper.getSum(userId);
        if (sum <= 0) {
            logger.warn("getSum 获取余额总和失败: {}", sum);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未获取到总和", null);
        }
        Map<String, Double> detailsMap = new HashMap<>(16);
        for (AccountVO sumDetail : sumDetails) {
            detailsMap.put(sumDetail.getName(), sumDetail.getBalance());
        }
        GetSumResponse sumResponse = GetSumResponse
                .builder()
                .sum(sum)
                .details(detailsMap)
                .build();
        return PublicResponse
                .builder()
                .code(BillResponseEnum.GET_SUM_SUCCESS.getResponseCode())
                .message(BillResponseEnum.GET_SUM_SUCCESS.getMessage())
                .result(sumResponse)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse income(BillDTO billDTO) throws ServiceException {
        boolean accountNotExist = isAccountNotExist(billDTO.getUserId(), billDTO.getAccount());
        if (accountNotExist) {
            throw new ServiceException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "用户 '" + billDTO.getUserId() + "' 没有该账户", null);
        }
        billDTO.setUuid(String.valueOf(UUID.randomUUID()));
        billDTO.setType(BillTypeEnum.INCOME.getType());
        logger.info("income 开始生成账单");
        Integer effectiveRowsOfBill = billMapper.insert(billDTO);
        Integer effectiveRowsOfAccount;
        if (effectiveRowsOfBill > 0) {
            logger.info("income 开始向账户添加金额");
            effectiveRowsOfAccount = accountMapper.plusTo(billDTO);
        } else {
            logger.warn("income 生成账单失败: {}", effectiveRowsOfBill);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "账单添加失败", null);
        }
        if (effectiveRowsOfAccount <= 0) {
            logger.warn("income 向账户转账失败: {}", effectiveRowsOfAccount);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "交易执行失败", null);
        }
        StringBuilder stringBuilder = new StringBuilder(BillResponseEnum.INCOME_SUCCESS.getMessage());
        return PublicResponse.builder()
                .code(BillResponseEnum.INCOME_SUCCESS.getResponseCode())
                .message(stringBuilder
                        .append("从")
                        .append(billDTO.getAccount())
                        .append("收入了")
                        .append(billDTO.getMoney())
                        .append("元")
                        .toString())
                .result(effectiveRowsOfBill).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse expense(BillDTO billDTO) throws ServiceException {
        boolean accountNotExist = isAccountNotExist(billDTO.getUserId(), billDTO.getAccount());
        if (accountNotExist) {
            throw new ServiceException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "用户 '" + billDTO.getUserId() + "' 没有该账户", null);
        }
        billDTO.setUuid(String.valueOf(UUID.randomUUID()));
        logger.info("expense 开始生成账单");
        Integer effectiveRowsOfBill = billMapper.insert(billDTO);
        Integer effectiveRowsOfAccount;
        if (effectiveRowsOfBill > 0) {
            logger.info("expense 从账户转出");
            effectiveRowsOfAccount = accountMapper.minusFrom(billDTO);
        } else {
            logger.warn("expense 生成账单失败: {}", effectiveRowsOfBill);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "账单添加失败", null);
        }
        if (effectiveRowsOfAccount <= 0) {
            logger.warn("expense 从账户转出失败: {}", effectiveRowsOfAccount);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "交易执行失败", null);
        }
        StringBuilder stringBuilder = new StringBuilder(BillResponseEnum.EXPENSE_SUCCESS.getMessage());
        return PublicResponse.builder()
                .code(BillResponseEnum.EXPENSE_SUCCESS.getResponseCode())
                .message(stringBuilder
                        .append("从")
                        .append(billDTO.getAccount())
                        .append("支出了")
                        .append(billDTO.getMoney())
                        .append("元")
                        .toString())
                .result(effectiveRowsOfBill)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse transfer(TransferDTO transferDTO) throws ServiceException {
        logger.info("transfer 开始从输入中获取 BillDTO");
        HashMap<String, BillDTO> map = transferDTOToBillDTO(transferDTO);
        BillDTO from = map.get("from");
        BillDTO to = map.get("to");
        // 账单插入是否生效
        logger.info("transfer 开始生成转账 From 账单");
        Integer effectiveRowsOfFrom = billMapper.insert(from);
        logger.info("transfer 开始生成转账 To 账单");
        Integer effectiveRowsOfTo = billMapper.insert(to);
        Boolean isInsertFail = effectiveRowsOfFrom < 1 || effectiveRowsOfTo < 1;
        if (isInsertFail) {
            logger.warn("transfer 生成账单失败: 'From' = {}, 'To' = {}", effectiveRowsOfFrom, effectiveRowsOfTo);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "添加账单失败", null);
        }
        logger.info("transfer 开始进行转账");
        int effectiveRowsOfAccountFrom = accountMapper.minusFrom(from);
        int effectiveRowsOfAccountTo = accountMapper.plusTo(to);
        Boolean isTransferFail = effectiveRowsOfAccountFrom < 1 || effectiveRowsOfAccountTo < 1;
        if (isTransferFail) {
            logger.warn("transfer 转账失败: 'From' = {}, 'To' = {}", effectiveRowsOfAccountFrom, effectiveRowsOfAccountTo);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "转账失败", null);
        }
        StringBuilder stringBuilder = new StringBuilder(BillResponseEnum.TRANSFER_SUCCESS.getMessage());
        return PublicResponse.builder()
                .code(BillResponseEnum.TRANSFER_SUCCESS.getResponseCode())
                .message(
                        stringBuilder
                                .append(", 从 ")
                                .append(transferDTO.getFrom())
                                .append(" 转出 ")
                                .append(transferDTO.getMoney())
                                .append(" 元到 ")
                                .append(transferDTO.getTo())
                                .append(" : ")
                                .append(transferDTO.getDescription())
                                .toString()
                )
                .build();
    }

    @Override
    public PublicResponse getPaid(String userId) throws ServiceException {
        BillDTO billDTO = BillDTO
                .builder()
                .money(3500D)
                .account("cmb")
                .uuid(String.valueOf(UUID.randomUUID()))
                .build();
        billDTO.setUserId(userId);
        logger.info("getPaid 生成工资账单");
        Integer i = billMapper.getPaid(billDTO);
        Integer j;
        if (i > 0) {
            logger.info("getPaid 转入工资账户");
            j = accountMapper.plusTo(billDTO);
        } else {
            logger.warn("getPaid 生成工资账单失败: {}", i);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "账单更新失败", null);
        }
        if (j <= 0) {
            logger.warn("getPaid 转入工资账户失败: {}", j);
            throw new ServiceException(ErrorCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "账户更新失败", null);
        }
        return PublicResponse
                .builder()
                .code(BillResponseEnum.GET_PAID_SUCCESS.getResponseCode())
                .message(BillResponseEnum.GET_PAID_SUCCESS.getMessage())
                .result(i)
                .build();
    }

    public PublicResponse selectByFilter(SelectBillDTO selectBillDTO) throws ServiceException {
        Double gt;
        Double lt;
        gt = selectBillDTO.getGreaterThan();
        lt = selectBillDTO.getLessThan();
        if (gt != null && lt != null) {
            if (lt < gt || gt > lt) {
                throw new ServiceException(ErrorCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "数字区间错误", null);
            }
        }
        logger.info("selectByFilter 模糊筛选");
        List<BillDO> billList = billMapper.selectBillList(selectBillDTO);
        List<BillVO> billVOList = new ArrayList<>();
        for (BillDO billDO : billList) {
            if (billDO.getDeleted() == 0) {
                billVOList.add(billDOToBillVO(billDO));
            }
        }
        StringBuilder stringBuilder = new StringBuilder(BillResponseEnum.GET_LIST_SUCCESS.getMessage());
        return PublicResponse.builder()
                .code(BillResponseEnum.GET_LIST_SUCCESS.getResponseCode())
                .message(stringBuilder
                        .append(", 共")
                        .append(billList.size())
                        .append("条")
                        .toString())
                .result(billList)
                .build();
    }

    private HashMap<String, BillDTO> transferDTOToBillDTO(TransferDTO transferDTO) {
        String description = transferDTO.getDescription();
        Double money = transferDTO.getMoney();
        BillDTO from = BillDTO.builder()
                .account(transferDTO.getFrom())
                .description(description)
                .money(money)
                .type(BillTypeEnum.EXPENSE.getType())
                .uuid(String.valueOf(UUID.randomUUID()))
                .build();
        from.setUserId(transferDTO.getUserId());
        BillDTO to = BillDTO.builder()
                .account(transferDTO.getTo())
                .description(description)
                .money(money)
                .type(BillTypeEnum.INCOME.getType())
                .uuid(String.valueOf(UUID.randomUUID()))
                .build();
        to.setUserId(transferDTO.getUserId());
        HashMap<String, BillDTO> map = new HashMap<>(2);
        map.put("from", from);
        map.put("to", to);
        return map;
    }

    private boolean isAccountNotExist(String userId, String account) {
        List<String> allAccount = accountMapper.getAllAccount(userId);
        String tempAccount = null;
        String dtoAccount = account;
        for (String s : allAccount) {
            if (s.equals(dtoAccount)) {
                tempAccount = dtoAccount;
            }
        }
        return tempAccount == null;
    }

    private BillVO billDOToBillVO(BillDO billDO) {
        return BillVO
                .builder()
                .account(billDO.getAccount())
                .createTime(billDO.getCreateTime())
                .description(billDO.getDescription())
                .money(billDO.getMoney())
                .type(billDO.getType())
                .uuid(billDO.getUuid())
                .build();
    }
}
