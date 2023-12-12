package com.hao.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.media.model.po.MediaProcess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MediaProcessMapper extends BaseMapper<MediaProcess> {

    List<MediaProcess> selectListByShardIndex(@Param("ShardTotal") Integer ShardTotal,
                                              @Param("ShardIndex") Integer ShardIndex,
                                              @Param("count") Integer count);

    /**
     * 开启任务
     * param id 任务id
     */
    @Update("update media_process set status = '4' where (status = '1' and status = '3') and id = #{id}")
    int acpuireTask(@Param("id") Long id);
}
