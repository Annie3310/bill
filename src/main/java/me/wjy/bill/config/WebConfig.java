package me.wjy.bill.config;

import me.wjy.bill.aspect.UserIdResolver;
import me.wjy.bill.intercetpor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 王金义
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final UserIdResolver userIdResolver;

    public WebConfig(AuthInterceptor authInterceptor, UserIdResolver userIdResolver) {
        this.authInterceptor = authInterceptor;
        this.userIdResolver = userIdResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/error");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        Method[] methods = userIdResolver.getClass().getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
        resolvers.add(userIdResolver);
    }
}
