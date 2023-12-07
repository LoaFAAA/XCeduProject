package com.hao.content.api;

import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.content.model.dto.CourseBaseInfoDto;
import com.hao.content.model.dto.EditCourseDto;
import com.hao.content.model.dto.QueryCourseParamsDTO;
import com.hao.content.model.po.CourseBase;
import com.hao.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "课程信息管理接口",tags = "课程信息管理接口")
@RestController
public class CourseBaseInfoController {
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    /**
     * 课程查询接口
     *
     * @author hao
     */
    @ApiOperation("查询分页课程接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDTO queryCourseParamsDTO){
        PageResult<CourseBase> pageResult = courseBaseInfoService.selectPage(pageParams,queryCourseParamsDTO);

        return pageResult;
    }

    /**
     * 根据id查询课程接口
     * @param courseId
     * @author hao
     */
    @ApiOperation("课程查询接口")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable("courseId") Long courseId){
        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.getCourseBaseById(courseId);

        return courseBaseInfoDto;
    }

    /**
     * 课程修改接口
     * @param editCourseDto
     * @author hao
     */
    @ApiOperation("课程修改接口")
    @PutMapping("/course")
    public CourseBaseInfoDto EditCourseBase(@RequestBody EditCourseDto editCourseDto){
        Long companyId = 1232141425L;

        return courseBaseInfoService.updateCourseBase(companyId,editCourseDto);
    }
}
