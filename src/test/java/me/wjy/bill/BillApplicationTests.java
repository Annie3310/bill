package me.wjy.bill;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.service.impl.UserServiceImpl;
import me.wjy.bill.utils.JWTUtil;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

@SpringBootTest
class BillApplicationTests {
    @Autowired
    UserServiceImpl userService;

    @Test
    void contextLoads() {
        CRC32 crc32 = new CRC32();
        crc32.update("123456".getBytes());
        System.out.println(crc32.getValue());
    }

}
