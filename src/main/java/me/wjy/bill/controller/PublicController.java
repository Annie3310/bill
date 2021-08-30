package me.wjy.bill.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 王金义
 * @date 2021/8/24
 */
@Controller
public class PublicController {

    @Value("${project-root-path}")
    private String rootPath;

    @RequestMapping("/")
    public String toProfilePage() {
        return "redirect:" + rootPath;
    }
}
