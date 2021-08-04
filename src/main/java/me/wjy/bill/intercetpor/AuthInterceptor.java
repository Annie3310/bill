package me.wjy.bill.intercetpor;

import me.wjy.bill.utils.JWTUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证, 已弃用
 * @author 王金义
 */
@Deprecated
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        String id = null;
        if (token != null) {
            id = JWTUtil.checkToken(token);
        }
        return id != null;
    }
}
