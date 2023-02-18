package com.example.administrator.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain = true)
public class ChargingPileSpecificInfoVO {
    private BigInteger id;
    private String serialNumber;
    private Boolean canUsed;//当前是否空闲，true表示空闲
    private String canUsedTime;//在canUsed为false的情况下，才标识什么时候可以被用
    private Integer row;//行号
    private Integer col;//列号
    private String parkingPlace;
    private Integer chargingType;
    private String beginUsedTime;//出厂日期
    private BigInteger price;//每小时，多少元
    private Integer power;//每小时多少焦耳
    private List<String> photoUrlList;//关于充电桩的图片的连接
    private String createTime;
    private String updateTime;
    private Boolean isDeleted;
}
