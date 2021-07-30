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
        UserDTO userDTO = UserDTO.builder().password("L6isEeFgjzj5yHC2WXGw").build();
        userDTO.setUserId("846a4e9f-91e4-42a7-ab20-391417af3546");
        UserDO user = userService.getUser(userDTO);
        System.out.println(LocalDate.now());
        Assert.assertEquals(UserDO
                .builder()
                .userId("846a4e9f-91e4-42a7-ab20-391417af3546")
                .password("5a55e90d655d0d5cae6a5b082413aff138f0ed3166973c2de859a9de825ae44d")
                .build(), user);
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
