package com.hao.content.api;

import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.centent.model.dto.QueryCourseParamsDTO;
import com.hao.centent.model.po.CourseBase;
import com.hao.content.service.CourseBaseInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams,@RequestBody(required = false) QueryCourseParamsDTO queryCourseParamsDTO){
        PageResult<CourseBase> pageResult = courseBaseInfoService.selectPage(pageParams,queryCourseParamsDTO);

        return pageResult;
    }
}
