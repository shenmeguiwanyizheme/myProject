package com.example.service;

import com.example.mapper.ParkingPlaceMapper;
import com.example.pojo.ParkingPlace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
@Slf4j
public class ParkingPlaceService {
    @Resource
    private ParkingPlaceMapper mapper;


    public BigInteger insert(String location, int cTime, int uTime) {
        ParkingPlace entity = new ParkingPlace();
        entity.setCreateTime(cTime).setUpdateTime(uTime).setLocation(location);
        mapper.insert(entity);
        return entity.getId();
    }

    public int update(BigInteger id, String location, int updateTime) throws Exception {
        if (id == null) throw new RuntimeException("要更新的充电桩位置id不存在");
        ParkingPlace entity = new ParkingPlace();
        entity.setLocation(location).setId(id).setUpdateTime(updateTime);
        int res = mapper.update(entity);
        if (res == 0) {
            throw new RuntimeException("要更新的充电桩位置id不存在");
        }
        return res;
    }

    public int delete(BigInteger id, int updateTime) throws Exception {
        if (id == null) {
            throw new RuntimeException("要删除的充电桩id不存在");
        }
        ParkingPlace p = new ParkingPlace();
        p.setIsDeleted(1).setUpdateTime(updateTime);
        int res = mapper.update(p);
        if (res == 0) {
            throw new RuntimeException("要删除的充电桩id不存在");
        }
        return res;
    }

    public ParkingPlace getByIdForUser(BigInteger id) throws Exception {
        if (id == null) throw new RuntimeException("要查询的充电桩位置id不存在");
        ParkingPlace res = mapper.getById(id);
        if (res == null) {
            throw new RuntimeException("充电桩所在位置id不存在");
        }
        return res;
    }

    public ParkingPlace getByIdForAdministrator(BigInteger id) throws Exception {
        if (id == null) {
            log.error("service: id为空");
            throw new RuntimeException("要查询的充电桩位置id不存在");
        }

        ParkingPlace res = mapper.extractById(id);
        System.out.println("service 66 row  res:" + res);
        if (res == null) {
            log.error("service 查询结果数为0");
            throw new RuntimeException("充电桩所在位置id不存在");
        }
        return res;
    }
    //interceptor 拦截器

    public List<ParkingPlace> getListForAdministrator(Integer page, Integer pageSize) {
        int from = (page - 1) * pageSize;
        return mapper.getListForAdministrator(from, pageSize);
    }

    public BigInteger getTotalCountForAdministrator(Integer page, Integer pageSize) {

        int from = (page - 1) * pageSize;
        return mapper.getTotalCountForAdministrator(from, pageSize);

    }

    public List<ParkingPlace> getListForUser(Integer page, Integer pageSize) {
        int from = (page - 1) * pageSize;
        return mapper.getListForUser(from, pageSize);
    }

//    public void forTest() {
//        ParkingPlace p;
//        do {
//              p=mapper.selectById(1).getVersion(); 模拟使用自带的乐观锁更新数据
//                   p.setLocation("new Location");
//        } while (mapper.updateById(p)==0);要对比的版本就存在这个查出来的对象中，所以修改对象必须是查出来的对象
//
//    }
}












