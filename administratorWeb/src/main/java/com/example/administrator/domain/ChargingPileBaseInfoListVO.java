package com.example.administrator.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain = true)
public class ChargingPileBaseInfoListVO {
    private List<ChargingPileBaseInfoVO> chargingPiles;
    private Integer pageSize;
    private BigInteger total;
}
