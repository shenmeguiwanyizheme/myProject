package com.example.user.controller;

import com.example.mapper.chargingpile.ChargingPileMapper;
import com.example.user.domain.chargingpile.ChargingPileSpecificInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.HashMap;

@SpringBootTest
@Slf4j
@Configuration
public class ServiceApplicationTests {

    @Resource
    ChargingPileMapper entityMapper;

    @Bean
    //卵用没有 @Scope("singleton")
    public static ChargingPileSpecificInfoVO getVO() {
        return new ChargingPileSpecificInfoVO();
    }

    @Test

    public void test() {
        HashMap<String, ChargingPileSpecificInfoVO> hashMap = new HashMap<>();
        ChargingPileSpecificInfoVO specificInfoVO = new ChargingPileSpecificInfoVO().setId(BigInteger.valueOf(13));
        hashMap.put("123", specificInfoVO);
        specificInfoVO.setId(BigInteger.valueOf(15));
        System.out.println(hashMap.get("123").getId());

    }
}
//