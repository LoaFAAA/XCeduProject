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
  * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
  * @author Mr.M
  * @date 2022/9/10 8:57
 */
 public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

 /**
  * 上传文件
  * @param companyId 机构id
  * @param uploadfileDTO 文件信息
  * @param localFilePath 文件本地路径
  * @return UploadFileResultDto
  */
 public UploadFileResultVO uploadFile(Long companyId, UploadFileDTO uploadfileDTO, String localFilePath);

 public MediaFiles uploadFileToDB(Long companyId,String fileMd5,UploadFileDTO uploadfileDTO,String bucket,String objectName);
}
