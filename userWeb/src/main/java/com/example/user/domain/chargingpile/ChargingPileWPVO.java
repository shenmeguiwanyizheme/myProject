package com.example.user.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChargingPileWPVO {
    private String serialNumber;
    private Integer page;
    private String place;
}
