package com.hao.media.service.impl;

import com.hao.base.model.RestResponse;
import com.hao.media.mapper.MediaFileMapper;
import com.hao.media.model.dto.UploadFileDTO;
import com.hao.media.model.po.MediaFiles;
import com.hao.media.service.LargeFileService;
import com.hao.media.service.MediaFileService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class LargeFileServiceImpl implements LargeFileService {
    @Autowired
    private MediaFileMapper mediaFileMapper;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MediaFileService mediaFileService;
    @Value("${minIO.bucket.videofiles}")
    private String bucketVideo;

    public RestResponse<Boolean> checkFile(String fileMD5) {
        //查询数据库
        MediaFiles mediaFiles = mediaFileMapper.selectById(fileMD5);
        if (mediaFiles != null){
            String bucketName = mediaFiles.getBucket();
            String filePath = mediaFiles.getFilePath();

            //组装查询minIO的参数
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(filePath)
                    .build();

            try {
                    FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
                    if (inputStream != null){
                        return RestResponse.success(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
            }
        }
        return RestResponse.success(false);
    }

    @Override
    public RestResponse<Boolean> checkChunk(String fileMD5, int chunkIndex) {
        //获取文件的分块路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMD5);

        //如果数据库存在再查询 minio
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucketVideo)
                .object(chunkFileFolderPath+chunkIndex)
                .build();

        //查询分块文件的字节流
        try {
            FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
            if(inputStream!=null){
                //文件已存在
                return RestResponse.success(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestResponse.success(false);
    }

    @Override
    public RestResponse uploadChunk(String fileMD5, int chunkIndex, String localChunkFile) {
        //分块文件的路径
        String chunkFilePath = getChunkFileFolderPath(localChunkFile);
        //获取扩展名
        String mimeType = mediaFileService.getFileMimeType(null);

        Boolean uploadStatus = mediaFileService.UploadMediaFilesToMinIO(localChunkFile,mimeType,bucketVideo,chunkFilePath);
        if (!uploadStatus.equals(true)){
            return RestResponse.error("上传分块文件失败",false);
        }

        return RestResponse.success(true);
    }


    @Transactional
    public RestResponse mergeChunk(Long companyId, String fileMD5, int chunkTotal, UploadFileDTO uploadFileDTO) {
        //=====获取分块文件路径=====
        String chunkFileFolderPath = getChunkFileFolderPath(fileMD5);

        //组成将分块文件路径组成 List<ComposeSource>
        List<ComposeSource> sources = Stream.iterate(0,i -> ++i).limit(chunkTotal)
                .map(i -> ComposeSource.builder().bucket(bucketVideo)
                .object(chunkFileFolderPath.concat(Integer.toString(i)))
                .build()).collect(Collectors.toList());

        //=====合并=====
        //文件名称
        String fileName = uploadFileDTO.getFilename();
        //文件扩展名
        String extName = fileName.substring(fileName.lastIndexOf("."));
        //合并文件路径
        String mergeFilePath = getFilePathByMd5(fileMD5, extName);
        try {
            //合并文件
            ObjectWriteResponse response = minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucketVideo)
                            .object(mergeFilePath)
                            .sources(sources)
                            .build());

            log.debug("合并文件成功:{}",mergeFilePath);
        } catch (Exception e) {
            log.debug("合并文件失败,fileMd5:{},异常:{}",fileMD5,e.getMessage(),e);
            return RestResponse.error("合并文件失败。", false);
        }

        // ====验证md5====
        File minioFile = downloadFileFromMinIO(bucketVideo,mergeFilePath);
        if(minioFile == null){
            log.debug("下载合并后文件失败,mergeFilePath:{}",mergeFilePath);
            return RestResponse.error("下载合并后文件失败。", false);
        }

        try (InputStream newFileInputStream = new FileInputStream(minioFile)) {
            //minio上文件的md5值
            String md5Hex = DigestUtils.md5Hex(newFileInputStream);
            //比较md5值，不一致则说明文件不完整
            if(!fileMD5.equals(md5Hex)){
                return RestResponse.error("文件合并校验失败，最终上传失败。", false);
            }
            //文件大小
            uploadFileDTO.setFileSize(minioFile.length());
        }catch (Exception e){
            log.debug("校验文件失败,fileMd5:{},异常:{}",fileMD5,e.getMessage(),e);
            return RestResponse.error("文件合并校验失败，最终上传失败。", false);
        }finally {
            if(minioFile!=null){
                minioFile.delete();
            }
        }

        //文件入库
        mediaFileService.uploadFileToDB(companyId,fileMD5,uploadFileDTO,bucketVideo,mergeFilePath);
        //=====清除分块文件=====
        clearChunkFiles(chunkFileFolderPath,chunkTotal);
        return RestResponse.success(true);

    }

    //得到分块文件的目录
    public String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + "chunk" + "/";
    }

    //清除分块文件
    public void clearChunkFiles(String chunkFileFolderPath,int chunkTotal){

        try {
            List<DeleteObject> deleteObjects = Stream.iterate(0, i -> ++i)
                    .limit(chunkTotal)
                    .map(i -> new DeleteObject(chunkFileFolderPath.concat(Integer.toString(i))))
                    .collect(Collectors.toList());

            RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder().bucket("video").objects(deleteObjects).build();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);
            results.forEach(r->{
                DeleteError deleteError = null;
                try {
                    deleteError = r.get();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("清楚分块文件失败,objectname:{}",deleteError.objectName(),e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("清楚分块文件失败,chunkFileFolderPath:{}",chunkFileFolderPath,e);
        }
    }

    public String getFilePathByMd5(String fileMd5,String fileExt){
        return   fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/" +fileMd5 +fileExt;
    }

    //从minIO下载分块文件
    public File downloadFileFromMinIO(String bucket, String objectName){
        //临时文件
        File minioFile = null;
        FileOutputStream outputStream = null;
        try{
            InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build());
            //创建临时文件
            minioFile=File.createTempFile("minio", ".merge");
            outputStream = new FileOutputStream(minioFile);
            IOUtils.copy(stream,outputStream);
            return minioFile;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}
