package com.hao.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.media.mapper.MediaFileMapper;
import com.hao.media.mapper.MediaProcessHistoryMapper;
import com.hao.media.mapper.MediaProcessMapper;
import com.hao.media.model.po.MediaFiles;
import com.hao.media.model.po.MediaProcess;
import com.hao.media.model.po.MediaProcessHistory;
import com.hao.media.service.MediaFileService;
import com.hao.media.service.MediaProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MediaProcessServiceImpl implements MediaProcessService {
    @Autowired
    MediaProcessMapper mediaProcessMapper;
    @Autowired
    MediaFileMapper mediaFileMapper;
    @Autowired
    MediaProcessHistoryMapper mediaProcessHistoryMapper;

    public List<MediaProcess> getMediaProcessList(Integer shareTotal, Integer shareIndex, Integer count) {
        List<MediaProcess> mediaProcessList = mediaProcessMapper.selectListByShardIndex(shareTotal,shareTotal,count);
        return null;
    }

    @Override
    public Boolean acpuireTask(Long id) {
        int result = mediaProcessMapper.acpuireTask(id);

        return result <= 0 ? false : true;
    }

    @Transactional
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg) {
        //查询任务是否存在
        MediaProcess mediaProcess = mediaProcessMapper.selectById(taskId);

        if (mediaProcess == null){
            return;
        }

        //处理失败，更新任务处理结果
        LambdaQueryWrapper<MediaProcess> queryWrapperById = new LambdaQueryWrapper<MediaProcess>().eq(MediaProcess::getId, taskId);

        //如果任务执行失败
        if (status.equals("3")){
            mediaProcess.setStatus("3");
            mediaProcess.setErrormsg(errorMsg);
            mediaProcess.setFailCount(mediaProcess.getFailCount()+1);
            mediaProcessMapper.update(mediaProcess,queryWrapperById);
            log.debug("更新任务处理状态为失败，任务信息:{}",mediaProcess);
            return;
        }

        //任务执行成功
        MediaFiles mediaFiles = mediaFileMapper.selectById(fileId);
        if (mediaFiles != null){
            mediaFiles.setUrl(url);
            mediaFileMapper.updateById(mediaFiles);
        }

        //先更新media_Process表数据
        mediaProcess.setUrl(url);
        mediaProcess.setFinishDate(LocalDateTime.now());
        mediaProcess.setStatus("2");
        mediaProcessMapper.updateById(mediaProcess);

        //插入media_Process_History表
        MediaProcessHistory mediaProcessHistory = new MediaProcessHistory();
        BeanUtils.copyProperties(mediaFiles,mediaProcessHistory);
        mediaProcessHistoryMapper.insert(mediaProcessHistory);

        //将media_Process表已执行成功数据删除
        mediaProcessMapper.deleteById(mediaProcess.getId());
    }
}
