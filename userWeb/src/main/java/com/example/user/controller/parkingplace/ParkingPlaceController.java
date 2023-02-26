package com.example.user.controller.parkingplace;

import com.example.pojo.parkingplace.ParkingPlace;
import com.example.service.parkingplace.ParkingPlaceService;
import com.example.user.domain.parkingplace.ParkingPlaceBaseInfoListVO;
import com.example.user.domain.parkingplace.ParkingPlaceSpecificInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ParkingPlaceController {
    @Autowired
    private ParkingPlaceService parkingPlaceService;

    @RequestMapping("/parking_place/list")
    public ParkingPlaceBaseInfoListVO parkingPlaceList(@RequestParam("token") String token, @RequestParam("page") Integer page
            , HttpServletRequest request, HttpServletResponse response) {
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
        try {
            request.getRequestDispatcher("/parking_place/list").forward(request, response);
        } catch (ServletException e) {
        } catch (IOException e) {
        }
        return resultVO;
    }
}
