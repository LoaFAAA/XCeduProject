package com.hao.content.api;

import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.centent.model.dto.QueryCourseParamsDTO;
import com.hao.centent.model.po.CourseBase;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseBaseInfoController {

    /**
     * 课程查询接口
     *
     * @author hao
     */
    @RequestMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams,@RequestBody(required = false) QueryCourseParamsDTO queryCourseParamsDTO){

        return null;
    }
}
