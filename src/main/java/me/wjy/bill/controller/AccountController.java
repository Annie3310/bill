package me.wjy.bill.controller;

import me.wjy.bill.annotation.GetUserId;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.dto.AccountUpdateDTO;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.service.AccountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王金义
 * @date 2021/8/3
 */
@RestController
@Validated
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetUserId
    @PostMapping("account")
    public PublicResponse addAccount(@RequestBody @Validated AccountDTO accountDTO) throws ServiceException {
        return accountService.addAccount(accountDTO);
    }
    @GetUserId
    @DeleteMapping("account")
    public PublicResponse rmAccount(@RequestBody @Validated AccountDTO accountDTO) throws ServiceException {
        return accountService.rmAccount(accountDTO);
    }

    @GetUserId
    @PutMapping("account")
    public PublicResponse updateAccount(@RequestBody @Validated AccountUpdateDTO accountUpdateDTO) throws ServiceException {
        return accountService.updateAccount(accountUpdateDTO);
    }

    @GetUserId
    @GetMapping("account")
    public PublicResponse getAllAccount(AccountDTO accountDTO) throws ServiceException {
        return accountService.getAllAccount(accountDTO.getUserId());
    }
}
