package com.example.administrator.controller;

import com.example.administrator.domain.IsSuccessedParkingPlaceVO;
import com.example.administrator.domain.ParkingPlaceBaseInfoListVO;
import com.example.administrator.domain.ParkingPlaceSpecificInfoVO;
import com.example.pojo.ParkingPlace;
import com.example.service.ParkingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ParkingPlaceController {
    @Autowired
    ParkingPlaceService parkingPlaceService;

    @RequestMapping("/parkingPlace/insert")
    public IsSuccessedParkingPlaceVO insert(@RequestParam("lc") String location) {
        int time = (int) (System.currentTimeMillis() / 1000);
        return new IsSuccessedParkingPlaceVO().setIsSuccessed(parkingPlaceService.insert(location, time, time).toString());
        //BigInteger的toString是可靠的
    }

    @RequestMapping("/parkingPlace/update")
    public IsSuccessedParkingPlaceVO update(@RequestParam("id") BigInteger id, @RequestParam("lc") String location) {
        try {
            int time = (int) (System.currentTimeMillis() / 1000);
            return new IsSuccessedParkingPlaceVO().setIsSuccessed((String.valueOf(parkingPlaceService.update(id, location, time))));
        } catch (Exception e) {
            return new IsSuccessedParkingPlaceVO().setIsSuccessed("更新失败" + e.toString());
        }
        //异常处理还没做
    }

    @RequestMapping("/parkingPlace/delete")
    public IsSuccessedParkingPlaceVO delete(@RequestParam("id") BigInteger id) {
        try {
            int time = (int) (System.currentTimeMillis() / 1000);
            return new IsSuccessedParkingPlaceVO().setIsSuccessed(String.valueOf(parkingPlaceService.delete(id, time)));
        } catch (Exception e) {
            return new IsSuccessedParkingPlaceVO().setIsSuccessed("删除失败" + e.toString());
        }
    }

    @RequestMapping("/parkingPlace/list")
    public ParkingPlaceBaseInfoListVO getBaseInfoList(@RequestParam("page") Integer page) {
        final int pageSize = 5;
        List<ParkingPlace> temp = parkingPlaceService.getListForAdministrator(page, 5);
        if (temp.size() == 0) return new ParkingPlaceBaseInfoListVO().setParkingPlaces(null);
        List<ParkingPlaceSpecificInfoVO> res = new ArrayList<>();
        for (ParkingPlace p : temp) {
            ParkingPlaceSpecificInfoVO sp = new ParkingPlaceSpecificInfoVO();
            sp.setId(p.getId());
            sp.setLocation(p.getLocation());
            sp.setUpdateTime(p.getUpdateTime());
            sp.setCreateTime(p.getCreateTime());
            sp.setIsDeleted(p.getIsDeleted());
            res.add(sp);
        }
        BigInteger total = parkingPlaceService.getTotalCountForAdministrator(page, 5);
        return new ParkingPlaceBaseInfoListVO().setParkingPlaces(res).setTotal(total).setPageSize(5);
    }
}

//难点
