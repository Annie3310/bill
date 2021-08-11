package me.wjy.bill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 王金义
 */
@SpringBootApplication
@MapperScan("me.wjy.bill.mapper")
@EntityScan
@EnableTransactionManagement
public class BillApplication {
    // TODO 规范密码格式

    public static void main(String[] args) {
        SpringApplication.run(BillApplication.class, args);
    }

}
