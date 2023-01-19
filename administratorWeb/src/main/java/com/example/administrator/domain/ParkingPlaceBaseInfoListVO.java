package com.example.administrator.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain = true)
public class ParkingPlaceBaseInfoListVO {
    private List<ParkingPlaceSpecificInfoVO> parkingPlaces;
    private Integer pageSize;
    private BigInteger total;//因为mybatis-plus返回的是long值，直接采用bigInteger
}
