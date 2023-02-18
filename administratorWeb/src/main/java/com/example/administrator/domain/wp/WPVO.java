package com.example.administrator.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WPVO {
    private String base64Code;
}
