package me.wjy.bill.service.impl;

import me.wjy.bill.enums.BillResponseEnum;
import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.mapper.AccountMapper;
import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.dto.AccountUpdateDTO;
import me.wjy.bill.pojo.dto.BillDTO;
import me.wjy.bill.pojo.po.AccountDO;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.service.AccountService;
import me.wjy.bill.service.BillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author 王金义
 * @date 2021/8/11
 */
@Service
public class AccountServiceImpl implements AccountService {
    private final AccountMapper accountMapper;
    private final BillService billService;
    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl(AccountMapper accountMapper, BillService billService) {
        this.accountMapper = accountMapper;
        this.billService = billService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PublicResponse addAccount(AccountDTO accountDTO) throws ServiceException {
        AccountDO name = accountMapper.getByName(accountDTO);
        if (name != null) {
            logger.warn(getClass().getName() + " addAccount: 该账户已存在");
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "该账户已经存在", null);
        }
        Double money = accountDTO.getBalance();
        accountDTO.setBalance(0D);
        int accountInsert = accountMapper.addAccount(accountDTO);
        BillDTO billDTO = BillDTO
                .builder()
                .account(accountDTO.getName())
                .description("创建账户")
                .money(money == null ? 0 : money)
                .build();
        billDTO.setUserId(accountDTO.getUserId());
        PublicResponse income = billService.income(billDTO);
        if (accountInsert < 1 || !Objects.equals(income.getCode(), BillResponseEnum.INCOME_SUCCESS.getResponseCode())) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "插入未成功", null);
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("添加成功")
                .build();
    }

    @Override
    public PublicResponse rmAccount(AccountDTO accountDTO) throws ServiceException {
        Integer integer = accountMapper.rmAccount(accountDTO);
        if (integer == null || integer < 1) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "删除未成功, 可能是该账户不存在", null);
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("删除成功")
                .build();
    }

    @Override
    public PublicResponse updateAccount(AccountUpdateDTO accountUpdateDTO) throws ServiceException {
        AccountDTO accountDTO = new AccountDTO();
        if (accountUpdateDTO.getBalance() == null & accountUpdateDTO.getNewName() == null) {
            throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "未输入需要修改的数据", null);
        }
        accountDTO.setName(accountUpdateDTO.getOldName());
        accountDTO.setUserId(accountUpdateDTO.getUserId());
        AccountDO name = accountMapper.getByName(accountDTO);
        if (name == null) {
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "未找到该账户", null);
        }
        Integer integer = accountMapper.updateAccount(accountUpdateDTO);
        if (integer == null || integer < 1) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "更新未成功", null);
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("更新成功")
                .build();
    }

    @Override
    public PublicResponse getAllAccount(String userId) throws ServiceException {
        List<AccountDO> allAccount = accountMapper.getAllAccount(userId);
        if (allAccount.isEmpty()) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未获取成功", null);
        }
        HashMap<String, Double> mapOfVO = new HashMap<>(allAccount.size());
        for (AccountDO accountDO : allAccount) {
            mapOfVO.put(accountDO.getName(), accountDO.getBalance());
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("获取成功")
                .result(mapOfVO)
                .build();
    }
}
