package com.hao.media.service;

import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.media.model.dto.QueryMediaParamsDto;
import com.hao.media.model.dto.UploadFileDTO;
import com.hao.media.model.po.MediaFiles;
import com.hao.media.model.vo.UploadFileResultVO;

/**
 * @description 媒资文件管理业务类
 * @author Mr.M
 * @date 2022/9/10 8:55
 * @version 1.0
 */
public interface MediaFileService {

 /**
  * @description 媒资文件查询方法
  * @param pageParams 分页参数
  * @param queryMediaParamsDto 查询条件
  * @return com.hao.base.model.PageResult<com.hao.media.model.po.MediaFiles>
 */
 public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

 /**
  * 上传文件
  * @param companyId 机构id
  * @param uploadfileDTO 文件信息
  * @param localFilePath 文件本地路径
  * @return UploadFileResultVO
  */
 public UploadFileResultVO uploadFile(Long companyId, UploadFileDTO uploadfileDTO, String localFilePath);

 public MediaFiles uploadFileToDB(Long companyId,String fileMd5,UploadFileDTO uploadfileDTO,String bucket,String objectName);

 /**
  * 上传文件到minIO
  * @param localFilePath 本地路径
  * @param mimeType 文件类型
  * @param bucketName 桶名称
  * @param objectName 文件本地名称
  * @return boolean
  */
 public boolean UploadMediaFilesToMinIO(String localFilePath,String mimeType,String bucketName, String objectName);

 /**
  * 获取文件的扩展名
  * @param extension 本地路径
  * @return String
  */
 public String getFileMimeType(String extension);

 /**
  * 获取文件默认存储目录路径 年/月/日
  * @return String
  */
 public String getDefaultFolderPath();
 }
