package com.example.administrator;

import com.example.mapper.ChargingPileMapper;
import com.example.mapper.ParkingPlaceMapper;
import com.example.pojo.ParkingPlace;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigInteger;

@SpringBootTest
@Slf4j
class AdministratorWebApplicationTests {
    @Resource
    ChargingPileMapper chargingPileMapper;

    @Resource
    ParkingPlaceMapper parkingPlaceMapper;

    @Test
    void contextLoads() {
        System.out.println(chargingPileMapper.delete(BigInteger.valueOf(300), (int) (System.currentTimeMillis() / 1000)));
        ParkingPlace p = parkingPlaceMapper.selectById(10);
        System.out.println(p);

        try {
            int a = 1 / 0;
        } catch (Exception e) {
            System.out.println("message:   " + e.getMessage());
            System.out.println("toString:  " + e);
        }
//        ParkingPlace p = new ParkingPlace();// mapper和wrapper
//        parkingPlaceMapper.insert(p.setLocation("aaa").setCreateTime((int) (System.currentTimeMillis() / 1000)).setUpdateTime((int) (System.currentTimeMillis() / 1000)));
//        log.info(p.getId().toString());
//        QueryWrapper<ParkingPlace> queryWrapper = new QueryWrapper<>();
//        queryWrapper.isNotNull("location");
//        queryWrapper.and(i -> i.eq("is_deleted", 0).or().eq("is_deleted", 1));
//        List<ParkingPlace> list = parkingPlaceMapper.selectList(queryWrapper);
//        log.info(list.toString());
//        queryWrapper.select("id", "location", "is_deleted");
//        log.info(parkingPlaceMapper.selectList(queryWrapper).toString());
//
//        //queryWrapper.gt("id", 1).eq("is_deleted", 1);
//        UpdateWrapper<ParkingPlace> updateWrapper = new UpdateWrapper<>();
//        //parkingPlaceMapper.delete(queryWrapper);
//        //and 优先级高于 or 自己进行四则运算的设计
//        // parkingPlaceMapper.update(new ParkingPlace(), queryWrapper);
//        //条件间拼接默认是and，使用了or（）之后的第一个条件用or拼接
//        //lambda的条件优先执行
//        //   queryWrapper.and(i -> i.eq("id", 1).and(k-> k.eq("id",1)));
//        //管理员查询接口的条件编写
//
//        //光看手机，越学越困
//        queryWrapper.like(new ParkingPlace().getCreateTime() != null, "location", "aaa");

    }

}
