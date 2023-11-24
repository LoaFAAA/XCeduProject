package com.hao.content.service.Impl;

//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.content.model.dto.QueryCourseParamsDTO;
import com.hao.content.model.po.CourseBase;
import com.hao.content.mapper.CourseBaseInfoMapper;
import com.hao.content.service.CourseBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    private CourseBaseInfoMapper courseBaseInfoMapper;

    public PageResult<CourseBase> selectPage(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDTO) {
        PageHelper.startPage(pageParams.getPageNo().intValue(),pageParams.getPageSize().intValue());
        Page<CourseBase> page = courseBaseInfoMapper.selectPage(queryCourseParamsDTO);

        PageResult<CourseBase> pageResult = new PageResult();
        List<CourseBase> courseBaseList = page.getResult();
        pageResult.setItems(courseBaseList);
        Long count = Long.valueOf(courseBaseList.size());
        pageResult.setCounts(count);
        pageResult.setPage(pageParams.getPageNo());
        pageResult.setPageSize(pageParams.getPageSize());

        return pageResult;
    }
}
