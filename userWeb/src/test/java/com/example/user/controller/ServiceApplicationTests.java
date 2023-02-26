package com.example.user.controller;

import com.example.mapper.chargingpile.ChargingPileMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
@Component
@EnableAspectJAutoProxy(exposeProxy = true)
public class ServiceApplicationTests {

    @Resource
    ChargingPileMapper entityMapper;


    @Test
    public void test() {
    }

    public static void main(String[] args) {

    }
}
//