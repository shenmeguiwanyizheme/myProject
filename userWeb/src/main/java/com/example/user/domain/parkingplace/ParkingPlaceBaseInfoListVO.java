package com.example.user.domain.parkingplace;

import com.example.user.domain.chargingpile.ChargingPileSpecificInfoVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ParkingPlaceBaseInfoListVO {
    private List<ChargingPileSpecificInfoVO> chargingPiles;
    private Boolean isEnd;
}
