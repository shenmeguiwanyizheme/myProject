package com.example.administrator.controller;

import com.example.administrator.domain.ChargingPileBaseInfoListVO;
import com.example.administrator.domain.ChargingPileBaseInfoVO;
import com.example.administrator.domain.ChargingPileSpecificInfoVO;
import com.example.administrator.domain.IsSuccessedChargingPileVO;
import com.example.pojo.ChargingPile;
import com.example.pojo.ParkingPlace;
import com.example.service.ChargingPileService;
import com.example.service.ParkingPlaceService;
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

//web负责做好参数传递和资格校验，dao负责和数据库对接,service负责写好业务
@RestController
@Slf4j
public class ChargingPileController {

    @Autowired
    private ChargingPileService chargingPileService;//此处属于误报
    @Autowired
    ParkingPlaceService parkingPlaceService;


    @RequestMapping("/chargingPile/list")
    public ChargingPileBaseInfoListVO queryAllDividedByPage(@RequestParam(name = "serialNumber", required = false) String serialNumber, @RequestParam(name = "page") Integer page, @RequestParam(name = "parkingPlace", required = false) Integer pId) {
        log.info("接收到请求");
        List<ChargingPile> list1 = chargingPileService.getChargingPileListForAdministrator(serialNumber, page, pId);//功能在前对象在后
        System.out.println(list1);
        if (list1.size() == 0) {
            //异常处理规范

            return new ChargingPileBaseInfoListVO().setTotal(chargingPileService.getTotalCountForAdministrator(serialNumber, pId)).setPageSize(5);
        }
        List<ChargingPileBaseInfoVO> list2 = new ArrayList<>();
        log.info("controller" + list1.toString());
        for (ChargingPile entity : list1) {
            ParkingPlace p = null;
            try {

                p = parkingPlaceService.getByIdForAdministrator(entity.getParkingPlace());
                System.out.println("controller 50 row  p:" + p);
            } catch (Exception ex) {
                log.error("controller 52行" + ex.getMessage());
                continue;
            }
            ChargingPileBaseInfoVO chargingPileBaseInfoVO = new ChargingPileBaseInfoVO();
            chargingPileBaseInfoVO.setId(entity.getId());
            chargingPileBaseInfoVO.setImage(entity.getPhotoUrlList().split("$")[0]);
            chargingPileBaseInfoVO.setRow(entity.getRow());
            chargingPileBaseInfoVO.setCol(entity.getCol());
            try {
                chargingPileBaseInfoVO.setParkingPlace(parkingPlaceService.getByIdForAdministrator(entity.getParkingPlace()).getLocation());
            } catch (Exception e) {
                log.error("controller 63行" + e.getMessage());
                continue;
            }
            chargingPileBaseInfoVO.setIsDeleted(entity.getIsDeleted() == 0 ? false : true);
            list2.add(chargingPileBaseInfoVO);
        }
        ChargingPileBaseInfoListVO res = new ChargingPileBaseInfoListVO();
        log.info("controller" + list2);
        res.setChargingPiles(list2);
        res.setPageSize(5);
        res.setTotal(chargingPileService.getTotalCountForAdministrator(serialNumber, pId));
        return res;
    }

    @RequestMapping("/chargingPile/info")
    public ChargingPileSpecificInfoVO queryById(@RequestParam(name = "id") BigInteger id) {
        // return null;
        ChargingPile entity = chargingPileService.extractById(id);
        if (entity == null) {
            //对象空的json显示是一片空白，这里返回的是对象非空属性空，也可以返回对象空
            return new ChargingPileSpecificInfoVO();
        }
        ChargingPileSpecificInfoVO res = new ChargingPileSpecificInfoVO();
        res.setId(id);
        res.setSerialNumber(entity.getSerialNumber());
        res.setCanUsed(entity.getCanUsed() == 0 ? false : true);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        res.setCanUsedTime(format.format(new Date(entity.getCanUsedTime() * (long) 1000)));
        res.setRow(entity.getRow());
        res.setCol(entity.getCol());
        try {
            ParkingPlace place = parkingPlaceService.getByIdForAdministrator(entity.getParkingPlace());
            res.setParkingPlace(place.getLocation());
        } catch (Exception e) {
            return new ChargingPileSpecificInfoVO();//也就是没有这个数据
        }
        // res.setParkingPlace(entity.getParkingPlace());
        res.setBeginUsedTime(format.format(new Date(entity.getBeginUsedTime() * (long) 1000)));
        res.setChargingType(entity.getChargingType());
        res.setPrice(entity.getPrice());
        res.setPower(entity.getPower());
        String[] s = entity.getPhotoUrlList().split("$");
        if (s != null && s.length != 0) {
            List<String> list = new ArrayList<>(s.length);
            Collections.addAll(list, s);
            res.setPhotoUrlList(list);
        } else {
            res.setPhotoUrlList(null);
        }//修复返回空串而不是null的bug
        res.setCreateTime(format.format(new Date(entity.getCreateTime() * (long) 1000)));
        res.setUpdateTime(format.format(new Date(entity.getUpdateTime() * (long) 1000)));
        res.setIsDeleted(entity.getIsDeleted() == 0 ? false : true);
        return res;
    }

