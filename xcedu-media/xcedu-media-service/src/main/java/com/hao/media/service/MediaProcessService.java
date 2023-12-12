package com.hao.media.service;

import com.hao.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MediaProcessService {

    List<MediaProcess> getMediaProcessList(Integer shareTotal,Integer shareIndex,Integer count);

    /**
     * @description 尝试获取锁
     * @param id  任务id
     * @return boolean 是否获取到锁
     * @author hao
     */
    Boolean acpuireTask(@Param("id") Long id);

    /**
     * @description 保存任务结果
     * @param taskId  任务id
     * @param status 任务状态
     * @param fileId  文件id
     * @param url url
     * @param errorMsg 错误信息
     * @return void
     * @author hao
     */
    void saveProcessFinishStatus(Long taskId,String status,String fileId,String url,String errorMsg);

}
