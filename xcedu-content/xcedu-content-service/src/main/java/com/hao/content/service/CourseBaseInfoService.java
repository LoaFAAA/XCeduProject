package com.hao.content.service;

import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.content.model.dto.AddCourseDto;
import com.hao.content.model.dto.CourseBaseInfoDto;
import com.hao.content.model.dto.QueryCourseParamsDTO;
import com.hao.content.model.po.CourseBase;

public interface CourseBaseInfoService {
    public PageResult<CourseBase> selectPage(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDTO);

    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);
}
