package me.wjy.bill.aspect;

import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.BaseDTO;
import me.wjy.bill.pojo.dto.UserDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 限制接口访问次数
 *
 * @author 王金义
 * @date 2021/8/12
 */
@Component
@Aspect
@Order(3)
public class AccessLimitAspect {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Logger logger = LoggerFactory.getLogger(AccessLimitAspect.class);
    /**
     * 每分钟可以访问多少次接口
     */
    private final Integer INTERFACE_ACCESS_PER_MIN_AMOUNT = 5;
    /**
     * get_token 接口单个账号每天可访问次数
     */
    private final Integer GET_TOKEN_INTERFACE_ACCESSIBLE_PER_DAY_AMOUNT = 15;
    /**
     * Redis 自增步长
     */
    private final Long REDIS_INCR_STEP_LENGTH = 1L;

    public AccessLimitAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Pointcut("execution(* me.wjy.bill.controller.*.*(..)) && !execution(* me.wjy.bill.controller.UserController.getToken(..)) && !execution(* me.wjy.bill.controller.UserController.register(..))")
    public void interfaceLimit() {
    }

    @Pointcut("execution(* me.wjy.bill.controller.UserController.getToken(..))")
    public void getTokenLimit() {
    }

    @Before("interfaceLimit()")
    public void accessLimit(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseDTO) {
                String userId = ((BaseDTO) arg).getUserId();
                Integer accessAmount = (Integer) redisTemplate.opsForValue().get(userId);
                if (accessAmount != null) {
                    if (accessAmount < INTERFACE_ACCESS_PER_MIN_AMOUNT) {
                        redisTemplate.opsForValue().increment(userId, REDIS_INCR_STEP_LENGTH);
                    } else {
                        throw new ServiceException(
                                ResponseCodeEnum.NUMBER_OF_REQUEST_EXCEEDED_THE_LIMIT.getErrorCode()
                                , ResponseCodeEnum.NUMBER_OF_REQUEST_EXCEEDED_THE_LIMIT.getErrorMessage());
                    }
                } else {
                    redisTemplate.opsForValue().set(userId, 1);
                    redisTemplate.expire(userId, 1, TimeUnit.MINUTES);
                }
            }
        }
    }

    /**
     * 限制每天最多可以请求多少次 get_token 接口
     *
     * @param joinPoint 切入点
     */
    @Before("getTokenLimit()")
    public void getTokenLimit(JoinPoint joinPoint) {
        String currentMethodName = this.getClass().getName() + "-" + Thread.currentThread().getStackTrace()[1].getMethodName();
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof UserDTO) {
                String userId = ((UserDTO) arg).getUserId();
                if (userId == null) {
                    throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "未输入用户名");
                }
                String key = userId + "_get_token";
                Integer getTokenAccessAmount = (Integer) redisTemplate.opsForValue().get(key);
                // 如果未获取到该值, 说明该用户今日未访问过, redis 中没有该 key, 新建一个 key, 如果有, 则检查是否大于最大访问数
                if (getTokenAccessAmount != null) {
                    if (getTokenAccessAmount < GET_TOKEN_INTERFACE_ACCESSIBLE_PER_DAY_AMOUNT) {
                        logger.info(currentMethodName + ": 自增后的次数 {}", redisTemplate.opsForValue().increment(key, REDIS_INCR_STEP_LENGTH));
                    } else {
                        throw new ServiceException(
                                ResponseCodeEnum.NUMBER_OF_REQUEST_EXCEEDED_THE_LIMIT.getErrorCode()
                                , "每天最多可以请求 " + GET_TOKEN_INTERFACE_ACCESSIBLE_PER_DAY_AMOUNT + " 次 get_token 接口");
                    }
                } else {
                    redisTemplate.opsForValue().set(key, 1);
                    redisTemplate.expire(key, 1, TimeUnit.DAYS);
                }
            }
        }
    }


}
