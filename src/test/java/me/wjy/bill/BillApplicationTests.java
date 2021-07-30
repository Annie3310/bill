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

    }
}
