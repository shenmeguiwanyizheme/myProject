package com.example.pojo.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class User {
    private BigInteger id;
    private String username;
    private String password;
    private Integer createTime;
    private Integer updateTime;
    private Integer isDeleted;
}
