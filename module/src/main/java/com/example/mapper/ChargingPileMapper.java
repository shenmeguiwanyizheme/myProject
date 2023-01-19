package com.example.mapper;

import com.example.pojo.ChargingPile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface ChargingPileMapper {
    BigInteger getTotalCountForAdministrator(@Param("sn") String serialNumber, @Param("pId") Integer pId);


    List<ChargingPile> getChargingPileListForUser(@Param("sn") String serialNumber, @Param("from") int from, @Param("pageSize") int pageSize, @Param("pId") Integer p);

    List<ChargingPile> getChargingPileListForAdministrator(@Param("sn") String serialNumber, @Param("from") int from, @Param("pageSize") long pageSize, @Param("pId") Integer p);

    @Select("select * from charging_pile where id=#{id}")
    ChargingPile extractById(@Param("id") BigInteger id);//这个是有没有删除都可以查询出来的


    //下面几个是通用接口，就不修改了
    @Select("select  * from charging_pile where id=#{id} and is_deleted=0")
    ChargingPile getById(@Param("id") BigInteger id);

    //不写xml其实都可以不用注解，不写param注解，id表示的是属性或者是其值本身，数字类的应该就是值，写了注解@param（“id'）则i
//    @Options(useGeneratedKeys = true, keyProperty = "entity.id", keyColumn = "id")
//    @SelectKey(keyColumn = "id", keyProperty = "entity.id", before = false, statement = "select last_insert_id()"
//            , resultType = BigInteger.class)
    int insert(@Param("entity") ChargingPile entity);


    @Update("update charging_pile set  is_deleted=1 ,update_time=#{updateTime} where id =#{id}")
    int delete(@Param("id") BigInteger id, @Param("updateTime") int updateTime);

    int update(@Param("entity") ChargingPile entity);//采用 类名-entity的命名方式挺好的，因为mapper名已经告诉了操纵的是哪张表
}

























