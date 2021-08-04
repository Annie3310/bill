package me.wjy.bill.config;

import me.wjy.bill.intercetpor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author 王金义
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

}
