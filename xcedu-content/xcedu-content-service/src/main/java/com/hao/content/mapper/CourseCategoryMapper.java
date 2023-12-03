package com.hao.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.content.model.dto.CourseCategoryTreeDto;
import com.hao.content.model.po.CourseCategory;

import java.util.List;


public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    public List<CourseCategoryTreeDto> selectTreeNodes(String id);

    public String SelectByCourseId(String CourseId);

}
