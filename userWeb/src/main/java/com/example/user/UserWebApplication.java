package com.example.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"com.example", "com.example.service"})//对于这个包之下的类都要扫描进行bean管理
@MapperScan("com.example.mapper")//这个是生成代理类的，mapperlocation是找xml文件的
public class UserWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserWebApplication.class, args);
    }

}
