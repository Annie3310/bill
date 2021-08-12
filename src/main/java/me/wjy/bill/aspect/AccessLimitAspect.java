package me.wjy.bill.aspect;

import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.BaseDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 王金义
 * @date 2021/8/12
 */
@Component
@Aspect
@Order(3)
public class AccessLimitAspect {
    private final RedisTemplate redisTemplate;

    public AccessLimitAspect(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Pointcut("execution(* me.wjy.bill.controller.*.*(..)) && !execution(* me.wjy.bill.controller.UserController.getToken(..))")
    public void interfaceLimit() {
    }

    @Pointcut("execution(* me.wjy.bill.controller.UserController.getToken(..))")
    public void getTokenLimit(){}

    @Before("interfaceLimit()")
    public void accessLimit(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseDTO) {
                String userId = ((BaseDTO) arg).getUserId();
                Integer accessAmount = (Integer) redisTemplate.opsForValue().get(userId);
                if (accessAmount != null) {
                    if (accessAmount >= 5) {
                        throw new ServiceException(
                                ResponseCodeEnum.NUMBER_OF_REQUEST_EXCEEDED_THE_LIMIT.getErrorCode()
                                , ResponseCodeEnum.NUMBER_OF_REQUEST_EXCEEDED_THE_LIMIT.getErrorMessage()
                                , null);
                    } else {
                        redisTemplate.opsForValue().increment(userId, 1L);
                    }
                } else {
                    redisTemplate.opsForValue().set(userId, 1);
                    redisTemplate.expire(userId, 1, TimeUnit.MINUTES);
                }
            }
        }
    }


}
