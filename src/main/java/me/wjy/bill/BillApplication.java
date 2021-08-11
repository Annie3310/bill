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
    // TODO 修改账户相关的操作时, 需要重新提供 ID 和密码
    // TODO 规范密码格式

    public static void main(String[] args) {
        SpringApplication.run(BillApplication.class, args);
    }

}
