package com.hao.content.service;

import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.centent.model.dto.QueryCourseParamsDTO;
import com.hao.centent.model.po.CourseBase;

public interface CourseBaseInfoService {
    public PageResult<CourseBase> selectPage(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDTO);
}
