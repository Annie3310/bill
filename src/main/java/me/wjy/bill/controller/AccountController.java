package me.wjy.bill.controller;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.dto.AccountUpdateDTO;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王金义
 * @date 2021/8/3
 */
@RestController
public class AccountController {
    private final UserServiceImpl userService;

    public AccountController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("account")
    public PublicResponse addAccount(@RequestBody AccountDTO accountDTO) throws ServiceException {
        return userService.addAccount(accountDTO);
    }

    @DeleteMapping("account")
    public PublicResponse rmAccount(@RequestBody AccountDTO accountDTO) throws ServiceException {
        return userService.rmAccount(accountDTO);
    }

    @PutMapping("account")
    public PublicResponse updateAccount(@RequestBody AccountUpdateDTO accountUpdateDTO) throws ServiceException {
        return userService.updateAccount(accountUpdateDTO);
    }

    @GetMapping("account")
    public PublicResponse getAllAccount(@RequestBody AccountDTO accountDTO) throws ServiceException {
        return userService.getAllAccount(accountDTO.getUserId());
    }
}