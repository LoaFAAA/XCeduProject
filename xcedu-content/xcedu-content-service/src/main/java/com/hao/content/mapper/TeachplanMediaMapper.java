package com.hao.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.content.model.po.TeachplanMedia;

public interface TeachplanMediaMapper extends BaseMapper<TeachplanMedia> {

    int deleteByCourseId(Long TeachplanId);
}
