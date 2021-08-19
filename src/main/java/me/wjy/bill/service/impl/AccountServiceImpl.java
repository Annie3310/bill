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
        String className = getClass().getName();
        AccountDO name = accountMapper.getByName(accountDTO);
        if (name != null) {
            logger.warn("{}.addAccount: 要添加的账户已存在", className);
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "该账户已经存在");
        }
        Double money = accountDTO.getBalance();
        // 因为向账单添加账单后会更新账户, 新建账户后将余额置为 0, 在账单添加后会将余额更新
        // 问题: 多执行了一条 SQL, 少写了一次添加账单的方法
        accountDTO.setBalance(0D);
        int accountInsert = accountMapper.addAccount(accountDTO);
        logger.info("{}.addAccount: 添加账户: {}", className, accountInsert);
        BillDTO billDTO = BillDTO
                .builder()
                .account(accountDTO.getName())
                .description("创建账户")
                .money(money == null ? 0 : money)
                .build();
        billDTO.setUserId(accountDTO.getUserId());
        PublicResponse income = billService.income(billDTO);
        logger.info("{}.addAccount: 向账户中添加创建账户的记录: {}",className, income.getCode());
        if (accountInsert < 1 || !Objects.equals(income.getCode(), BillResponseEnum.INCOME_SUCCESS.getResponseCode())) {
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "新建账户失败, 账单插入未成功");
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("添加成功")
                .build();
    }

    @Override
    public PublicResponse rmAccount(AccountDTO accountDTO) throws ServiceException {
        String className = getClass().getName();
        Integer integer = accountMapper.rmAccount(accountDTO);
        logger.info("{}.rmAccount: 删除账户: {}",className, integer);
        if (integer == null || integer < 1) {
            logger.warn("{}.rmAccount: 删除未成功, 可能是该账户不存在", className);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "删除未成功, 可能是该账户不存在");
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("删除成功")
                .build();
    }

    @Override
    public PublicResponse updateAccount(AccountUpdateDTO accountUpdateDTO) throws ServiceException {
        String className = getClass().getName();
        AccountDTO accountDTO = new AccountDTO();
        if (accountUpdateDTO.getBalance() == null & accountUpdateDTO.getNewName() == null) {
            logger.warn("{}.updateAccount: 未输入要修改的数据", className);
            throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "未输入需要修改的数据");
        }
        accountDTO.setName(accountUpdateDTO.getOldName());
        accountDTO.setUserId(accountUpdateDTO.getUserId());
        AccountDO name = accountMapper.getByName(accountDTO);
        logger.info("{}.updateAccount: 查找要修改的账户名", className);
        if (name == null) {
            logger.warn("{}.updateAccount: 未找到要修改的账户",className);
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "未找到要修改的账户");
        }
        Integer integer = accountMapper.updateAccount(accountUpdateDTO);
        logger.info("{}.updateAccount: 更新账户: {}", className, integer);
        if (integer == null || integer < 1) {
            logger.warn("{}.updateAccount: 更新账户信息失败, 数据库执行结果不为 1", className);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "更新账户信息未成功");
        }
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("更新成功")
                .build();
    }

    @Override
    public PublicResponse getAllAccount(String userId) throws ServiceException {
        String className = getClass().getName();
        List<AccountDO> allAccount = accountMapper.getAllAccount(userId);
        logger.info("{}.getAllAccount: 获取所有账户: size() --> {}", className, allAccount.size());
        if (allAccount.isEmpty()) {
            logger.warn("{}.getAllAccount: 结果列表为空, 或 SQL 未执行成功, 或该账号下没有账户", className);
            throw new ServiceException(ResponseCodeEnum.SYSTEM_EXECUTION_ERROR.getErrorCode(), "未获取成功, 或该账号下没有任何账户");
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
