package com.mqz.controller;

import com.mqz.domin.ResponseResult;
import com.mqz.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping()
    public ResponseResult upload(MultipartFile img){
        return uploadService.upload(img);
    }
}
