package com.example.service;

import com.example.mapper.ChargingPileMapper;
import com.example.pojo.ChargingPile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ChargingPileService {

    //默认按类型注入 @resource按名字注入，但是怎么指定bean的名字呢
    @Resource
    private ChargingPileMapper mapper;


    //不是bean，如何注入？
    public List<ChargingPile> getChargingPileListForUser(String serialNumber, Integer page, Integer pId) {
        int pageSize = 5;
        int from = (page - 1) * pageSize;
        int offset = pageSize;//这个不是结束的记录位置，这个是要查询的记录数，比前者多1;
        return mapper.getChargingPileListForUser(serialNumber, from, offset, pId);
    }


    public List<ChargingPile> getChargingPileListForAdministrator(String serialNumber, Integer page, Integer parkingId) {
        int pageSize = 5;
        int from = (page - 1) * pageSize;
        int offset = pageSize;
        return mapper.getChargingPileListForAdministrator(serialNumber, from, offset, parkingId);
    }

    public BigInteger getTotalCountForAdministrator(String serialNumber, Integer pId) {
        return mapper.getTotalCountForAdministrator(serialNumber, pId);
    }

    public ChargingPile getById(BigInteger id) {

        return mapper.getById(id);
        //做好异常控制，避免异常抛给main方法，导致服务宕机
    }


    public ChargingPile extractById(BigInteger id) {
        return mapper.extractById(id);
    }

    public BigInteger insertOrUpdate(BigInteger id, String serialNumber, Integer canUsed, String canUsedTime,
                                     Integer row, Integer col, BigInteger parkingPlace, String beginUsedTime, Integer chargingType,
                                     String photoUrlList, BigInteger price, Integer power, String tag) throws Exception {
        //主要是校验两个地方，如果传入的id不为空，校验id，如果parkingPlace不为空，校验parkingPlace
        ChargingPile entity = new ChargingPile();
        int res = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        entity.setId(null);//表示采用默认值,写上这个方便理清思路
        entity.setSerialNumber(serialNumber);
        entity.setRow(row);
        entity.setCol(col);
        entity.setCanUsed(null);
        if (canUsedTime == null && id == null) {
            //就说明是插入
            entity.setCanUsedTime((int) (System.currentTimeMillis() / 1000));
        } else if (canUsedTime != null) {
            try {
                entity.setCanUsedTime((int) (simpleDateFormat.parse(canUsedTime).getTime() / 1000));// 10
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        } else {
            entity.setCanUsedTime(null);
        }
        //就是setParkingPlace这里报错
        //首先插入是要进行父表有没有的检验，如果没有那就会报错
        entity.setParkingPlace(parkingPlace);
        if (beginUsedTime != null) {
            try {
                entity.setBeginUsedTime((int) (simpleDateFormat.parse(beginUsedTime).getTime() / 1000));//9
            } catch (ParseException e) {
                log.error("日期无法转换成时间戳");
                throw new RuntimeException("日期无法转换成时间戳");
            }
        } else {
            entity.setBeginUsedTime(null);
        }
        entity.setChargingType(chargingType);
        entity.setPrice(price);
        entity.setPower(power);
        entity.setPhotoUrlList(photoUrlList);
        entity.setTag(tag);
        int time = (int) (System.currentTimeMillis() / 1000);
        if (id != null) {
            entity.setCreateTime(time);//11
        } else {
            entity.setCreateTime(null);//写上只是为了方便维护的人知道不同情况下属性的值
        }
        entity.setUpdateTime(time);//12
        entity.setIsDeleted(0);
        if (id == null) {
            res = mapper.insert(entity);
            if (res == 0) {
                //日志统一在controller层处理
                throw new RuntimeException("row，col位置重复");
            }
            return entity.getId();
        } else {
            res = mapper.update(entity);
            if (res == 0) throw new RuntimeException("更新的充电桩id不存在");
            return BigInteger.valueOf(res);
        }//这个地方因为合并了insert，所以要统一接口而使用BigInteger
    }

    public int delete(BigInteger id) throws Exception {
        int res;
        res = mapper.delete(id, (int) (System.currentTimeMillis() / 1000));
        if (res == 0) {
            throw new RuntimeException("要删除的充电桩id不存在");
        }
        return res;
    }

    public int addPhotoUrl(BigInteger id, String s) {
        ChargingPile entity = mapper.getById(id);
        String temp = entity.getPhotoUrlList();
        temp.concat("$").concat(s);//使用建造者模式
//        entity.setPhotoUrlList(temp);      好像没必要.....
        entity.setUpdateTime((int) (new Date().getTime() / 1000));
        return mapper.update(entity);
    }
}
