package com.example.mapper;

import com.example.pojo.ParkingPlace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

    BigInteger insert(@Param("entity") ParkingPlace entity);

    int delete(@Param("id") BigInteger id);

    int update(@Param("entity") ParkingPlace entity);

    @Select("select * from parking_place where is_deleted=0 limit #{from},#{pageSize}")
    List<ParkingPlace> getListForUser(@Param("from") int from, @Param("pageSize") int pageSize);

    @Select("select * from parking_place limit #{from},#{pageSize}")
    List<ParkingPlace> getListForAdministrator(@Param("from") int from, @Param("pageSize") int pageSize);

    @Select("select count(*) from parking_place")
    BigInteger getTotalCountForAdministrator(@Param("from") int from, @Param("pageSize") int pageSize);
}
