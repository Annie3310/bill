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

    // TODO 封装 Redis
    // TODO 限制请求 get_token 接口的次数 (请求该接口无需携带 Token, 也就不能以 userId 作为限制标准)

    public static void main(String[] args) {
        SpringApplication.run(BillApplication.class, args);
    }

}
