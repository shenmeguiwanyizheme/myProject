package com.example.service.parkingplace;

import com.example.mapper.parkingplace.ParkingPlaceMapper;
import com.example.pojo.parkingplace.ParkingPlace;
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


    public BigInteger insert(String location) {
        ParkingPlace entity = new ParkingPlace();
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        entity.setCreateTime(currentTime).setUpdateTime(currentTime).setLocation(location);
        mapper.insert(entity);
        return entity.getId();
    }

    public BigInteger update(BigInteger id, String location) {
        if (id == null) {
            log.error("id is null");
        }
        //不进行查库避免压力
        ParkingPlace entity = extractById(id);
        if (entity == null) {
            log.error("no such id ");
            throw new RuntimeException("no such id ");
        }
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        entity.setLocation(location).setId(id).setUpdateTime(currentTime);
        mapper.update(entity);
        return id;
    }

    public int delete(BigInteger id) {
        if (id == null) {
            log.error("id is null");
            return 0;
        }
        int currentTime = (int) (System.currentTimeMillis() / 1000);
        return mapper.delete(id, currentTime);
    }

    public ParkingPlace getById(BigInteger id) {
        return mapper.getById(id);
    }

    public ParkingPlace extractById(BigInteger id) {
        return mapper.extractById(id);
    }
    //interceptor 拦截器

    public List<ParkingPlace> getListForAdministrator(Integer page, Integer pageSize, String location) {
        int offset = (page - 1) * pageSize;
        ParkingPlace parkingPlace = new ParkingPlace();
        parkingPlace.setLocation(location);
        return mapper.getListForAdministrator(offset, pageSize, parkingPlace);
    }

    public int getTotalCountForAdministrator(String location) {
        ParkingPlace parkingPlace = new ParkingPlace();
        parkingPlace.setLocation(location);
        return mapper.getTotalCountForAdministrator(parkingPlace);

    }

    public List<ParkingPlace> getListForUser(Integer page, Integer pageSize, String location) {
        int offset = (page - 1) * pageSize;
        ParkingPlace parkingPlace = new ParkingPlace();
        parkingPlace.setLocation(location);
        return mapper.getListForUser(offset, pageSize, parkingPlace);
    }

    public List<ParkingPlace> getListForAdministratorByIdList(int page, int pageSize, String idList) {
        int offset = (page - 1) * pageSize;
        return mapper.getListForAdministratorByIdList(offset, pageSize, idList);
    }

    public List<ParkingPlace> getListForUserByIdList(int page, int pageSize, String idList) {
        int offset = (page - 1) * pageSize;
        return mapper.getListForUserByIdList(offset, pageSize, idList);
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












