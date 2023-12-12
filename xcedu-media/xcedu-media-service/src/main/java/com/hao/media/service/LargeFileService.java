package com.hao.media.service;

import com.hao.base.model.RestResponse;
import com.hao.media.model.dto.UploadFileDTO;

import java.io.File;

public interface LargeFileService {

    /**
     * @description 检查文件是否存在
     * @param fileMD5 文件的md5
     * @return com.hao.base.model.RestResponse<java.lang.Boolean> false不存在，true存在
     * @author Hao
     */
    public RestResponse<Boolean> checkFile(String fileMD5);

    /**
     * @description 检查分块是否存在
     * @param fileMD5  文件的md5
     * @param chunkIndex  分块序号
     * @return com.hao.base.model.RestResponse<java.lang.Boolean> false不存在，true存在
     * @author hao
     */
    public RestResponse<Boolean> checkChunk(String fileMD5, int chunkIndex);

    /**
     * @description 分块文件上传接口
     * @param fileMD5  文件的md5
     * @param chunkIndex  分块序号
     * @return com.hao.base.model.RestResponse<java.lang.Boolean> false不存在，true存在
     * @author hao
     */
    public RestResponse uploadChunk(String fileMD5, int chunkIndex, String localChunkFile);

    /**
     * @description 分块文件的合并接口
     * @param companyId 机构ID
     * @param fileMD5  MD5值
     * @param chunkTotal 分块数量
     * @param uploadFileDTO 分块文件信息
     * @return RestResponse
     * @author hao
     */
    public RestResponse mergeChunk(Long companyId, String fileMD5, int chunkTotal, UploadFileDTO uploadFileDTO);

    /**
     * 清除分块文件
     * @param chunkFileFolderPath 分块文件路径
     * @param chunkTotal 分块文件总数
     */
    public void clearChunkFiles(String chunkFileFolderPath,int chunkTotal);

    /**
     * 得到合并后的文件的地址
     * @param fileMd5 文件id即md5值
     * @param fileExt 文件扩展名
     * @return
     */
    public String getFilePathByMd5(String fileMd5,String fileExt);

    /**
     * 从minio下载文件
     * @param bucket 桶
     * @param objectName 对象名称
     * @return 下载后的文件
     */
    public File downloadFileFromMinIO(String bucket, String objectName);


}
