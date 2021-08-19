package me.wjy.bill.aspect;

import me.wjy.bill.enums.ResponseCodeEnum;
import me.wjy.bill.exception.ServiceException;
import me.wjy.bill.pojo.dto.BaseDTO;
import me.wjy.bill.utils.JWTUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取 Token 中的 userId
 * @author 王金义
 * @date 2021/8/3
 * {@link me.wjy.bill.annotation.GetUserId}
 */
@Aspect
@Component
@Order(2)
public class PublicControllerAspect {
    private final Logger logger = LoggerFactory.getLogger(PublicControllerAspect.class);

    @Pointcut("@annotation(me.wjy.bill.annotation.GetUserId)")
    public void getUserId() {
    }

    @Before("getUserId()")
    public void setUserId(JoinPoint joinPoint) throws ServiceException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        assert servletRequestAttributes != null;
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        String token = httpServletRequest.getHeader("token");
        String id;
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();
        if (token != null) {
            id = JWTUtil.checkToken(token);
        } else {
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "未携带 token");
        }
        if (id == null) {
            logger.warn(className + "." + methodName + "Token 验证未通过");
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "token 不正确");
        }
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseDTO) {
                ((BaseDTO) arg).setUserId(id);
            }
        }
    }

}
