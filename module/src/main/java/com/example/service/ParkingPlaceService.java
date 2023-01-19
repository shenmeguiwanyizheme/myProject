package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

        UpdateWrapper<ParkingPlace> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("update_time", updateTime).set("location", location).eq("id", id);
        int res = mapper.update(null, updateWrapper);
        if (res == 0) {
            throw new RuntimeException("要更新的充电桩位置id不存在");
        }
        return res;
    }

    public int delete(BigInteger id, int updateTime) throws Exception {
        QueryWrapper<ParkingPlace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        ParkingPlace p = new ParkingPlace().setIsDeleted(1).setUpdateTime(updateTime);
        int res = mapper.update(p, queryWrapper);
        if (res == 0) {
            throw new RuntimeException("要删除的充电桩id不存在");
        }
        return res;
    }

    public ParkingPlace getByIdForAdministrator(BigInteger id) throws Exception {
        if (id == null) throw new RuntimeException("要查询的充电桩位置id不存在");
        QueryWrapper<ParkingPlace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);//没删除的和删除的全部要查出来
        ParkingPlace res = mapper.selectOne(queryWrapper);
        if (res == null) {
            throw new RuntimeException("充电桩所在位置id不存在");
        }
        return res;
    }
    //interceptor 拦截器

    public List<ParkingPlace> getListForAdministrator(Integer page, Integer pageSize) {
        Page<ParkingPlace> pageObject = new Page<>(page, pageSize);
        mapper.selectPage(pageObject, null);
        return pageObject.getRecords();
    }

    public BigInteger getTotalCountForAdministrator(Integer page, Integer pageSize) {
        Page<ParkingPlace> pageObject = new Page<>(page, pageSize);
        mapper.selectPage(pageObject, null);
        return BigInteger.valueOf(pageObject.getTotal());

    }

    public List<ParkingPlace> getListForUser(Integer page, Integer pageSize) {
        QueryWrapper<ParkingPlace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);
        Page<ParkingPlace> pageObject = new Page<>(page, pageSize);
        mapper.selectPage(pageObject, queryWrapper);
        return pageObject.getRecords();
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












