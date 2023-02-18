package com.example.user.domain.parkingplace;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class ParkingPlaceSpecificInfoVO {
    private BigInteger id;
    private String location;
}
