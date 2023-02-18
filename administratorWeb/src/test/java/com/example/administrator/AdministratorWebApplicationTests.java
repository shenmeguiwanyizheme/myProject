package com.example.administrator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.mapper.chargingpile.ChargingPileMapper;
import com.example.mapper.parkingplace.ParkingPlaceMapper;
import com.example.mapper.user.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootTest
@Slf4j
class AdministratorWebApplicationTests {
    @Resource
    ChargingPileMapper chargingPileMapper;

    @Resource
    ParkingPlaceMapper parkingPlaceMapper;
    @Resource
    UserMapper userMapper;

    @Test
    public void testTransactional() {
        System.out.println(DigestUtils.md5DigestAsHex("ssszsqqq".getBytes(StandardCharsets.UTF_8)).equals(
                userMapper.extractByUsername("zs").getPassword()
        ));


    }

    @Test
        //@Transactional(isolation = Isolation.READ_COMMITTED)
    void contextLoads() {
        HashMap<String, String> map = new HashMap<>();
        map.put("code", "666");
        String json = JSONObject.toJSONString(map);
        System.out.println(json);
        JSONObject jsonObject = JSON.parseObject(json);
        jsonObject.entrySet();
        
    }


    @Test
    //@Transactional(isolation = Isolation.DEFAULT)//使用数据库隔离级别
    //@Retryable(backoff = @Backoff(delay = 1000L * 1), value = org.springframework.dao.DuplicateKeyException.class)
    public void addData() throws Exception {
        JSONObject jsonObject = new JSONObject();
        JSON json = new JSONObject();
        chargingPileMapper.extractById(null);
    }
    //# 没隔开的时候是ok的，现在来试一下隔开的 如果隔开 ，会直接把这个东西当成要插入的数据
//最后,由于@Retryable注解是通过切面实现的，因此我们要避免@Retryable 注解的方法的调用方和被调用方处于同一个类中，因为这样会使@Retryable 注解失效
    //写在这没用
}





