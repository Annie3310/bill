package me.wjy.bill.service.impl;

import me.wjy.bill.enums.BillResponseEnum;
import me.wjy.bill.enums.BillTypeEnum;
import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.enums.UUIDConfig;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.mapper.AccountMapper;
import me.wjy.bill.mapper.BillMapper;
import me.wjy.bill.pojo.dto.*;
import me.wjy.bill.pojo.po.AccountDO;
import me.wjy.bill.pojo.po.BillDO;
import me.wjy.bill.pojo.vo.AccountVO;
import me.wjy.bill.pojo.vo.BillVO;
import me.wjy.bill.response.GetSumResponse;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.service.BillService;
import me.wjy.bill.utils.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * @author 王金义
 */
@Service
public class BillServiceImpl implements BillService {
    private final AccountMapper accountMapper;
    private final BillMapper billMapper;
    private final Logger logger = LoggerFactory.getLogger(BillServiceImpl.class);

    private final Integer UUID_LENGTH = UUIDConfig.UUID_LEN;

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
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未获取到账户详细信息");
        }
        Double sum = 0D;
        Map<String, Double> detailsMap = new HashMap<>(16);
        for (AccountVO sumDetail : sumDetails) {
            if (sumDetail.getBalance() != 0) {
                detailsMap.put(sumDetail.getName(), sumDetail.getBalance());
                sum+=sumDetail.getBalance();
            }
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
        if (isAccountNotExist(billDTO.getUserId(), billDTO.getAccount())) {
            throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "用户 '" + billDTO.getUserId() + "' 没有该账户");
        }
        billDTO.setUuid(UUIDUtil.getUUID(UUID_LENGTH));
        billDTO.setType(BillTypeEnum.INCOME.getType());
        logger.info("income 开始生成账单");
        int effectiveRowsOfBill = billMapper.insert(billDTO);
        int effectiveRowsOfAccount;
        if (effectiveRowsOfBill > 0) {
            logger.info("income 开始向账户添加金额");
            effectiveRowsOfAccount = accountMapper.plusTo(billDTO);
        } else {
            logger.warn("income 生成账单失败: {}", effectiveRowsOfBill);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "账单添加失败");
        }
        if (effectiveRowsOfAccount <= 0) {
            logger.warn("income 向账户添加金额失败: {}", effectiveRowsOfAccount);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "交易执行失败");
        }
        StringBuilder stringBuilder = new StringBuilder(BillResponseEnum.INCOME_SUCCESS.getMessage());
        return PublicResponse.builder()
                .code(BillResponseEnum.INCOME_SUCCESS.getResponseCode())
                .message(stringBuilder
                        .append("'")
                        .append(billDTO.getAccount())
                        .append("'收入了 ")
                        .append(billDTO.getMoney())
                        .append(" 元")
                        .toString())
                .result(effectiveRowsOfBill).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse expense(BillDTO billDTO) throws ServiceException {
        if (isAccountNotExist(billDTO.getUserId(), billDTO.getAccount())) {
            throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "用户 '" + billDTO.getUserId() + "' 没有该账户");
        }
        billDTO.setUuid(UUIDUtil.getUUID(UUID_LENGTH));
        logger.info("expense 开始生成账单");
        int effectiveRowsOfBill = billMapper.insert(billDTO);
        int effectiveRowsOfAccount;
        if (effectiveRowsOfBill > 0) {
            logger.info("expense 从账户转出");
            effectiveRowsOfAccount = accountMapper.minusFrom(billDTO);
        } else {
            logger.warn("expense 生成账单失败: {}", effectiveRowsOfBill);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "账单添加失败");
        }
        if (effectiveRowsOfAccount <= 0) {
            logger.warn("expense 从账户转出失败: {}", effectiveRowsOfAccount);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "交易执行失败");
        }
        StringBuilder stringBuilder = new StringBuilder(BillResponseEnum.EXPENSE_SUCCESS.getMessage());
        return PublicResponse.builder()
                .code(BillResponseEnum.EXPENSE_SUCCESS.getResponseCode())
                .message(stringBuilder
                        .append(" 从'")
                        .append(billDTO.getAccount())
                        .append("'支出了 ")
                        .append(billDTO.getMoney())
                        .append(" 元")
                        .toString())
                .result(effectiveRowsOfBill)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse transfer(TransferDTO transferDTO) throws ServiceException {
        logger.info("transfer 开始从输入中获取 BillDTO");
        HashMap<String, BillDTO> map = transferToBill(transferDTO);
        BillDTO from = map.get("from");
        BillDTO to = map.get("to");
        // 判重和判空
        if (Objects.equals(from.getAccount(), to.getAccount())) {
            throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "两个请求账户相同");
        }
        if (isAccountNotExist(transferDTO.getUserId(), from.getAccount(), to.getAccount())) {
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "未找到账户");
        }
        // 账单插入是否生效
        logger.info("transfer 开始生成转账 From 账单");
        int effectiveRowsOfFrom = billMapper.insert(from);
        logger.info("transfer 开始生成转账 To 账单");
        int effectiveRowsOfTo = billMapper.insert(to);
        boolean isInsertFail = effectiveRowsOfFrom < 1 || effectiveRowsOfTo < 1;
        if (isInsertFail) {
            logger.warn("transfer 生成账单失败: 'From' = {}, 'To' = {}", effectiveRowsOfFrom, effectiveRowsOfTo);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "添加账单失败");
        }
        logger.info("transfer 开始进行转账");
        int effectiveRowsOfAccountFrom = accountMapper.minusFrom(from);
        int effectiveRowsOfAccountTo = accountMapper.plusTo(to);
        boolean isTransferFail = effectiveRowsOfAccountFrom < 1 || effectiveRowsOfAccountTo < 1;
        if (isTransferFail) {
            logger.warn("transfer 转账失败: 'From' = {}, 'To' = {}", effectiveRowsOfAccountFrom, effectiveRowsOfAccountTo);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "转账失败");
        }
        StringBuilder stringBuilder = new StringBuilder(BillResponseEnum.TRANSFER_SUCCESS.getMessage());
        String description = transferDTO.getDescription();
        stringBuilder
                .append(", 从 ")
                .append(transferDTO.getFrom())
                .append(" 转出 ")
                .append(transferDTO.getMoney())
                .append(" 元到 ")
                .append(transferDTO.getTo());
        if (description != null) {
            stringBuilder.append(" : ").append(description);
        }
        return PublicResponse.builder()
                .code(BillResponseEnum.TRANSFER_SUCCESS.getResponseCode())
                .message(stringBuilder.toString())
                .build();
    }

    public PublicResponse filter(FilterDTO filterDTO) throws ServiceException {
        Double gt;
        Double lt;
        gt = filterDTO.getGreaterThan();
        lt = filterDTO.getLessThan();
        if (gt != null && lt != null) {
            if (lt < gt) {
                throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "数字区间错误");
            }
        }
        LocalDate startDate = filterDTO.getStartDate();
        LocalDate endDate = filterDTO.getEndDate();
        if (startDate != null && endDate != null) {
            if (Objects.equals(startDate, endDate)) {
                filterDTO.setEndDate(null);
            }
        }
        logger.info("selectByFilter 模糊筛选");
        List<BillDO> billList = billMapper.filter(filterDTO);
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
                .result(billVOList)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse rollback(BillDTO billDTO) {
        String className = getClass().getName();
        String userId = billDTO.getUserId();
        /*
        流程:
            从账单中取出最后一条记录 --> 在账户表中找出最后一条记录所使用的账户 --> 获取最后一条账单的类型 (收入 1 / 支出 0) --> 收入则减去账单中的金额, 反之
            --> 更新账户 --> 将最后一条记录更新为已删除 (deleted = 1)
         */
        // 从账单中取出最后一条记录
        BillDO lastOne = billMapper.selectLast(userId);
        if (lastOne == null) {
            logger.warn("{}.rollback: 未找到最后一条记录, 数据库返回了 null", className);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未找到最后一条记录");
        }
        String accountName = lastOne.getAccount();
        AccountDTO accountDTO = AccountDTO.builder().name(accountName).build();
        accountDTO.setUserId(userId);
        // 在账户表中找出最后一条记录所使用的账户
        AccountDO accountDO = accountMapper.getByName(accountDTO);
        if (accountDO == null) {
            logger.warn("{}.rollback: 未找到最后一条记录所对应的账户信息, 数据库返回了 null", className);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未找到最后一条记录对应的账户信息");
        }
        // 获取最后一条账单的类型 (收入 1 / 支出 0) --> 收入则减去账单中的金额, 反之
        AccountUpdateDTO accountUpdateDTO = AccountUpdateDTO.builder().oldName(accountName).build();
        accountUpdateDTO.setUserId(userId);
        accountUpdateDTO.setBalance(lastOne.getType() == 1 ? (accountDO.getBalance() - lastOne.getMoney()) : (accountDO.getBalance() + lastOne.getMoney()));
        // 更新账户
        Integer updateAccountI = accountMapper.updateAccount(accountUpdateDTO);
        if (updateAccountI < 1) {
            logger.warn("{}.rollback: 账户未更新成功, 数据库返回了 {}", className, updateAccountI);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "账户未更新成功");
        }
        BillDO billDO = BillDO.builder().deleted(1).userId(userId).id(lastOne.getId()).build();
        // 将最后一条记录更新为已删除 (deleted = 1)
        Integer updateBillI = billMapper.update(billDO);
        logger.info("{}.rollback: 执行账单回滚, 数据库执行记录数: {}",className, updateBillI);
        if (updateBillI < 1) {
            logger.warn("{}.rollback: 账单回滚失败, 数据库未执行成功", className);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "回滚失败");
        }
        return PublicResponse
                .builder()
                .code(BillResponseEnum.ROLLBACK_SUCCESS.getResponseCode())
                .message(BillResponseEnum.ROLLBACK_SUCCESS.getMessage())
                .build();
    }

    private HashMap<String, BillDTO> transferToBill(TransferDTO transferDTO) {
        String description = transferDTO.getDescription();
        Double money = transferDTO.getMoney();
        BillDTO from = BillDTO.builder()
                .account(transferDTO.getFrom())
                .description(description)
                .money(money)
                .uuid(UUIDUtil.getUUID(UUID_LENGTH))
                .build();
        from.setUserId(transferDTO.getUserId());
        BillDTO to = BillDTO.builder()
                .account(transferDTO.getTo())
                .description(description)
                .money(money)
                .type(BillTypeEnum.INCOME.getType())
                .uuid(UUIDUtil.getUUID(UUID_LENGTH))
                .build();
        to.setUserId(transferDTO.getUserId());
        HashMap<String, BillDTO> map = new HashMap<>(2);
        map.put("from", from);
        map.put("to", to);
        return map;
    }

    private boolean isAccountNotExist(String userId, String... account) {
        int flag = 0;
        for (String s : account) {
            List<AccountDO> allAccount = accountMapper.getAllAccount(userId);
            for (AccountDO accountDO : allAccount) {
                if (Objects.equals(accountDO.getName(), s)) {
                    flag++;
                    break;
                }
            }
        }
        return flag < account.length;
    }

    private BillVO billDOToBillVO(BillDO billDO) {
        return BillVO
                .builder()
                .account(billDO.getAccount())
                .createTime(billDO.getCreateTime())
                .description(billDO.getDescription())
                .money(billDO.getMoney())
                .type(billDO.getType() == 0 ? BillTypeEnum.EXPENSE.getName() : BillTypeEnum.INCOME.getName())
                .uuid(billDO.getUuid())
                .build();
    }

}
