package me.wjy.bill.config;

import me.wjy.bill.intercetpor.RegisterInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 王金义
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final RegisterInterceptor registerInterceptor;

    public WebConfig(RegisterInterceptor registerInterceptor) {
        this.registerInterceptor = registerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(registerInterceptor)
                .addPathPatterns("/user/register");
    }
}
