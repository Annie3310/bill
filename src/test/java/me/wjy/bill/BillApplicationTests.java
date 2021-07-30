package me.wjy.bill;

import me.wjy.bill.controller.BillController;
import me.wjy.bill.controller.Error;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.po.UserDO;
import me.wjy.bill.pojo.dto.UserDTO;
import me.wjy.bill.service.impl.UserServiceImpl;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class BillApplicationTests {
    @Autowired
    UserServiceImpl userService;

    @Test
    void contextLoads() throws ServiceException {

    }

    @Test
    public void getPass() {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("1ffddab75e0bf3cda4e9eb85d980fb4dcdb0a6121c7ea923019c4268");
        String url = textEncryptor.encrypt("jdbc:mysql://115.159.123.218:3306/bill?characterEncoding=UTF-8&useUnicode=true&serverTimezone=UTC");
        String name = textEncryptor.encrypt("remote");
        String password = textEncryptor.encrypt("Haibara1.");

        System.out.println(url);
        System.out.println(name);
        System.out.println(password);
    }
}
