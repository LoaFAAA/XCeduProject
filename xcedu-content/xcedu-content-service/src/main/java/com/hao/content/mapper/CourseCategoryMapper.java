package com.hao.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.content.model.dto.CourseCategoryTreeDto;
import com.hao.content.model.po.CourseCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    public List<CourseCategoryTreeDto> selectTreeNodes(String id);

    //根据分类id查询分类
    public String SelectByCategoryId(String CourseId);

}
