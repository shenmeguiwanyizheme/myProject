package com.example.administrator.controller;

import com.example.administrator.domain.DeleteResultVO;
import com.example.administrator.domain.InsertOrUpdateResultVO;
import com.example.administrator.domain.ParkingPlaceBaseInfoListVO;
import com.example.administrator.domain.ParkingPlaceSpecificInfoVO;
import com.example.pojo.ParkingPlace;
import com.example.service.ParkingPlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class ParkingPlaceController {
    @Autowired
    ParkingPlaceService parkingPlaceService;

    @RequestMapping("/parking_place/insert")
    public InsertOrUpdateResultVO parkingPlaceInsert(@RequestParam("location") String location) {
        BigInteger id = parkingPlaceService.insert(location);
        return new InsertOrUpdateResultVO().setId(id).setIsSuccessed(true);
    }

    @RequestMapping("/parking_place/update")
    public InsertOrUpdateResultVO parkingPlaceUpdate(@RequestParam("id") BigInteger id, @RequestParam("location") String location) {
        try {

            ParkingPlace parkingPlace = parkingPlaceService.extractById(id);
            if (parkingPlace == null) {
                String errorMessage = "fail to update ,no such id";
                log.error(errorMessage);
                return new InsertOrUpdateResultVO()
                        .setIsSuccessed(false)
                        .setErrorMessage(errorMessage);
            }
            parkingPlaceService.update(id, location);

            return new InsertOrUpdateResultVO()
                    .setId(id)
                    .setIsSuccessed(true);
        } catch (Exception e) {
            return new InsertOrUpdateResultVO()
                    .setErrorMessage("update failed" + e.getMessage())
                    .setIsSuccessed(false);
        }
        //异常处理还没做
    }

    @RequestMapping("/parking_place/delete")
    public DeleteResultVO parkingPlaceDelete(@RequestParam("id") BigInteger id) {
        int affectedRows = parkingPlaceService.delete(id);
        if (affectedRows == 0) {
            return new DeleteResultVO().setErrorMessage("delete failed").setIsSuccessed(false);
        }
        return new DeleteResultVO().setIsSuccessed(true);
    }


    @RequestMapping("/parking_place/list")
    public ParkingPlaceBaseInfoListVO parkingPlaceList(@RequestParam("page") Integer page) {
        final int pageSize = 5;
        List<ParkingPlace> parkingPlaceList = parkingPlaceService.getListForAdministrator(page, pageSize, null);
        List<ParkingPlaceSpecificInfoVO> resultVO = new ArrayList<>();
        if (parkingPlaceList.size() == 0) return new ParkingPlaceBaseInfoListVO().setParkingPlaces(resultVO);
        for (ParkingPlace parkingPlace : parkingPlaceList) {
            ParkingPlaceSpecificInfoVO sp = new ParkingPlaceSpecificInfoVO();
            sp.setId(parkingPlace.getId());
            sp.setLocation(parkingPlace.getLocation());
            sp.setUpdateTime(parkingPlace.getUpdateTime());
            sp.setCreateTime(parkingPlace.getCreateTime());
            sp.setIsDeleted(parkingPlace.getIsDeleted());
            resultVO.add(sp);
        }
        int total = parkingPlaceService.getTotalCountForAdministrator(null);
        return new ParkingPlaceBaseInfoListVO().setParkingPlaces(resultVO).setTotal(total).setPageSize(5);
    }
}

//难点
