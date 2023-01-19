package com.example.user.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ParkingPlaceBaseInfoListVO {
    private List<ChargingPileSpecificInfoVO> chargingPiles;
    private Boolean isEnd;
}
