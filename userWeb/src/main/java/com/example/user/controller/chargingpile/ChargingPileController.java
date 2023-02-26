package com.example.user.controller.chargingpile;

import com.alibaba.fastjson.JSONObject;
import com.example.constSetting.DateConsts;
import com.example.pojo.chargingpile.ChargingPile;
import com.example.pojo.parkingplace.ParkingPlace;
import com.example.service.chargingpile.ChargingPileService;
import com.example.service.parkingplace.ParkingPlaceService;
import com.example.user.domain.chargingpile.ChargingPileBaseInfoVO;
import com.example.user.domain.chargingpile.ChargingPileListVO;
import com.example.user.domain.chargingpile.ChargingPileSpecificInfoVO;
import com.example.utils.BaseUtils;
import com.example.utils.Response;
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
    private static final String SALT = "SALT";
    @Autowired
    private ChargingPileService chargingPileService;
    @Autowired
    private ParkingPlaceService parkingPlaceService;

    @RequestMapping("/charging_pile/list")
    public Response chargingPileList(@RequestParam(name = "wp", required = false) String wp, @RequestParam(name = "serialNumber", required = false) String serialNumber,
                                     @RequestParam(name = "place", required = false) String parkingPlace) {

        //wp需要先用base64转，再用json转
        log.info("接收到请求");
        int page = 0;
        if (!BaseUtils.isEmpty(wp)) {
            wp = wp.substring(0, wp.length() - 1 - SALT.length());
            Base64.Decoder urlDecoder = Base64.getUrlDecoder();
            byte[] oldRequestChargingPileWPVOBytes = urlDecoder.decode(wp.getBytes(StandardCharsets.UTF_8));
            //decode，encode一个字节数组的话，可以指定自己的字符串采用的字符集..，但是直接decode String是不行的
            String oldRequestChargingPileWPVOJson = new String(oldRequestChargingPileWPVOBytes, StandardCharsets.UTF_8);
            //json->转java对象
            com.example.user.domain.chargingpile.ChargingPileWPVO oldRequestChargingPileWPVO;
            try {
                oldRequestChargingPileWPVO = JSONObject.parseObject(oldRequestChargingPileWPVOJson, com.example.user.domain.chargingpile.ChargingPileWPVO.class);
                log.info("解码后的参数" + oldRequestChargingPileWPVO);
            } catch (Exception exception) {
                log.error("java对象创建失败" + exception.getMessage());
                return new Response().setCode(4011).setResult(new ChargingPileListVO());

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
        List<ChargingPile> chargingPileList = chargingPileService.getChargingPileListForUser(serialNumber, pageSize, page, parkingPlace);
        log.info("database data list" + chargingPileList);
        ChargingPileListVO resultVO = new ChargingPileListVO();
        if (chargingPileList.size() == pageSize) {
            resultVO.setIsEnd(false);
        } else
            resultVO.setIsEnd(true);
        List<ChargingPileBaseInfoVO> res = new ArrayList<>();
        StringBuilder idListBuilder = new StringBuilder();
        for (int index = 0; index < chargingPileList.size(); index++) {
            if (index == 0) {
                idListBuilder.append(chargingPileList.get(0).getParkingPlaceId());
            } else {
                idListBuilder.append("," + chargingPileList.get(index).getParkingPlaceId());
            }
        }
        List<ParkingPlace> parkingPlaceList = parkingPlaceService.getListForUserByIdList(page, pageSize, idListBuilder.toString());
        for (int index = 0; index < chargingPileList.size(); index++) {
            ChargingPileBaseInfoVO chargingPileBaseInfoVO = new ChargingPileBaseInfoVO();
            chargingPileBaseInfoVO.setId(chargingPileList.get(index).getId());
            chargingPileBaseInfoVO.setRow(chargingPileList.get(index).getRow());
            chargingPileBaseInfoVO.setCol(chargingPileList.get(index).getCol());
            chargingPileBaseInfoVO.setParkingPlace(parkingPlaceList.get(index).getLocation());//实际上没有异常抛出
            String[] s = chargingPileList.get(index).getPhotoUrlList().split("$");
            chargingPileBaseInfoVO.setImage(s[0]);
            res.add(chargingPileBaseInfoVO);
        }
        com.example.user.domain.chargingpile.ChargingPileWPVO newRequestChargingPileWPVO = new com.example.user.domain.chargingpile.ChargingPileWPVO().setPage(page + 1).setSerialNumber(serialNumber).setPlace(parkingPlace);
        String newRequestChargingPileWPVOJson;
        try {
            newRequestChargingPileWPVOJson = JSONObject.toJSONString(newRequestChargingPileWPVO);
        } catch (Exception exception) {
            log.error("java对象转成json失败" + exception.getMessage());
            return new Response().setCode(4011).setResult(new ChargingPileListVO());

        }
        Base64.Encoder urlEncoder = Base64.getUrlEncoder();
        byte[] oldRequestChargingPileWPVOBytes = urlEncoder.encode(newRequestChargingPileWPVOJson.getBytes(StandardCharsets.UTF_8));
        //这一步是在进行编码转换，从普通的字节数组转换成base64编码特征的字节数组
        String newWP = new String(oldRequestChargingPileWPVOBytes, StandardCharsets.UTF_8) + SALT;
        resultVO.setChargingPiles(res).setWp(newWP.trim());
        return new Response().setCode(200).setResult(resultVO);
    }

    @RequestMapping("/charging_pile/info")
    public Response chargingPileInfo(@RequestParam("token") String token, @RequestParam(value = "id") BigInteger id) {
        log.info("success invoke");
        ChargingPile chargingPile = chargingPileService.getById(id);
        ChargingPileSpecificInfoVO resultVO = new ChargingPileSpecificInfoVO();
        if (chargingPile == null) {
            log.error("no such id");
            return new Response().setCode(4004).setResult(resultVO);
        }
        ParkingPlace parkingPlace = parkingPlaceService.getById(chargingPile.getParkingPlaceId());
        if (parkingPlace == null) {
            return new Response().setCode(4010).setResult(resultVO);
        }
        resultVO.setId(chargingPile.getId());
        resultVO.setCol(chargingPile.getCol());
        resultVO.setRow(chargingPile.getRow());
        resultVO.setParkingPlace(parkingPlace.getLocation());
        if (chargingPile.getCanUsed() == 0) {//数据库里面默认是1
            resultVO.setCanUsed(false);
        } else {
            resultVO.setCanUsed(true);
        }
        resultVO.setPrice(chargingPile.getPrice());
        resultVO.setPower(chargingPile.getPower());
        String[] s = chargingPile.getPhotoUrlList().split("$");
        List<String> photoUrlList = new ArrayList<>();
        if (s.length != 0) {
            photoUrlList.addAll(Arrays.asList(s));
        }
        resultVO.setPhotoUrlList(photoUrlList);
        resultVO.setSerialNumber(chargingPile.getSerialNumber());
        resultVO.setChargingType(chargingPile.getChargingType());
        //把int的时间戳转换为用户能看懂的字符串
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateConsts.PATTERN);
        Date beginUsedDate = new Date(chargingPile.getBeginUsedTime() * 1000L);
        resultVO.setBeginUsedTime(simpleDateFormat.format(beginUsedDate));
        beginUsedDate.setTime(chargingPile.getCanUsedTime() * 1000L);//canUsedTime 是int类型
        resultVO.setCanUsedTime(simpleDateFormat.format(beginUsedDate));
        return new Response().setCode(200).setResult(resultVO);
        //类名和对象名一样，会让人分不清这是成员方法还是静态方法
    }
}//标记java包为java代码所在地 然后重新导入pom文件就可以了
//分别对应与两个问题： 自己编写的类无法识别，依赖的类无法识别
