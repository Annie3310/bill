package me.wjy.bill.controller;

import me.wjy.bill.annotation.GetUserId;
import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.UserDTO;
import me.wjy.bill.response.PublicResponse;
import me.wjy.bill.service.impl.UserServiceImpl;
import me.wjy.bill.utils.JWTUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author 王金义
 * @date 2021/8/3
 */
@RestController
@RequestMapping("user")
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("get_token")
    public PublicResponse getToken(@RequestBody UserDTO userDTO) throws ServiceException {
        userService.getUser(userDTO);
        return PublicResponse
                .builder()
                .code(ResponseCodeEnum.OK.getErrorCode())
                .message("获取 Token 成功")
                .result(JWTUtil.getToken(userDTO.getUserId()))
                .build();
    }

    @PostMapping("/register")
    public PublicResponse register(@RequestBody UserDTO userDTO) throws ServiceException {
        return userService.register(userDTO);
    }

    @GetUserId
    @PutMapping("password")
    public PublicResponse updatePassword(@RequestBody UserDTO userDTO) throws ServiceException {
        return userService.updatePassword(userDTO);
    }

    @GetUserId
    @PostMapping("name")
    public PublicResponse updateName(@RequestBody UserDTO userDTO) throws ServiceException {
        return userService.updateName(userDTO);
    }
}
