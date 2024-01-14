package ru.bk.artv.vkrattach.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.bk.artv.vkrattach.config.logging.ResponseLoggingFilter;

/**
 * Папки с ресурсами и templates
 */
@Configuration
@EnableWebMvc
@Slf4j
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/templates/" );
    }

    @Bean
    public FilterRegistrationBean<ResponseLoggingFilter> loggingFilter() {
        FilterRegistrationBean<ResponseLoggingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ResponseLoggingFilter());
        registrationBean.addUrlPatterns("/rest/*"); // Указать URL-паттерн, по которому будет применяться фильтр
        return registrationBean;
    }


}
