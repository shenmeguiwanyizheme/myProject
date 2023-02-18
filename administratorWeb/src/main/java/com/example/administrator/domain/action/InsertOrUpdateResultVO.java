package com.example.administrator.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Data
@Accessors(chain = true)
public class InsertOrUpdateResultVO {
    private Boolean isSuccessed;
    private BigInteger id;
    private String errorMessage;
    
}
