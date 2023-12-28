package com.hao.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.base.exception.XCException;
import com.hao.media.mapper.MediaFileMapper;
import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.media.mapper.MediaProcessMapper;
import com.hao.media.model.dto.QueryMediaParamsDto;
import com.hao.media.model.dto.UploadFileDTO;
import com.hao.media.model.po.MediaFiles;
import com.hao.media.model.po.MediaProcess;
import com.hao.media.model.vo.UploadFileResultVO;
import com.hao.media.service.MediaFileService;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Hao
 * @version 1.0
 * @description TODO
 */
@Slf4j
@Service
public class MediaFileServiceImpl implements MediaFileService {
    @Autowired
    private MediaFileMapper mediaFileMapper;
    @Autowired
    MinioClient minioClient;
    @Autowired
    MediaFileService currentProxy;
    @Autowired
    MediaProcessMapper mediaProcessMapper;

    //存储普通文件
    @Value("${minIO.bucket.files}")
    private String bucketMediafiles;
    //存储视频
    @Value("${minIO.bucket.videofiles}")
    private String bucketVideo;

    /**
     * 将文件上传到minio
     * @param localFilePath 文件本地路径
     * @param mimeType 媒体类型
     * @param bucketName 桶
     * @param objectName 对象名
     * @return
     */
    public boolean UploadMediaFilesToMinIO(String localFilePath,String mimeType,String bucketName, String objectName){
        try {
            //组装上传图片的参数信息
            UploadObjectArgs uploadObjectArgs = new UploadObjectArgs()
                    .builder()
                    .bucket(bucketName) //桶名称
                    .filename(localFilePath) //上传文件的路径
                    .object(objectName) //上传文件的名称
                    .contentType(mimeType)
                    .build();

            minioClient.uploadObject(uploadObjectArgs);
            log.debug("上传文件成功,bucket:{},objectName:{}", bucketName, objectName);
            return true;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("上传文件出错,bucket:{},objectName:{},错误信息:{}", bucketName, objectName, e.getMessage());

            return false;
        }

    }
    //获取上传文件的扩展名
    public String getFileMimeType(String extension){
        if (extension == null){
            extension = "";
        }
        //根据扩展名取出mineType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; //通用mimeType，字节流
        if (extensionMatch != null){
            mimeType = extensionMatch.getMimeType();
        }
        return mimeType;
    }

