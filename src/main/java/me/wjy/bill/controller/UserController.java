package me.wjy.bill.controller;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.AccountDTO;
import me.wjy.bill.pojo.dto.UserDTO;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王金义
 * @date 2021/8/3
 */
@RestController()
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public PublicResponse register(@RequestBody UserDTO userDTO) throws ServiceException {
        return userService.register(userDTO);
    }

}
