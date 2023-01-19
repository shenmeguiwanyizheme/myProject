package com.example.administrator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//分布式、微服务、集群管理、负载均衡、容灾系统、安全架构、日常监控、数据库优化、高并发处理、消息中间件等等很多东西--牢记于心
@SpringBootApplication(scanBasePackages = "com.example")
@MapperScan("com.example")
@EnableAsync//启用异步任务
//@ComponentScan（basePackages="com.example"）和scanBasePackages = "com.example"确实是等价的
//springboot下的bean对象包扫描指定是在@springbootApplication中的，是scanBasePackages=xxxx
public class AdministratorWebApplication {


    public static void main(String[] args) {
        SpringApplication.run(AdministratorWebApplication.class, args);
    }

}
