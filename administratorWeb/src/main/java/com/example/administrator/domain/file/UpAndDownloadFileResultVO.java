package com.example.administrator.domain.file;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpAndDownloadFileResultVO {
    
    private Boolean isSuccessed;
    private String storeUrl;
    private String errorMessage;

}










