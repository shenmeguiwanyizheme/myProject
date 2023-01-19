package com.example.user.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class ParkingPlaceSpecificInfoVO {
    private BigInteger id;
    private String location;
}
