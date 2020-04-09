package com.leyou.upload.controller;

import com.leyou.upload.config.OSSProperties;
import com.leyou.upload.service.UploadService;
import com.leyou.upload.util.OSSUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author RuXing
 * @create 2020-04-01 13:19
 */
@Controller
@RequestMapping("/upload")
public class ImageUploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file")MultipartFile file){
        String url = uploadService.uploadImage(file);
        if (StringUtils.isBlank(url)){
            return ResponseEntity.badRequest().build();
        }
        // 返回成功信息，和url地址
        return ResponseEntity.status(HttpStatus.CREATED).body(url);
    }

}
