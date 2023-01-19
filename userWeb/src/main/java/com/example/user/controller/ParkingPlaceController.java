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

    @RequestMapping("/parkingPlace/list")
    public ParkingPlaceBaseInfoListVO getList(@RequestParam("page") Integer page) {
        ParkingPlaceBaseInfoListVO res = new ParkingPlaceBaseInfoListVO();
        List<ParkingPlace> temp = parkingPlaceService.getListForUser(page, 5);
        if (temp == null || temp.size() == 0) {
            return res.setChargingPiles(null).setIsEnd(true);
        } else {
            List<ParkingPlaceSpecificInfoVO> list = new ArrayList<>();
            for (ParkingPlace p : temp) {
                ParkingPlaceSpecificInfoVO vo = new ParkingPlaceSpecificInfoVO();
                vo.setId(p.getId());
                vo.setLocation(p.getLocation());
                list.add(vo);
            }
            res.setIsEnd(list.size() == 5 ? false : true);
        }

        return res;
    }
}
