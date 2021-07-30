package me.wjy.bill.controller;

import me.wjy.bill.enums.ErrorEnum;
import me.wjy.bill.response.PublicResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 王金义
 */
@RestController
@RequestMapping("error")
public class Error {
    @RequestMapping("auth")
    public PublicResponse error() {
        return PublicResponse
                .builder()
                .code(ErrorEnum.AUTH_FAIL.getCode())
                .message(ErrorEnum.AUTH_FAIL.getMessage())
                .build();
    }
}
