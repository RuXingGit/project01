package com.leyou;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author RuXing
 * @create 2020-03-28 12:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class FastDFSTest {
    // 用来上传文件
    @Autowired
    private FastFileStorageClient storageClient;
    // 上传文件并获取缩略图
    @Autowired
    private ThumbImageConfig thumbImageConfig;

    // 上传文件
    @Test
    public void testUpload() throws FileNotFoundException {
        File file = new File("F:/image/1.jpg");
        StorePath storePath = storageClient.uploadFile(
                new FileInputStream(file), file.length(), "jpg", null
        );
        // 带分组的路径
        System.out.println(storePath.getFullPath());
        // 不带分组的路径
        System.out.println(storePath.getPath());
    }

    @Test
    public void testThumb() throws FileNotFoundException {
        File file = new File("F:/image/1.jpg");
        // 上传文件并获取缩略图
        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(
                new FileInputStream(file), file.length(), "jpg", null
        );

        // 获取缩略图路径
        String path = thumbImageConfig.getThumbImagePath(storePath.getPath());
        System.out.println(path);
    }
}
