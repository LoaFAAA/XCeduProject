package com.hao.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface TeachplanMediaMapper extends BaseMapper<com.hao.content.model.po.TeachplanMedia> {

    int deleteByCourseId(Long TeachplanId);
}