    @RequestMapping("/chargingPile/insert")
    public IsSuccessedChargingPileVO insert(@RequestParam(name = "serialNumber") String serialNumber,
                                            @RequestParam(name = "row") Integer row, @RequestParam(name = "col") Integer col, @RequestParam(name = "parkingPlace") BigInteger parkingPlace, @RequestParam(name = "beginUsedTime") String beginUsedTime, @RequestParam(name = "chargingType") Integer chargingType,
                                            @RequestParam(name = "photoUrlList") String photoUrlList, @RequestParam(name = "price") BigInteger price, @RequestParam(name = "power") Integer power, @RequestParam(name = "tag") String tag) throws Exception {

        log.info("已经接收到insert请求");
        //要是没有查到，会直接返回null
        try {
            ParkingPlace temp = parkingPlaceService.getByIdForAdministrator(parkingPlace);
        } catch (Exception e) {
            return new IsSuccessedChargingPileVO().setIsSuccessed("增加失败" + e.toString());
        }

        BigInteger res;
        try {
            res = chargingPileService.insertOrUpdate(null, serialNumber, null, null, row, col, parkingPlace, beginUsedTime, chargingType, photoUrlList, price, power, tag);
        } catch (Exception e) {
            log.error(e.toString());
            return new IsSuccessedChargingPileVO().setIsSuccessed("增加失败" + e.toString());
        }
        return new IsSuccessedChargingPileVO().setIsSuccessed(res.toString());
    }


    @RequestMapping("/chargingPile/update")
    public IsSuccessedChargingPileVO update(@RequestParam(name = "id") BigInteger id, @RequestParam(name = "serialNumber", required = false) String serialNumber, @RequestParam(name = "canUsed", required = false) Integer canUsed,
                                            @RequestParam(name = "canUsedTime", required = false) String canUsedTime,
                                            @RequestParam(name = "row", required = false) Integer row, @RequestParam(name = "col", required = false) Integer col, @RequestParam(name = "parkingPlace", required = false) BigInteger parkingPlace, @RequestParam(name = "beginUsedTime", required = false) String beginUsedTime, @RequestParam(name = "chargingType", required = false) Integer chargingType,
                                            @RequestParam(name = "price", required = false) BigInteger price, @RequestParam(name = "power", required = false) Integer power, @RequestParam(name = "photoUrlList", required = false) String photoUrlList, @RequestParam(name = "tag", required = false) String tag) throws Exception {
        log.info("已经接收到update请求");
        //要是没有查到，会直接返回null
        try {
            ParkingPlace temp = parkingPlaceService.getByIdForAdministrator(parkingPlace);
        } catch (Exception e) {
            log.error(e.toString());
            return new IsSuccessedChargingPileVO().setIsSuccessed("更新失败" + e.toString());
        }
        BigInteger res;
        try {
            res = chargingPileService.insertOrUpdate(id, serialNumber, canUsed, canUsedTime, row, col, parkingPlace, beginUsedTime, chargingType, photoUrlList, price, power, tag);
        } catch (Exception e) {
            log.error(e.toString());
            return new IsSuccessedChargingPileVO().setIsSuccessed("更新失败" + e.toString());
        }
        return new IsSuccessedChargingPileVO().setIsSuccessed(res.toString());
    }

    @RequestMapping("/chargingPile/delete")
    public IsSuccessedChargingPileVO delete(@RequestParam(name = "id") BigInteger id) throws Exception {
        int res = 0;
        try {
            res = chargingPileService.delete(id);
        } catch (Exception e) {
            return new IsSuccessedChargingPileVO().setIsSuccessed("删除失败" + e.toString());
        }
        return new IsSuccessedChargingPileVO().setIsSuccessed("" + res);
    }


    @RequestMapping("/chargingPile/addPhotoUrl")
    public IsSuccessedChargingPileVO addPhotoUrl(@RequestParam(name = "id") BigInteger id, @RequestParam(name = "url") String url) throws Exception {

        int res = chargingPileService.addPhotoUrl(id, url);
        return new IsSuccessedChargingPileVO().setIsSuccessed("" + res);
    }
//    public int update() {
//
//
//    }
}
