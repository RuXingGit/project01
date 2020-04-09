package com.leyou.upload.service;

import com.leyou.upload.config.OSSProperties;
import com.leyou.upload.util.OSSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
 * @create 2020-03-27 18:20
 */
@Service
public class UploadService {
    @Autowired
    private OSSProperties ossProperties;

    // 白名单
    private static final List<String> CONTENT_TYPES= Arrays.asList(
        "image/gif","image/jpeg"
    );

    // 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);



    public String uploadImage(MultipartFile file){
        try {
            // 校验文件类型
            String originalFilename = file.getOriginalFilename();
            // 通过文件名获取后缀名
            //String suffix = StringUtils.substringAfterLast(originalFilename, ".");
            // 通过请求头的请求类型获取文件类型
            String contentType = file.getContentType();
            if(!CONTENT_TYPES.contains(contentType)){
                LOGGER.error("文件类型不支持:{}",originalFilename);
                return null;
            }
            // 校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage==null){
                LOGGER.error("文件内容不合法:{}",originalFilename);
                return null;
            }
            // 保存到服务器
            return OSSUtil.uploadFile(ossProperties,file);
        } catch (IOException e) {
            return null;
        }
    }



    /*// 上传图片
    public String uploadImage(MultipartFile file) {
        try {
            // 校验文件类型
            String originalFilename = file.getOriginalFilename();
            // 通过文件名获取后缀名
            //String suffix = StringUtils.substringAfterLast(originalFilename, ".");
            // 通过请求头的请求类型获取文件类型
            String contentType = file.getContentType();
            if(!CONTENT_TYPES.contains(contentType)){
                LOGGER.error("文件类型不支持:{}",originalFilename);
                return null;
            }
            // 校验文件内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage==null){
                LOGGER.error("文件类容不合法:{}",originalFilename);
                return null;
            }
            // 保存到服务器
            file.transferTo(new File("F:\\leyou\\images"+originalFilename));
            *//**
             * 使用Nginx代理一个图片路径
             * service-name image.leyou.com
             * location / {root F:\\leyou\\images;}
             * http://image.leyou.com/1.jpg
             *
             * 使用Nginx代理绕过网关上传图片
             *  在api.leyou.com 的localtion /之前添加额外的location配置
             *  location /api/upload {proxy_pass http://127.0.0.1:2400}
             *  路径重写，因为请求进行了整体配置 都是以/api/** 开头(但图片上传不是)
             *  正则表达式,匹配所有api开头的url (break表示重写路径后不再按路径请求nginx,last类似重定向)
             *  rewrite "^/api/(.*)$" /$1 break;
             *
             *  注意需要配置一下跨域问题，因为不再走网关了，需要在upload这个项目中配置
             *//*
            // 返回url
            return "http://image.leyou.com/"+originalFilename;
        } catch (IOException e) {
            LOGGER.error("系统错误:{}",e.getMessage());
            e.printStackTrace();
        }
        return null;
    }*/
}
