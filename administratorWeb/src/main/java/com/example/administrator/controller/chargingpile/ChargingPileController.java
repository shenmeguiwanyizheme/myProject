package com.example.administrator.controller.chargingpile;

import com.alibaba.fastjson.JSONObject;
import com.example.administrator.annotations.VerifierUser;
import com.example.administrator.domain.action.DeleteResultVO;
import com.example.administrator.domain.action.InsertOrUpdateResultVO;
import com.example.administrator.domain.chargingpile.ChargingPileBaseInfoListVO;
import com.example.administrator.domain.chargingpile.ChargingPileBaseInfoVO;
import com.example.administrator.domain.chargingpile.ChargingPileSpecificInfoVO;
import com.example.constSetting.DateConsts;
import com.example.pojo.chargingpile.ChargingPile;
import com.example.pojo.parkingplace.ParkingPlace;
import com.example.pojo.user.User;
import com.example.service.chargingpile.ChargingPileService;
import com.example.service.parkingplace.ParkingPlaceService;
import com.example.utils.BaseUtils;
import com.example.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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


    @RequestMapping("/charging_pile/list")
    public Response chargingPileList(
            @VerifierUser User loginUser,
            @RequestParam(name = "serialNumber", required = false) String serialNumber,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "place", required = false) String place) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }

        log.info("接收到请求");
        if (serialNumber != null) {
            serialNumber = serialNumber.trim();
        }
        if (place != null) {
            place = place.trim();
        }
        int pageSize = 5;
        List<ChargingPile> chargingPileList = chargingPileService.getChargingPileListForAdministrator(serialNumber, pageSize, page, place);//功能在前对象在后
        //  System.out.println(list1);
        if (chargingPileList.size() == 0) {
            ChargingPileBaseInfoListVO resultVO = new ChargingPileBaseInfoListVO().setTotal(chargingPileService.getTotalCountForAdministrator(serialNumber, place)).setPageSize(5);
            String resultJson = JSONObject.toJSONString(resultVO);
            //异常处理规
            return new Response().setCode(4010).setResult(resultVO);

        }
        //mapper层已经校验过了，所有数据都有效
        List<ChargingPileBaseInfoVO> chargingPileBaseInfoVOList = new ArrayList<>();
        log.info("controller" + chargingPileList);
        StringBuilder idListBuilder = new StringBuilder();
        for (int index = 0; index < chargingPileList.size(); index++) {
            if (index == 0) {
                idListBuilder.append(chargingPileList.get(0).getParkingPlaceId());
            } else {
                idListBuilder.append("," + chargingPileList.get(index).getParkingPlaceId());
            }
        }
        List<ParkingPlace> parkingPlaceList = parkingPlaceService.getListForAdministratorByIdList(page, pageSize, idListBuilder.toString());
        for (int index = 0; index < chargingPileList.size(); index++) {
            ChargingPile chargingPile = chargingPileList.get(index);
            ParkingPlace parkingPlace = parkingPlaceList.get(index);
            if (parkingPlace == null) {
                log.warn("有数据已经过期：" + chargingPile);
                continue;
            }
            //这个地方怎么优化呢，如果place不为空且不是空串，那么肯定能查出来东西，就不需要校验location，不然则需要校验，校验成功继续执行代码，校验失败进入下一行
            ChargingPileBaseInfoVO chargingPileBaseInfoVO = new ChargingPileBaseInfoVO();
            chargingPileBaseInfoVO.setId(chargingPile.getId());
            String image = chargingPile.getPhotoUrlList().split("$")[0];
            chargingPileBaseInfoVO.setImage(image);
            chargingPileBaseInfoVO.setRow(chargingPile.getRow());
            chargingPileBaseInfoVO.setCol(chargingPile.getCol());
            chargingPileBaseInfoVO.setParkingPlace(parkingPlace.getLocation());
            //log.info("有异常，但实际上不会抛出");
            chargingPileBaseInfoVO.setIsDeleted(chargingPile.getIsDeleted() == 0 ? false : true);
            chargingPileBaseInfoVOList.add(chargingPileBaseInfoVO);
        }
        ChargingPileBaseInfoListVO resultVO = new ChargingPileBaseInfoListVO();
        log.info("controller" + chargingPileBaseInfoVOList);
        resultVO.setChargingPiles(chargingPileBaseInfoVOList);
        resultVO.setPageSize(5);
        resultVO.setTotal(chargingPileService.getTotalCountForAdministrator(serialNumber, place));
        return new Response().setCode(200).setResult(resultVO);
    }

    @RequestMapping("/charging_pile/info")
    public Response chargingPileInfo(
            @VerifierUser User loginUser,
            @RequestParam(name = "id") BigInteger id) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }

        // return null;
        ChargingPile entity = chargingPileService.extractById(id);
        if (entity == null) {
            //对象空的json显示是一片空白，这里返回的是对象非空属性空，也可以返回对象空
            String resultJson = JSONObject.toJSONString(new ChargingPileSpecificInfoVO());
            return new Response().setCode(4010);
        }
        ParkingPlace place = parkingPlaceService.extractById(entity.getParkingPlaceId());
        if (place == null) {
            String resultJson = JSONObject.toJSONString(new ChargingPileSpecificInfoVO());
            return new Response().setCode(4010);
        }
        ChargingPileSpecificInfoVO resultVO = new ChargingPileSpecificInfoVO();
        resultVO.setId(id);
        resultVO.setSerialNumber(entity.getSerialNumber());
        resultVO.setCanUsed(entity.getCanUsed() == 0 ? false : true);
        SimpleDateFormat format = new SimpleDateFormat(DateConsts.PATTERN);
        resultVO.setCanUsedTime(format.format(new Date(entity.getCanUsedTime() * 1000L)));
        resultVO.setRow(entity.getRow());
        resultVO.setCol(entity.getCol());
        //仍然做校验，因为马上就不允许用子查询
        // resultVO.setParkingPlace(entity.getParkingPlace());
        resultVO.setBeginUsedTime(format.format(new Date(entity.getBeginUsedTime() * (long) 1000)));
        resultVO.setChargingType(entity.getChargingType());
        resultVO.setPrice(entity.getPrice());
        resultVO.setPower(entity.getPower());
        String[] s = entity.getPhotoUrlList().split("$");
        List<String> photoUrlList = new ArrayList<>();
        if (s.length != 0) {
            photoUrlList.addAll(Arrays.asList(s));
        }
        resultVO.setPhotoUrlList(photoUrlList);
        //修复返回空串而不是null的bug
        resultVO.setCreateTime(format.format(new Date(entity.getCreateTime() * 1000L)));
        resultVO.setUpdateTime(format.format(new Date(entity.getUpdateTime() * 1000L)));
        resultVO.setIsDeleted(entity.getIsDeleted() == 0 ? false : true);
        return new Response().setCode(200).setResult(resultVO);
    }

    @RequestMapping("/charging_pile/insert")
    public Response chargingPileInsert(
            @VerifierUser User loginUser, @RequestParam(name = "serialNumber") String serialNumber,
            @RequestParam(name = "row") Integer row, @RequestParam(name = "col") Integer col, @RequestParam(name = "parkingPlaceId") BigInteger parkingPlaceId, @RequestParam(name = "beginUsedTime") String beginUsedTime, @RequestParam(name = "chargingType") Integer chargingType,
            @RequestParam(name = "photoUrlList") String photoUrlList, @RequestParam(name = "price") BigInteger price, @RequestParam(name = "power") Integer power, @RequestParam(name = "tag") String tag) {

        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        log.info("已经接收到insert请求");
        //要是没有查到，会直接返回null
        ParkingPlace parkingPlace = parkingPlaceService.extractById(parkingPlaceId);
        if (parkingPlace == null) {
            log.error("failed to insert, can not find parkingPlace corresponding to parkingPlaceId parameter");
            InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO()
                    .setErrorMessage("failed to insert ,can not find parkingPlace corresponding to parkingPlaceId parameter")
                    .setIsSuccessed(false);
            return new Response().setCode(4010).setResult(resultVO);
        }
        BigInteger resultId;
        try {
            resultId = chargingPileService.edit(null, serialNumber, null, row, col, parkingPlaceId, beginUsedTime, chargingType, photoUrlList, price, power, tag);
        } catch (Exception exception) {
            log.error(exception.toString());
            InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO().setErrorMessage("insert failed" + exception.getMessage()).setIsSuccessed(false);
            return new Response().setCode(4010).setResult(resultVO);
        }
        InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO().setId(resultId);
        return new Response().setCode(200).setResult(resultVO);

    }

    @RequestMapping("/charging_pile/update")
    public Response chargingPileUpdate(
            @VerifierUser User loginUser, @RequestParam(name = "id") BigInteger id, @RequestParam(name = "serialNumber", required = false) String serialNumber,
            @RequestParam(name = "canUsedTime", required = false) String canUsedTime,
            @RequestParam(name = "row", required = false) Integer row, @RequestParam(name = "col", required = false) Integer col, @RequestParam(name = "parkingPlaceId", required = false) BigInteger parkingPlaceId, @RequestParam(name = "beginUsedTime", required = false) String beginUsedTime, @RequestParam(name = "chargingType", required = false) Integer chargingType,
            @RequestParam(name = "price", required = false) BigInteger price, @RequestParam(name = "power", required = false) Integer power, @RequestParam(name = "photoUrlList", required = false) String photoUrlList, @RequestParam(name = "tag", required = false) String tag) {
        log.info("已经接收到update请求");
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        //要是没有查到，会直接返回null
        ChargingPile chargingPile = chargingPileService.extractById(id);
        if (chargingPile == null) {
            String errorMessage = "fail to update,no such id ,";
            log.error(errorMessage);
            InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO().setErrorMessage(errorMessage).setIsSuccessed(false);
            return new Response().setCode(4010).setResult(resultVO);

        }
        if (parkingPlaceId != null) {
            ParkingPlace parkingPlace = parkingPlaceService.extractById(parkingPlaceId);
            if (parkingPlace == null) {
                log.error("failed to update, can not find parkingPlace corresponding to parkingPlaceId parameter");
                InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO()
                        .setErrorMessage("failed to update ,can not find parkingPlace corresponding to parkingPlaceId parameter")
                        .setIsSuccessed(false);
                String resultJson = JSONObject.toJSONString(resultVO);
                return new Response().setCode(4010).setResult(resultVO);
            }
        }

        try {
            chargingPileService.edit(id, serialNumber, canUsedTime, row, col, parkingPlaceId, beginUsedTime, chargingType, photoUrlList, price, power, tag);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO().setErrorMessage("update failed" + exception.getMessage()).setIsSuccessed(false);
            return new Response().setCode(4010).setResult(resultVO);

        }
        InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO().setId(id).setIsSuccessed(true);
        return new Response().setCode(200).setResult(resultVO);
    }

    @RequestMapping("/charging_pile/delete")
    public Response chargingPileDelete(@VerifierUser User loginUser, @RequestParam(name = "id") BigInteger id) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        int affectedRows = 0;
        try {
            affectedRows = chargingPileService.delete(id);//&useAffectedRows=true
        } catch (Exception exception) {
            log.error("delete failed " + exception.getMessage());
            DeleteResultVO resultVO = new DeleteResultVO().setIsSuccessed(false).setErrorMessage("delete failed " + exception.getMessage());
            return new Response().setCode(4010).setResult(resultVO);
        }
        if (affectedRows == 0) {
            log.error("failed to delete, can not find charging_pile corresponding to id ");
            DeleteResultVO resultVO = new DeleteResultVO()
                    .setErrorMessage("failed to delete, can not find charging_pile corresponding to id ")
                    .setIsSuccessed(false);
            String resultJson = JSONObject.toJSONString(resultVO);
            return new Response().setCode(4010).setResult(resultVO);
        }
        DeleteResultVO resultVO = new DeleteResultVO().setIsSuccessed(true);
        return new Response().setCode(200).setResult(resultVO);
    }

    @RequestMapping("/charging_pile/addPhotoUrl")
    public Response chargingPileAddPhotoUrl(@VerifierUser User loginUser, @RequestParam(name = "id") BigInteger id, @RequestParam(name = "url") String url) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        chargingPileService.addPhotoUrl(id, url);
        InsertOrUpdateResultVO resultVO = new InsertOrUpdateResultVO().setId(id).setIsSuccessed(true);
        return new Response(200, resultVO);
    }
}
