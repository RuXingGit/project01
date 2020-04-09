package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-28 12:30
 */
@Service
public class UploadByFastDFS {
    // 白名单
    private static final List<String> CONTENT_TYPES= Arrays.asList(
            "image/gif","image/jpeg"
    );

    // 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);

    // 上传文件的对象
    @Autowired
    private FastFileStorageClient storageClient;
    // 获取缩略图的对象
    private ThumbImageConfig thumbImageConfig;

    // 上传图片
    public String uploadImage(MultipartFile file) {
        try {
            // 校验文件类型
            String originalFilename = file.getOriginalFilename();

            // 校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage==null){
                LOGGER.error("文件类容不合法:{}",originalFilename);
                return null;
            }
            // 通过请求头的请求类型获取文件类型
            String contentType = file.getContentType();
            if(!CONTENT_TYPES.contains(contentType)){
                LOGGER.error("文件类型不支持:{}",originalFilename);
                return null;
            }
            // 获取扩展名
            String suffix = StringUtils.substringAfterLast(originalFilename, ".");
            //上传文件
            StorePath storePath = storageClient.uploadFile(
                    file.getInputStream(), file.getSize(), suffix, null
            );
            // 返回
            return "http://image.leyou.com/"+storePath;

        } catch (IOException e) {
            LOGGER.error("系统错误:{}",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
