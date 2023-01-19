package com.example.user.controller;

import com.example.pojo.ChargingPile;
import com.example.pojo.ParkingPlace;
import com.example.service.ChargingPileService;
import com.example.service.ParkingPlaceService;
import com.example.user.domain.ChargingPileBaseInfoVO;
import com.example.user.domain.ChargingPileListVO;
import com.example.user.domain.ChargingPileSpecificInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//这种业务是网上查
@RestController
@Slf4j
public class ChargingPileController {
    @Autowired
    private ChargingPileService chargingPileService;
    @Autowired
    private ParkingPlaceService parkingPlaceService;

    @RequestMapping("/chargingPile/list")
    public ChargingPileListVO getList(@RequestParam(name = "serialNumber", required = false) String serialNumber, @RequestParam(name = "page") Integer page, @RequestParam(name = "pId", required = false) Integer pId) {
        log.info("接收到请求");
        List<ChargingPile> temp = chargingPileService.getChargingPileListForUser(serialNumber, page, pId);
        if (temp == null) return new ChargingPileListVO().setIsEnd(true).setChargingPiles(null);
        List<ChargingPileBaseInfoVO> res = new ArrayList<>();
        for (ChargingPile e : temp) {
            ParkingPlace p = null;
            try {
                p = parkingPlaceService.getByIdForAdministrator(e.getParkingPlace());
            } catch (Exception ex) {
                continue;
            }
            ChargingPileBaseInfoVO chargingPileBaseInfoVO = new ChargingPileBaseInfoVO();
            chargingPileBaseInfoVO.setId(e.getId());
            chargingPileBaseInfoVO.setRow(e.getRow());
            chargingPileBaseInfoVO.setCol(e.getCol());
            String[] s = e.getPhotoUrlList().split("$");
            chargingPileBaseInfoVO.setImage(s[0]);
            res.add(chargingPileBaseInfoVO);
        }
        int pageSize = 5;
        ChargingPileListVO userChargingPileListVO = new ChargingPileListVO();
        if (res.size() == pageSize) {
            userChargingPileListVO.setIsEnd(false);
        } else
            userChargingPileListVO.setIsEnd(true);
        userChargingPileListVO.setChargingPiles(res);
        return userChargingPileListVO;
    }

    @RequestMapping("/chargingPile/info")
    public ChargingPileSpecificInfoVO getInfo(@RequestParam(value = "id") BigInteger id) {
        System.out.println("接收到请求");
        ChargingPile e = chargingPileService.getById(id);
        ChargingPileSpecificInfoVO chargingPileSpecificInfoVO = new ChargingPileSpecificInfoVO();
        chargingPileSpecificInfoVO.setId(e.getId());
        chargingPileSpecificInfoVO.setCol(e.getCol());
        chargingPileSpecificInfoVO.setRow(e.getRow());
        ParkingPlace p = null;
        try {
            p = parkingPlaceService.getByIdForAdministrator(e.getParkingPlace());
        } catch (Exception ex) {
            return new ChargingPileSpecificInfoVO();
        }
        chargingPileSpecificInfoVO.setParkingPlace(p.getLocation());
        if (e.getCanUsed() == 0) {//数据库里面默认是1
            chargingPileSpecificInfoVO.setCanUsed(false);
        } else {
            chargingPileSpecificInfoVO.setCanUsed(true);
        }
        chargingPileSpecificInfoVO.setPrice(e.getPrice());
        chargingPileSpecificInfoVO.setPower(e.getPower());

        String[] s = e.getPhotoUrlList().split("$");
        List<String> list = new ArrayList<>(s.length);
        Collections.addAll(list, s);

        chargingPileSpecificInfoVO.setPhotoUrlList(list);
        chargingPileSpecificInfoVO.setSerialNumber(e.getSerialNumber());
        chargingPileSpecificInfoVO.setChargingType(e.getChargingType());
        //把int的时间戳转换为用户能看懂的字符串
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date(e.getBeginUsedTime() * 1000);
        chargingPileSpecificInfoVO.setBeginUsedTime(simpleDateFormat.format(d));
        d.setTime(e.getCanUsedTime() * 1000);//canUsedTime 是int类型
        chargingPileSpecificInfoVO.setCanUsedTime(simpleDateFormat.format(d));
        return chargingPileSpecificInfoVO;
        //类名和对象名一样，会让人分不清这是成员方法还是静态方法
    }
}//标记java包为java代码所在地 然后重新导入pom文件就可以了
//分别对应与两个问题： 自己编写的类无法识别，依赖的类无法识别
