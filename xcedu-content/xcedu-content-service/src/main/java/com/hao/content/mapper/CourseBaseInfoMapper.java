package com.hao.content.mapper;

//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.Page;
import com.hao.content.model.dto.QueryCourseParamsDTO;
import com.hao.content.model.po.CourseBase;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseBaseInfoMapper {

    CourseBase selectById(Long id);

    Page<CourseBase> selectPage(QueryCourseParamsDTO queryCourseParamsDTO);



}
