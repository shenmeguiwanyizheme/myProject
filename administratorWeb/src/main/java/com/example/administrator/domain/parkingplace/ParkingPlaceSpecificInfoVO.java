package com.example.administrator.domain.parkingplace;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class ParkingPlaceSpecificInfoVO {
    private BigInteger id;
    private String location;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
