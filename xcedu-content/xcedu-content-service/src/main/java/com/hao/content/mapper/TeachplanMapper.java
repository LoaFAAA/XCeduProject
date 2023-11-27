package com.hao.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.content.model.dto.TeachplanDto;
import com.hao.content.model.po.Teachplan;

import java.util.List;

public interface TeachplanMapper extends BaseMapper<Teachplan> {

    //课程计划查询
    public List<TeachplanDto> selectTreeNodes(Long courseId);
}
