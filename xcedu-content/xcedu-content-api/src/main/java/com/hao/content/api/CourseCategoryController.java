package com.hao.content.api;

import com.hao.base.exception.XCException;
import com.hao.content.model.dto.AddCourseDto;
import com.hao.content.model.dto.CourseBaseInfoDto;
import com.hao.content.model.dto.CourseCategoryTreeDto;
import com.hao.content.service.CourseBaseInfoService;
import com.hao.content.service.CourseCategoryService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据字典 前端控制器
 *
 * @author hao
 */

@Slf4j
@RestController
public class CourseCategoryController {
    //定义课程分类的根节点,默认为1
    public static String CourseRootNode = "1";
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;
    @Autowired
    private CourseCategoryService courseCategoryService;
    @Autowired

    /**
     * 课程分类查询接口
     *
     * @author hao
     */
    @ApiOperation("课程查询接口")
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes(){

        return courseCategoryService.queryTreeNodes(CourseRootNode);
    }

    /**
     * 新增课程接口
     *
     * @author hao
     */
    @ApiModelProperty("新增课程")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody AddCourseDto addCourseDto){
        //暂时写死
        Long companyId = 1232141425L;

        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.createCourseBase(companyId,addCourseDto);
        if (courseBaseInfoDto == null){
            throw new XCException("新增课程获取返回值为空");
        }

        return courseBaseInfoDto;
    }
}
