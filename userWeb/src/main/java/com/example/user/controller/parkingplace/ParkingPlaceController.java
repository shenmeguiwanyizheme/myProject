package com.example.user.controller;

import com.example.pojo.ParkingPlace;
import com.example.service.ParkingPlaceService;
import com.example.user.domain.ParkingPlaceBaseInfoListVO;
import com.example.user.domain.ParkingPlaceSpecificInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ParkingPlaceController {
    @Autowired
    private ParkingPlaceService parkingPlaceService;

    @RequestMapping("/parking_place/list")
    public ParkingPlaceBaseInfoListVO parkingPlaceList(@RequestParam("page") Integer page) {
        ParkingPlaceBaseInfoListVO resultVO = new ParkingPlaceBaseInfoListVO();
        List<ParkingPlace> parkingPlaceList = parkingPlaceService.getListForUser(page, 5, null);
        if (parkingPlaceList.size() == 0) {
            return resultVO.setChargingPiles(null).setIsEnd(true);
        } else {
            List<ParkingPlaceSpecificInfoVO> list = new ArrayList<>();
            resultVO.setIsEnd(list.size() == 5 ? false : true);
            for (ParkingPlace parkingPlace : parkingPlaceList) {
                ParkingPlaceSpecificInfoVO ofVo = new ParkingPlaceSpecificInfoVO();
                ofVo.setId(parkingPlace.getId());
                ofVo.setLocation(parkingPlace.getLocation());
                list.add(ofVo);
            }
        }
        return resultVO;
    }
}
