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
    /*  使用参数解析器, 而不是切面来添加 userId 进参数 <-- 有问题, 见下文
            如果自定义一个注解给想要解析的参数, 会由于 @RequestBody 解析器的优先级比自定义的解析器的优先级高 (内置大于自定义)
            会导致自定义的解析器可以被注册但是不生效, 如果要添加一个参数会比较麻烦
            使用 token 认证:
            在 token 中携带 id 参数, 如果 token 认证通过, 则每次在切面中从 token 中获取 id 值, 传入 DTO 中
     */
    // TODO 修改账户相关的操作时, 需要重新提供 ID 和密码


    public static void main(String[] args) {
        SpringApplication.run(BillApplication.class, args);
    }

}
