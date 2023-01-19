package com.example.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class ChargingPile {
    private BigInteger id;
    private String serialNumber;


    private Integer canUsed;//当前是否空闲，1表示空闲,0表示非空闲
    private Integer canUsedTime;//在canUsed为false的情况下，才标识什么时候可以被用
    private Integer row;//行号
    private Integer col;//列号
    private BigInteger parkingPlace;
    private Integer chargingType;
    private Integer beginUsedTime;
    private BigInteger price;//每小时，多少分
    private Integer power;//每小时多少焦耳
    private String photoUrlList;//关于充电桩的图片的连接
    private String tag;//先假设有这个字段
    private Integer createTime;
    private Integer updateTime;//Entity数据库实体的所有类型都跟数据库一样
    private Integer isDeleted;
    //一共十五个属性

}
