package com.example.administrator.domain.chargingpile;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ChargingPileBaseInfoListVO {
    private List<ChargingPileBaseInfoVO> chargingPiles;
    private Integer pageSize;
    private Integer total;
}
