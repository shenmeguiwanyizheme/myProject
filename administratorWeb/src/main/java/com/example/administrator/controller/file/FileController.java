package com.example.administrator.controller.file;

import com.example.administrator.annotations.VerifierUser;
import com.example.administrator.domain.file.UpAndDownloadFileResultVO;
import com.example.pojo.user.User;
import com.example.utils.BaseUtils;
import com.example.utils.Response;
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

    //管理员端需要被校验
    @RequestMapping("/upload_charging_pile_photo")
    public Response uploadChargingPilePhoto(
            @VerifierUser User loginUser,
            @RequestPart("file") MultipartFile uploadFile) {
        if (BaseUtils.isEmpty(loginUser)) {
            return new Response(4004);
        }
        String fileName = uploadFile.getOriginalFilename();
        int suffixFileNameIndex = fileName.lastIndexOf(".");
        if (suffixFileNameIndex == fileName.length() || suffixFileNameIndex == -1) {
            UpAndDownloadFileResultVO resultVO = new UpAndDownloadFileResultVO().setIsSuccessed(false).setErrorMessage("文件后缀名不可为空");
            return new Response().setCode(4015).setResult(resultVO);
        }
        String suffixFileName = fileName.substring(suffixFileNameIndex + 1);
        log.info(suffixFileName);
        String[] disableFileTypeArray = disableFileTypes.split(",");
        for (String disableFile : disableFileTypeArray) {
            if (disableFile.equals(suffixFileName)) {
                log.error("该文件类型不可上传");
                UpAndDownloadFileResultVO resultVO = new UpAndDownloadFileResultVO().setIsSuccessed(false).setErrorMessage("该文件类型不可上传");
                return new Response().setCode(4012).setResult(resultVO);
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
                UpAndDownloadFileResultVO resultVO = new UpAndDownloadFileResultVO().setIsSuccessed(false).setErrorMessage(exception.getMessage());
                return new Response().setCode(4013).setResult(resultVO);
            }
        }
        try {
            uploadFile.transferTo(storeFile);
        } catch (IOException exception) {
            log.error("文件传输失败" + exception.getMessage());
            UpAndDownloadFileResultVO resultVO = new UpAndDownloadFileResultVO().setIsSuccessed(false).setErrorMessage(exception.getMessage());
            return new Response().setCode(4014).setResult(resultVO);
        }
        //DigestUtils.md5Digest(filePath.getBytes(StandardCharsets.UTF_8));
        UpAndDownloadFileResultVO resultVO = new UpAndDownloadFileResultVO().setIsSuccessed(true).setStoreUrl(filePath);
        return new Response().setCode(200).setResult(resultVO);
    }


}
