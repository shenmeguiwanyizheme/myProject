package com.example.user.domain.chargingpile;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ChargingPileListVO {
    private String wp;

    private List<ChargingPileBaseInfoVO> chargingPiles;
    private Boolean isEnd;
}
