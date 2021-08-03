package me.wjy.bill.aspect;

import me.wjy.bill.pojo.dto.UserDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author 王金义
 * @date 2021/8/3
 */
@Aspect
@Component
public class UserControllerAspect {
    private final Logger logger = LoggerFactory.getLogger(UserControllerAspect.class);

    @Pointcut("execution(* me.wjy.bill.controller.UserController.register(..))")
    public void register() {
    }

    @Before("register()")
    public String parameterVerification(JoinPoint joinPoint) {
        // TODO 使用切面验证是否输入密码
        // FIXME 如果不在请求头中输入密码, 则会报错
        System.out.println("切入");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof UserDTO) {
                if (((UserDTO) arg).getPassword() == null) {
                    logger.warn("UserController.register(): 未输入密码");
                    break;
                }
                if (((UserDTO) arg).getPassword().isEmpty() || ((UserDTO) arg).getPassword().isBlank()) {
                    logger.warn("UserController.register(): 密码为空");
                }
            }
        }
        return "测试";
    }


}
