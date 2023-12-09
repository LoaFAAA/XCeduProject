package com.hao.media.api;

import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.media.model.dto.QueryMediaParamsDto;
import com.hao.media.model.dto.UploadFileDTO;
import com.hao.media.model.po.MediaFiles;
import com.hao.media.model.vo.UploadFileResultVO;
import com.hao.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @description 媒资文件管理接口
 * @author hao
 */
@Api(value = "媒资文件管理接口",tags = "媒资文件管理接口")
@RestController
public class MediaFilesController {
    @Autowired
    MediaFileService mediaFileService;

    /**
     * @description 分页查询媒资接口
     * @author hao
     */
    @ApiOperation("媒资列表查询接口")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDto queryMediaParamsDto){
        Long companyId = 1232141425L;

        return mediaFileService.queryMediaFiles(companyId,pageParams,queryMediaParamsDto);
    }

    /**
     * @description 将文件信息添加到文件表
     * @author hao
     */
    @ApiOperation("上传图片")
    @RequestMapping(value = "/upload/coursefile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResultVO upload(@RequestPart("filedata")MultipartFile filedata) throws IOException {
        //构建上传的文件信息
        UploadFileDTO uploadFileDTO = new UploadFileDTO();

        //文件的原始名称
        uploadFileDTO.setFilename(filedata.getOriginalFilename());

        //文件的大小
        uploadFileDTO.setFileSize(filedata.getSize());
        //文件类型
        uploadFileDTO.setFileType("001001");
        //创建临时文件
        File tempFile = File.createTempFile("minio",".temp");
        filedata.transferTo(tempFile);

        Long companyId = 1232141425L;

        //上传文件的本地地址
        String localFilePath = tempFile.getAbsolutePath();

        UploadFileResultVO uploadFileResultVO = mediaFileService.uploadFile(companyId,uploadFileDTO,localFilePath);
        return uploadFileResultVO;
    }
}
