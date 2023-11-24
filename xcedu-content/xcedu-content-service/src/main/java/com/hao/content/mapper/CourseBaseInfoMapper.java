package com.hao.content.mapper;

//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.Page;
import com.hao.base.model.PageParams;
import com.hao.centent.model.dto.QueryCourseParamsDTO;
import com.hao.centent.model.po.CourseBase;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseBaseInfoMapper {

    CourseBase selectById(Long id);

    Page<CourseBase> selectPage(QueryCourseParamsDTO queryCourseParamsDTO);

}
