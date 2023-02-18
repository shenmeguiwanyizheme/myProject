package com.example.service;

import com.example.mapper.chargingpile.ChargingPileMapper;
import com.example.pojo.ChargingPile;
import com.example.pojo.ParkingPlace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
//@Scope(scopeName = "singleton") 默认
public class ChargingPileService {

    //默认按类型注入 @resource按名字注入，但是怎么指定bean的名字呢
    @Resource
    private ChargingPileMapper mapper;
    @Resource
    ParkingPlaceService parkingPlaceService;

//    static {
//
//    }//单例的话意味着成员变量的初始值和静态块页只会有一次执行的机会

    //ChargingPileService chargingPileService = (ChargingPileService) AopContext.currentProxy();

    //不是bean，如何注入？
    public List<ChargingPile> getChargingPileListForUser(String serialNumber, Integer pageSize, Integer page, String location) {

        int offset = (page - 1) * pageSize;
        List<ParkingPlace> parkingPlaceList = parkingPlaceService.getListForUser(1, Integer.MAX_VALUE, location);
        if (parkingPlaceList.size() == 0) {
            log.error("条件查询数据为0");
            return new ArrayList<>();
        }
        String idList = parseParkingPlaceIdListToString(parkingPlaceList);
        //这个不是结束的记录位置，这个是要查询的记录数，比前者多1;
        return mapper.getChargingPileListForUser(serialNumber, offset, pageSize, idList);
    }

    public List<ChargingPile> getChargingPileListForAdministrator(String serialNumber, Integer pageSize, Integer page, String place) {

        int offset = (page - 1) * pageSize;
        List<ParkingPlace> parkingPlaceList = parkingPlaceService.getListForAdministrator(1, Integer.MAX_VALUE, place);
        if (parkingPlaceList.size() == 0) {
            log.error("条件查询数据为0");
            return new ArrayList<>();
        }
        String idList = parseParkingPlaceIdListToString(parkingPlaceList);
        return mapper.getChargingPileListForAdministrator(serialNumber, offset, pageSize, idList);
    }

    public int getTotalCountForAdministrator(String serialNumber, String place) {
        List<ParkingPlace> parkingPlaceList = parkingPlaceService.getListForAdministrator(1, Integer.MAX_VALUE, place);
        if (parkingPlaceList.size() == 0) {
            log.error("条件查询数据为0");
            return 0;
        }
        String idList = parseParkingPlaceIdListToString(parkingPlaceList);
        return mapper.getTotalCountForAdministrator(serialNumber, idList);
    }

    public String parseParkingPlaceIdListToString(List<ParkingPlace> parkingPlaceList) {
        StringBuilder idListBuilder = new StringBuilder();
        for (int index = 0; index < parkingPlaceList.size(); index++) {
            if (index == 0) {
                idListBuilder.append(parkingPlaceList.get(0).getId());
            } else {
                idListBuilder.append("," + parkingPlaceList.get(index).getId());
            }
        }
        return idListBuilder.toString();
    }

    public ChargingPile getById(BigInteger id) {

        return mapper.getById(id);
        //做好异常控制，避免异常抛给main方法，导致服务宕机
    }

    public ChargingPile extractById(BigInteger id) {
        return mapper.extractById(id);
    }

    public BigInteger edit(BigInteger id, String serialNumber, String canUsedTime,
                           Integer row, Integer col, BigInteger parkingPlaceId, String beginUsedTime, Integer chargingType,
                           String photoUrlList, BigInteger price, Integer power, String tag) throws ParseException, RuntimeException {
        //主要是校验两个地方，如果传入的id不为空，校验id，如果parkingPlace不为空，校验parkingPlace
        if (parkingPlaceId != null) {
            ParkingPlace parkingPlace = parkingPlaceService.extractById(parkingPlaceId);
            if (parkingPlace == null) {
                log.error("no such parkingPlaceId");
                throw new RuntimeException("no such parkingPlaceId");
            }
        }
        ChargingPile entity = new ChargingPile();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //entity.setId(null);//表示采用默认值,写上这个方便理清思路
        entity.setSerialNumber(serialNumber);
        entity.setRow(row);
        entity.setCol(col);
        //entity.setCanUsed(null);
// entity.setCanUsedTime(null);     }
        //就是setParkingPlace这里报错
        entity.setParkingPlaceId(parkingPlaceId);
        if (beginUsedTime != null) {
            entity.setBeginUsedTime((int) (simpleDateFormat.parse(beginUsedTime).getTime() / 1000));//9
        } else {
            //entity.setBeginUsedTime(null);
        }
        entity.setChargingType(chargingType);
        entity.setPrice(price);
        entity.setPower(power);
        entity.setPhotoUrlList(photoUrlList);
        entity.setTag(tag);
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        entity.setUpdateTime(currentTime);//12
        entity.setIsDeleted(0);
        if (id == null) {
            entity.setCreateTime(currentTime);
            entity.setCanUsedTime(currentTime);
            mapper.insert(entity);
            return entity.getId();
        } else {//这是update
            Integer parseCanUsedTime = null;
            if (canUsedTime != null) {
                parseCanUsedTime = (int) (simpleDateFormat.parse(canUsedTime).getTime() / 1000);
            }
            entity.setCanUsedTime(parseCanUsedTime);
            if (parseCanUsedTime != null) {
                if (parseCanUsedTime > currentTime) {
                    entity.setCanUsed(1);// 10
                } else {
                    entity.setCanUsed(0);
                }
            }
            //else{entity.setCanUsedTime(null);}
            ChargingPile isExist = extractById(id);
            if (isExist == null) {
                log.error(" no such id to update");
                throw new RuntimeException(" no such id to update");
            } else {
                mapper.update(entity);
                return id;
            }
        }//这个地方因为合并了insert，所以要统一接口而使用BigInteger
    }

    public int delete(BigInteger id) {
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        return mapper.delete(id, currentTime);
    }

    public BigInteger addPhotoUrl(BigInteger id, String s) {
        ChargingPile entity = mapper.getById(id);
        if (entity != null) {
            String temp = entity.getPhotoUrlList();
            temp.concat("$").concat(s);//使用建造者模式
        }
//        entity.setPhotoUrlList(temp);      好像没必要.....
        entity.setUpdateTime((int) (System.currentTimeMillis() / 1000));
        mapper.update(entity);
        return id;
    }
}