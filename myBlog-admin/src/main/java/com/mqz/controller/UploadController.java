package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.exception.SystemException;
import com.mqz.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;

import static com.mqz.enums.AppHttpCodeEnum.OSS_IMG_UPLOAD_FAILED;

@RestController
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        try {
            return uploadService.upload(img);
        } catch (Exception e) {
            throw new SystemException(OSS_IMG_UPLOAD_FAILED);
        }
    }
}