    //获取文件默认存储目录路径 年/月/日
    public String getDefaultFolderPath() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String folder = sdf.format(new Date()).replace("-", "/")+"/";
        return folder;
    }

    //获取文件的md5
    public String getFileMd5(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            String fileMd5 = DigestUtils.md5Hex(fileInputStream);
            return fileMd5;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 媒资系统分页查询
     * @param companyId 文件本地路径
     * @param pageParams 媒体类型
     * @param queryMediaParamsDto 桶
     * @return PageResult<MediaFiles>
     */
    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto) {
        if (companyId == null){
            throw new XCException("机构id不存在");
        }
        if (pageParams.getPageNo() == null || pageParams.getPageSize() == null){
            throw new XCException("分页参数不存在");
        }

        //条件查询参数组装
        LambdaQueryWrapper<MediaFiles> QueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(queryMediaParamsDto.getFilename())){
            QueryWrapper.like(MediaFiles::getFilename,queryMediaParamsDto.getFilename());
        }
        if (!StringUtils.isEmpty(queryMediaParamsDto.getFileType())){
            QueryWrapper.eq(MediaFiles::getFileType,queryMediaParamsDto.getFileType());
        }

        //分页查询结果
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        Page<MediaFiles> pageResult = mediaFileMapper.selectPage(page, QueryWrapper);

        //获得总结果数
        Long total = pageResult.getTotal();

        //获得媒资所有信息
        List<MediaFiles> queryList = pageResult.getRecords();

        PageResult<MediaFiles> queryResult = new PageResult<>(queryList,total,pageParams.getPageNo(),pageParams.getPageSize());
        return queryResult;
    }

    /**
     * @description 将文件信息添加到文件表
     * @param companyId  机构id
     * @param uploadfileDTO  上传文件的信息
     * @param localFilePath  上传文件的本地地址
     * @return com.hao.media.model.po.MediaFiles
     * @author hao
     */
    @Transactional
    public UploadFileResultVO uploadFile(Long companyId, UploadFileDTO uploadfileDTO, String localFilePath) {
        //文件名
        String fileName = uploadfileDTO.getFilename();

        //获取文件的扩展名
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String mimeType = getFileMimeType(extension);

        //设定上传文件的名称
        String defalultFolderPath = getDefaultFolderPath();
        String FileMD5 = getFileMd5(new File(localFilePath));
        String objectName = defalultFolderPath+FileMD5+extension;

        //上传文件至minIO
        boolean UploadStatus = UploadMediaFilesToMinIO(localFilePath,mimeType,bucketMediafiles,objectName);
        if (!UploadStatus){
            throw new XCException("文件上传失败");
        }

        //将上传图片信息存储至数据库,并获取媒资信息
        MediaFiles mediaFiles = currentProxy.uploadFileToDB(companyId,FileMD5,uploadfileDTO,bucketMediafiles,objectName);
        UploadFileResultVO uploadFileResultVO = new UploadFileResultVO();
        if (mediaFiles == null){
            throw new XCException("向数据库插入失败");
        }

        BeanUtils.copyProperties(mediaFiles,uploadFileResultVO);

        return uploadFileResultVO;
    }

    /**
     * @description 将文件信息添加到文件表
     * @param companyId  机构id
     * @param fileMd5  文件md5值
     * @param uploadFileDTO  上传文件的信息
     * @param bucket  桶
     * @param objectName 对象名称
     * @return com.hao.media.model.po.MediaFiles
     * @author hao
     */
    @Transactional
    public MediaFiles uploadFileToDB(Long companyId,String fileMd5,UploadFileDTO uploadFileDTO,String bucket,String objectName){
        //查询是否文件已存在
        MediaFiles mediaFiles = mediaFileMapper.selectById(fileMd5);
        if (mediaFiles == null){
            MediaFiles mediaFiles1 = new MediaFiles();
            if (uploadFileDTO == null){
                throw new XCException("所传入文件信息为空");
            }
            BeanUtils.copyProperties(uploadFileDTO,mediaFiles1);

            if (companyId == null){
                throw new XCException("机构id为空");
            }
            mediaFiles1.setCompanyId(companyId);

            if (fileMd5 == null){
                throw new XCException("文件MD5值为空");
            }
            mediaFiles1.setFileId(fileMd5);
            mediaFiles1.setId(fileMd5);

            if (bucket == null){
                throw new XCException("文件所上传的桶为空");
            }
            mediaFiles1.setBucket(bucket);

            if (objectName == null){
                throw new XCException("文件名称为空");
            }
            mediaFiles1.setFilePath(objectName);

            mediaFiles1.setUrl("/"+bucket+"/"+objectName);
            mediaFiles1.setStatus("1");
            mediaFiles1.setAuditStatus("002003");
            mediaFiles1.setChangeDate(LocalDateTime.now());
            mediaFiles1.setCreateDate(LocalDateTime.now());

            int count = mediaFileMapper.insert(mediaFiles1);
            if (count != 1){
                log.debug("向数据库保存数据失败,bucket:{},objectName:{}",bucket,objectName);
                return null;
            }
            return mediaFiles1;
        }

        //记录待处理的任务
        //通过判断视频格式并写入待处理任务
        //向MediaProcess数据表写入
        return mediaFiles;
    }

    /**
     * @description 添加待处理的任务
     * @param mediaFiles  文件信息
     * @author hao
     */
    private void UploadWaitingTaskToDB(MediaFiles mediaFiles){
        //获取mimeType
        String fileName = mediaFiles.getFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String mimeType = getFileMimeType(extension);

        //如果视频格式为AVI，写入数据库
        if (mimeType.equals("video/x-msvideo")){
            //组装MediaProcess
            MediaProcess mediaProcess = new MediaProcess();
            mediaProcess.setFileId(mediaFiles.getFileId());
            BeanUtils.copyProperties(mediaFiles,mediaProcess);
            mediaProcess.setStatus("1");
            mediaProcess.setCreateDate(LocalDateTime.now());
            mediaProcess.setUrl(null);
            mediaProcess.setFailCount(0);

            mediaProcessMapper.insert(mediaProcess);
        }
    }
}
