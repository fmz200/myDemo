package com.soft.mydemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author admin
 */
@SpringBootApplication
public class MyDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyDemoApplication.class, args);
    }

    /**
     * 修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
     */
    public static class ServletInitializer extends SpringBootServletInitializer {
        public ServletInitializer() {
            System.out.println("初始化ServletInitializer");
        }

        @Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
            // 注意这里要指向原先用main方法执行的Application启动类
            return builder.sources(MyDemoApplication.class);
        }
    }

}
