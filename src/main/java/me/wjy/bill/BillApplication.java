package me.wjy.bill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("me.wjy.bill.mapper")
@EntityScan
@EnableTransactionManagement
public class BillApplication {
    // TODO 使用参数解析器, 而不是切面来添加 userId 进参数
    // TODO 使用 ThreadLocal 来存储已经验证的 userId, 从而不用每次在传参的时候传入 userId
    // TODO 用户插入/删除/更改账户时, 应同时增加对应的账单
    // TODO 使用 JWT 来做参数验证, 而不是每次都验证用户名和密码


    public static void main(String[] args) {
        SpringApplication.run(BillApplication.class, args);
    }

}
