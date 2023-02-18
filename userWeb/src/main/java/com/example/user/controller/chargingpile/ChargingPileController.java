package com.example.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.constSetting.DateConsts;
import com.example.pojo.ChargingPile;
import com.example.pojo.ParkingPlace;
import com.example.service.ChargingPileService;
import com.example.service.ParkingPlaceService;
import com.example.user.domain.ChargingPileBaseInfoVO;
import com.example.user.domain.ChargingPileListVO;
import com.example.user.domain.ChargingPileSpecificInfoVO;
import com.example.user.domain.ChargingPileWPVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

//这种业务是网上查
@RestController
@Slf4j
public class ChargingPileController {
    @Autowired
    private ChargingPileService chargingPileService;
    @Autowired
    private ParkingPlaceService parkingPlaceService;

    @RequestMapping("/charging_pile/list")
    public ChargingPileListVO chargingPileList(@RequestParam(name = "wp", required = false) String wp, @RequestParam(name = "serialNumber", required = false) String serialNumber,
                                               @RequestParam(name = "place", required = false) String parkingPlace) {

        log.info("接收到请求");
        int page = 0;
        if (wp != null && !wp.equals("")) {
            Base64.Decoder urlDecoder = Base64.getUrlDecoder();
            byte[] oldRequestChargingPileWPVOBytes = urlDecoder.decode(wp.getBytes(StandardCharsets.UTF_8));
            //decode，encode一个字节数组的话，可以指定自己的字符串采用的字符集..，但是直接decode String是不行的
            String oldRequestChargingPileWPVOJson = new String(oldRequestChargingPileWPVOBytes, StandardCharsets.UTF_8);
            //json->转java对象
            ChargingPileWPVO oldRequestChargingPileWPVO;

            try {
                oldRequestChargingPileWPVO = JSONObject.parseObject(oldRequestChargingPileWPVOJson, ChargingPileWPVO.class);
                log.info("解码后的参数" + oldRequestChargingPileWPVO);
            } catch (Exception exception) {
                log.error("java对象创建失败" + exception.getMessage());
                return new ChargingPileListVO();
            }
            page = oldRequestChargingPileWPVO.getPage();
            parkingPlace = oldRequestChargingPileWPVO.getPlace();
            serialNumber = oldRequestChargingPileWPVO.getSerialNumber();
        }
        if (page == 0) {
            page = 1;
        }
        if (serialNumber != null) {
            serialNumber = serialNumber.trim();
        }
        if (parkingPlace != null) {
            parkingPlace = parkingPlace.trim();
        }
        int pageSize = 5;
        List<ChargingPile> temp = chargingPileService.getChargingPileListForUser(serialNumber, pageSize, page, parkingPlace);
        log.info("database data list" + temp);
        ChargingPileListVO userChargingPileListVO = new ChargingPileListVO();
        if (temp.size() == pageSize) {
            userChargingPileListVO.setIsEnd(false);
        } else
            userChargingPileListVO.setIsEnd(true);
        List<ChargingPileBaseInfoVO> res = new ArrayList<>();
        for (ChargingPile e : temp) {
            ChargingPileBaseInfoVO chargingPileBaseInfoVO = new ChargingPileBaseInfoVO();
            chargingPileBaseInfoVO.setId(e.getId());
            chargingPileBaseInfoVO.setRow(e.getRow());
            chargingPileBaseInfoVO.setCol(e.getCol());
            chargingPileBaseInfoVO.setParkingPlace(parkingPlaceService.getById(e.getParkingPlaceId()).getLocation());//实际上没有异常抛出
            String[] s = e.getPhotoUrlList().split("$");
            chargingPileBaseInfoVO.setImage(s[0]);
            res.add(chargingPileBaseInfoVO);
        }
        ChargingPileWPVO newRequestChargingPileWPVO = new ChargingPileWPVO().setPage(page + 1).setSerialNumber(serialNumber).setPlace(parkingPlace);
        String newRequestChargingPileWPVOJson;
        try {
            newRequestChargingPileWPVOJson = JSONObject.toJSONString(newRequestChargingPileWPVO);
        } catch (Exception exception) {
            log.error("java对象转成json失败");
            return new ChargingPileListVO();
        }
        Base64.Encoder urlEncoder = Base64.getUrlEncoder();
        byte[] oldRequestChargingPileWPVOBytes = urlEncoder.encode(newRequestChargingPileWPVOJson.getBytes(StandardCharsets.UTF_8));
        //这一步是在进行编码转换，从普通的字节数组转换成base64编码特征的字节数组
        String newWP = new String(oldRequestChargingPileWPVOBytes, StandardCharsets.UTF_8);
        userChargingPileListVO.setChargingPiles(res).setWp(newWP);
        return userChargingPileListVO;
    }

    @RequestMapping("/charging_pile/info")
    public ChargingPileSpecificInfoVO chargingPileInfo(@RequestParam(value = "id") BigInteger id) {

        ChargingPile chargingPile = chargingPileService.getById(id);
        if (chargingPile == null) {
            log.error("no such id");
            return new ChargingPileSpecificInfoVO();
        }
        ParkingPlace parkingPlace = parkingPlaceService.getById(chargingPile.getParkingPlaceId());
        if (parkingPlace == null) {
            return new ChargingPileSpecificInfoVO();
        }
        ChargingPileSpecificInfoVO chargingPileSpecificInfoVO = new ChargingPileSpecificInfoVO();
        chargingPileSpecificInfoVO.setId(chargingPile.getId());
        chargingPileSpecificInfoVO.setCol(chargingPile.getCol());
        chargingPileSpecificInfoVO.setRow(chargingPile.getRow());
        chargingPileSpecificInfoVO.setParkingPlace(parkingPlace.getLocation());
        if (chargingPile.getCanUsed() == 0) {//数据库里面默认是1
            chargingPileSpecificInfoVO.setCanUsed(false);
        } else {
            chargingPileSpecificInfoVO.setCanUsed(true);
        }
        chargingPileSpecificInfoVO.setPrice(chargingPile.getPrice());
        chargingPileSpecificInfoVO.setPower(chargingPile.getPower());
        String[] s = chargingPile.getPhotoUrlList().split("$");
        List<String> photoUrlList = new ArrayList<>();
        if (s.length != 0) {
            photoUrlList.addAll(Arrays.asList(s));
        }
        chargingPileSpecificInfoVO.setPhotoUrlList(photoUrlList);
        chargingPileSpecificInfoVO.setSerialNumber(chargingPile.getSerialNumber());
        chargingPileSpecificInfoVO.setChargingType(chargingPile.getChargingType());
        //把int的时间戳转换为用户能看懂的字符串
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateConsts.PATTERN);
        Date beginUsedDate = new Date(chargingPile.getBeginUsedTime() * 1000L);
        chargingPileSpecificInfoVO.setBeginUsedTime(simpleDateFormat.format(beginUsedDate));
        beginUsedDate.setTime(chargingPile.getCanUsedTime() * 1000L);//canUsedTime 是int类型
        chargingPileSpecificInfoVO.setCanUsedTime(simpleDateFormat.format(beginUsedDate));
        return chargingPileSpecificInfoVO;
        //类名和对象名一样，会让人分不清这是成员方法还是静态方法
    }
}//标记java包为java代码所在地 然后重新导入pom文件就可以了
//分别对应与两个问题： 自己编写的类无法识别，依赖的类无法识别
