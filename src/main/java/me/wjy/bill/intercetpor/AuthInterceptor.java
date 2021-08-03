package me.wjy.bill.intercetpor;

import me.wjy.bill.pojo.po.UserDO;
import me.wjy.bill.pojo.dto.UserDTO;
import me.wjy.bill.service.impl.UserServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 王金义
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final UserServiceImpl userService;


    public AuthInterceptor(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String id = request.getHeader("id");
        String password = request.getHeader("password");
        UserDTO userDTO = UserDTO.builder().password(password).build();
        userDTO.setUserId(id);
        UserDO user = userService.getUser(userDTO);
        return user != null;
    }
}
