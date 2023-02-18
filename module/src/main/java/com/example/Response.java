package com.example.administrator.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Response {
    private Integer code;
    private String msg;
    private String result;
}
