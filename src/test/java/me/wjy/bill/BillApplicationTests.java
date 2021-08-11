package me.wjy.bill;

import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.service.impl.UserServiceImpl;
import me.wjy.bill.utils.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

@SpringBootTest
class BillApplicationTests {
    @Autowired
    UserServiceImpl userService;

    private final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxLjAiLCJpc3MiOiJBbm5pZTMzMTAiLCJzdWIiOiJTVUJKRUNUIiwiYXVkIjoiQVVESUVOQ0UiLCJpYXQiOjE2MjgwNDY2OTcsIm5iZiI6MTYyODA0NjY5NywiaWQiOiI4NDZhNGU5ZjkxIiwiZXhwIjoxNjI4MDUwMjk3fQ.0mVc8g08Vt88gDW9_R1Cj34sRi_P2Usk6XWNYN_V-FE";
    @Test
    void contextLoads() {
        CRC32 crc32 = new CRC32();
        crc32.update("123456".getBytes());
        System.out.println(crc32.getValue());
    }

}
