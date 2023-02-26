package com.example.pojo.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class UserSign {
    private BigInteger id;
    private Integer expireTime;
    private String salt;
}
