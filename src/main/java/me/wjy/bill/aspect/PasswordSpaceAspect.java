package me.wjy.bill.aspect;

import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.UpdatePasswordDTO;
import me.wjy.bill.pojo.dto.UserDTO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author 王金义
 * @date 2021/8/11
 * {@link me.wjy.bill.annotation.VerifySpace}
 */
@Aspect
@Component
@Order(1)
public class PasswordSpaceAspect {
    @Pointcut("@annotation(me.wjy.bill.annotation.VerifySpace)")
    public void verifySpace() {
    }

    @Before("verifySpace()")
    public void verifySpace(JoinPoint joinPoint) throws ServiceException {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof UserDTO) {
                boolean contains = ((UserDTO) arg).getPassword().contains(" ");
                if (contains) {
                    throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "密码中不可以包含空格", null);
                }
            }
            if (arg instanceof UpdatePasswordDTO) {
                boolean contains = ((UpdatePasswordDTO) arg).getNewPassword().contains(" ");
                if (contains) {
                    throw new ServiceException(ResponseCodeEnum.USER_REQUEST_PARAM_ERROR.getErrorCode(), "密码中不可以包含空格", null);
                }
            }
        }
    }
}
