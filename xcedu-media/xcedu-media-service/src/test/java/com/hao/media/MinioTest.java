package com.hao.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.io.*;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试minio的sdk
 * @date 2023/2/17 11:55
 */
public class MinioTest {

    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://192.168.101.65:9000")
                    .credentials("minioadmin", "minioadmin")
                    .build();

    @Test
    public void test_upload() throws Exception {

        //通过扩展名得到媒体资源类型 mimeType
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findMimeTypeMatch(".mp4");
        String mineType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (extensionMatch != null){
            mineType = extensionMatch.getMimeType();
        }


        //上传文件
        UploadObjectArgs testUpload = UploadObjectArgs.builder()
                .bucket("testbucket")
                .filename("D:\\minIOTest\\1.mp4")
                .object("test/1t.mp4")
                .contentType(mineType)
                .build();

        minioClient.uploadObject(testUpload);


    }
    //删除文件
    @Test
    public void test_delete() throws Exception {

        //RemoveObjectArgs
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("testbucket")
                .object("test/1t.mp4")
                .build();

        //删除文件
        minioClient.removeObject(removeObjectArgs);



    }

    //查询文件 从minio中下载
    @Test
    public void test_getFile() throws Exception {

//        GetObjectArgs getObjectArgs = GetObjectArgs
//                .builder()
//                .bucket("testbucket")
//                .object("test/1t.mp4")
//                .build();
//
//        //获取下载文件的文件输入流
//        FilterInputStream filterInputStream =  minioClient.getObject(getObjectArgs);
//        //指定输出流
//        FileOutputStream outputStream = new FileOutputStream("D:\\minIOTest\\upload\\1a.mp4");
//        //将获取文件的输入流拷贝到指定位置的输出流
//        IOUtils.copy(filterInputStream,outputStream);

        //MD5校验文件下载的完整性
//        String sourceMD5 = DigestUtils.md5Hex(filterInputStream);
//        String sourceMD5 = "ecc8fdfa9da8d1643f23831217488dff-13";
        String localMD5 = DigestUtils.md5Hex(new FileInputStream("D:\\minIOTest\\upload\\1a.mp4"));
        System.out.println(localMD5);
//
//        StatObjectArgs statObjectArgs = StatObjectArgs
//                .builder()
//                .bucket("testbucket")
//                .object("test/1t.mp4")
//                .build();
//
//        StatObjectResponse fileStream = minioClient.statObject(statObjectArgs);
//        String sourceMD5 =fileStream.etag();
//        System.out.println(sourceMD5);
    }









}
