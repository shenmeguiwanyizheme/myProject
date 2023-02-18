package com.example.mapper;

import com.example.pojo.ParkingPlace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 充电桩所在位置表 Mapper 接口
 * </p>
 *
 * @author maoring
 * @since 2023-01-19
 */
@Mapper
public interface ParkingPlaceMapper {
    @Select("select * from parking_place where is_deleted=0 and id =#{id}")
    ParkingPlace getById(@Param("id") BigInteger id);

    @Select("select * from parking_place where id =#{id} ")
    ParkingPlace extractById(@Param("id") BigInteger id);

    int insert(@Param("entity") ParkingPlace entity);//不支持返回bigInteger

    @Update("update parking_place set is_deleted =1 ,update_time =#{updateTime} where id=#{id}")
    int delete(@Param("id") BigInteger id, @Param("updateTime") int updateTime);

    int update(@Param("entity") ParkingPlace entity);


    List<ParkingPlace> getListForUser(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("entity") ParkingPlace entity);


    List<ParkingPlace> getListForAdministrator(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("entity") ParkingPlace entity);


    int getTotalCountForAdministrator(@Param("entity") ParkingPlace entity);

    @Select("select * from parking_place where id in (${idList})")
    List<ParkingPlace> getListForAdministratorByIdList(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("idList") String idList);
}
