package com.example.administrator.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ParkingPlaceBaseInfoListVO {
    private List<ParkingPlaceSpecificInfoVO> parkingPlaces;
    private Integer pageSize;
    private Integer total;
}
