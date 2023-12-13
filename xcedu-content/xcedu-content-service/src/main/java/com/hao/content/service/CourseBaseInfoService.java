package com.hao.content.service;

import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.content.model.dto.*;
import com.hao.content.model.po.CourseBase;
import com.hao.content.model.po.Teacher;
import com.hao.content.model.vo.TeacherVO;

public interface CourseBaseInfoService {
    public PageResult<CourseBase> selectPage(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDTO);

    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);

    public CourseBaseInfoDto getCourseBaseById(Long CourseId);

    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto);

    /**
     * @description 根据课程id查询课程教师接口
     * @param courseId 课程id
     * @return com.hao.content.model.po.Teacher
     * @author Hao
     */
    Teacher GetTeacherByCourseId(Long courseId);

    /**
     * @description 新增或修改课程教师接口
     * @param  teacherDTO
     * @return com.hao.content.model.vo.TeacherVO
     * @author Hao
     */
    TeacherVO saveTeacher(TeacherDTO teacherDTO);

    /**
     * @description 新增或修改课程教师接口
     * @param  courseId
     * @param  id
     * @author Hao
     */
    void DeleteTeacherByid(Long courseId, Long id);
}
