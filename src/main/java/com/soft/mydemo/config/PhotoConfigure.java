package com.soft.mydemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置博客文件的映射路径
 */
@Configuration
public class PhotoConfigure implements WebMvcConfigurer {
    // @Value可以将配置文件的内容自动注入到属性内
    @Value("${upload.blog}")
    private String blogPath; // 图标物理存储路径
    @Value("${upload.blogImgMapper}")
    private String blogMapperPath; // 图标映射路径

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(blogMapperPath + "**").addResourceLocations("file:" + blogPath);
    }
}
