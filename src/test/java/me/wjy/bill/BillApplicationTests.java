package me.wjy.bill;

import me.wjy.bill.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BillApplicationTests {
    @Autowired
    UserServiceImpl userService;

    @Test
    void contextLoads() {

    }

    @Test
    public void getPass() {

    }
}
