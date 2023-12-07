package com.hao.content;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 课程管理服务启动类
 *
 * @author hao
 */
@SpringBootApplication(scanBasePackages = {"com.hao", "com.hao.base.handler"})
@EnableSwagger2Doc
public class ContentApplication {

    public static void main(String[] args) {

        SpringApplication.run(ContentApplication.class,args);
    }
}
