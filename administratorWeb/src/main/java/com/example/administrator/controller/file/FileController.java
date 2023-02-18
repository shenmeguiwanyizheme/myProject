package com.example.administrator.controller;

import com.example.administrator.domain.UpAndDownloadFileResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
public class FileController {
    @Value("${file.upload.directory}")
    private String filePathParent;
    @Value("${file.upload.disableTypes}")
    private String disableFileTypes;

    @RequestMapping("/upload_charging_pile_photo")
    public UpAndDownloadFileResultVO uploadChargingPilePhoto(@RequestPart("file") MultipartFile uploadFile) {
        String fileName = uploadFile.getOriginalFilename();
        String suffixFileName = fileName.substring(fileName.lastIndexOf("."));
        String[] disableFileTypeArray = disableFileTypes.split(",");
        if (suffixFileName == null || suffixFileName.equals("")) {
            log.error("文件后缀名不可为空");
            return new UpAndDownloadFileResultVO().setIsSuccessed(false).setErrorMessage("文件后缀名不可为空");
        }
        for (String disableFile : disableFileTypeArray) {
            if (disableFile.equals(suffixFileName)) {
                log.error("该文件类型不可上传");
                return new UpAndDownloadFileResultVO().setIsSuccessed(false).setErrorMessage("该文件类型不可上传");
            }
            //实际上这种方式并不是很安全，当出现篡改后缀名或没有后缀名的时候，仍然会被攻击
            //最稳妥的做法是查看该文件的前几个字节来判断文件的类型
        }
        String filePath = filePathParent + File.separator + UUID.randomUUID() + fileName;
        File storeFile = new File(filePath);
        if (!storeFile.exists()) {
            storeFile.mkdirs();
            try {
                storeFile.createNewFile();
            } catch (IOException exception) {
                log.error("文件创建失败" + exception.getMessage());
                return new UpAndDownloadFileResultVO().setIsSuccessed(false).setErrorMessage(exception.getMessage());
            }
        }
        try {
            uploadFile.transferTo(storeFile);
        } catch (IOException exception) {
            log.error("文件传输失败" + exception.getMessage());
            return new UpAndDownloadFileResultVO().setIsSuccessed(false).setErrorMessage(exception.getMessage());
        }
        //DigestUtils.md5Digest(filePath.getBytes(StandardCharsets.UTF_8));
        return new UpAndDownloadFileResultVO().setIsSuccessed(true).setStoreUrl(filePath);
    }

   
}
