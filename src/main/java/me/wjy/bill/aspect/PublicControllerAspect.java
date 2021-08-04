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
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 王金义
 * @date 2021/8/3
 */
@Aspect
@Component
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
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "未携带 token", null);
        }
        if (id == null) {
            logger.warn(className + "." + methodName + "Token 验证未通过");
            throw new ServiceException(ResponseCodeEnum.USER_AUTH_FAIL_ERROR.getErrorCode(), "token 不正确", null);
        }
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BaseDTO) {
                ((BaseDTO) arg).setUserId(id);
            }
        }
    }

}
