package com.example.administrator.controller.parkigplace;

import com.example.administrator.annotations.VerifierUser;
import com.example.administrator.domain.action.DeleteResultVO;
import com.example.administrator.domain.action.InsertOrUpdateResultVO;
import com.example.administrator.domain.parkingplace.ParkingPlaceBaseInfoListVO;
import com.example.administrator.domain.parkingplace.ParkingPlaceSpecificInfoVO;
import com.example.pojo.parkingplace.ParkingPlace;
import com.example.pojo.user.User;
import com.example.service.parkingplace.ParkingPlaceService;
import com.example.utils.BaseUtils;
import com.example.utils.Response;
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
    public Response parkingPlaceInsert(@VerifierUser User loginUser, @RequestParam("location") String location) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        BigInteger id = parkingPlaceService.insert(location);
        InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO().setId(id).setIsSuccessed(true);
        return new Response(200, resultVO);
    }

    @RequestMapping("/parking_place/update")
    public Response parkingPlaceUpdate(@VerifierUser User loginUser, @RequestParam("id") BigInteger id, @RequestParam("location") String location) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        try {

            ParkingPlace parkingPlace = parkingPlaceService.extractById(id);
            if (parkingPlace == null) {
                String errorMessage = "fail to update ,no such id";
                log.error(errorMessage);
                InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO()
                        .setIsSuccessed(false)
                        .setErrorMessage(errorMessage);
                return new Response().setCode(4010).setResult(resultVO);
            }
            parkingPlaceService.update(id, location);
            InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO()
                    .setId(id)
                    .setIsSuccessed(true);
            return new Response().setCode(200).setResult(resultVO);
        } catch (Exception exception) {
            InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO()
                    .setErrorMessage("update failed" + exception.getMessage())
                    .setIsSuccessed(false);
            return new Response().setCode(6999).setResult(resultVO);
        }
        //异常处理还没做
    }
    

    @RequestMapping("/parking_place/delete")
    public Response parkingPlaceDelete(@VerifierUser User loginUser, @RequestParam("id") BigInteger id) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        int affectedRows = parkingPlaceService.delete(id);
        if (affectedRows == 0) {
            DeleteResultVO resultVO = new DeleteResultVO().setErrorMessage("delete failed").setIsSuccessed(false);
            return new Response(4004, resultVO);
        }
        return new Response(200);
    }


    @RequestMapping("/parking_place/list")
    public Response parkingPlaceList(@VerifierUser User loginUser, @RequestParam("page") Integer page) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        final int pageSize = 5;
        List<ParkingPlace> parkingPlaceList = parkingPlaceService.getListForAdministrator(page, pageSize, null);
        List<ParkingPlaceSpecificInfoVO> resultVO = new ArrayList<>();
        if (parkingPlaceList.size() == 0) {
            return new Response(4010);
        }
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
        ParkingPlaceBaseInfoListVO parkingPlaceBaseInfoListVO = new ParkingPlaceBaseInfoListVO().setParkingPlaces(resultVO).setTotal(total).setPageSize(5);
        return new Response(200, parkingPlaceBaseInfoListVO);

    }
}
//难点
