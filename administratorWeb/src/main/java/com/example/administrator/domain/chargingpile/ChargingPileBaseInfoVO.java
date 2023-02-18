package com.example.administrator.domain.chargingpile;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class ChargingPileBaseInfoVO {
    private BigInteger id;

    private String image;
    private String parkingPlace;
    private Integer row;
    private Integer col;
    private Boolean isDeleted;
}
