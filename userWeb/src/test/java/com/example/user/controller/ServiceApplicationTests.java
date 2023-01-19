package com.example.user.controller;

import com.example.mapper.ChargingPileMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class ServiceApplicationTests {

    @Resource
    ChargingPileMapper entityMapper;

    @Test

    public void conflictTime() {
        log.info("x");

    }
}
//