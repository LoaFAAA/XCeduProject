package com.hao.media.api;

import com.hao.base.model.RestResponse;
import com.hao.media.service.LargeFileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
public class LargeFileController {
    @Autowired
    private LargeFileService largeFileService;

    /**
     * @description 文件上传接口
     * @author hao
     */
    @ApiOperation(value = "文件上传前检查文件")
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkFile(@RequestParam("fileMd5") String fileMD5){

        return largeFileService.checkFile(fileMD5);
    }

    /**
     * @description 分块文件上传接口
     * @author hao
     */
    @ApiOperation(value = "分块文件上传前检查文件")
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkchunk(@RequestParam("fileMd5") String fileMD5,
                                            @RequestParam("chunk") int chunk){

        RestResponse<Boolean> booleanRestResponse = largeFileService.checkChunk(fileMD5,chunk);

        return booleanRestResponse;
    }

    /**
     * @description 上传分块文件
     * @author hao
     */
    @ApiOperation(value = "上传分块文件")
    @PostMapping("/upload/uploadchunk")
    public RestResponse uploadchunk(@RequestParam("file")MultipartFile file,
                                    @RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("chunk") int chunk) throws IOException {

        File tempFile = File.createTempFile("minio",".temp");
        file.transferTo(tempFile);

        //上传文件的本地地址
        String localFilePath = tempFile.getAbsolutePath();
        return largeFileService.uploadChunk(fileMd5,chunk,localFilePath);
    }

    /**
     * @description 合并分块文件
     * @author hao
     */
    @ApiOperation(value = "合并分块文件")
    @PostMapping("/upload/mergechunk")
    public RestResponse mergechunk(@RequestParam("fileName") String fileName,
                                   @RequestParam("fileMd5") String fileMd5,
                                   @RequestParam("chunkTotal") int chunkTotal) throws Exception{


        return null;
    }
}
