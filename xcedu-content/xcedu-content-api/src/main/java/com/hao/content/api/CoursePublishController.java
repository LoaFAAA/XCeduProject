package com.hao.content.api;

import com.hao.content.model.vo.CoursePreviewVO;
import com.hao.content.service.CoursePublishService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
public class CoursePublishController {
    @Autowired
    private CoursePublishService coursePublishService;

    @ResponseBody
    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId){
        CoursePreviewVO coursePreviewVO = coursePublishService.getCoursePreviewInfo(courseId);

        //组装模版引擎
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("model",coursePreviewVO);
            modelAndView.setViewName("course_template");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return modelAndView;
        }
    }

    /**
     * @description 课程进行审核
     * @author hao
     * @version 1.0
     */
    @ResponseBody
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable("courseId") Long courseId){
        //暂时写死
        Long companyId = 1232141425L;

        coursePublishService.commitAudit(companyId,courseId);

        return;
    }

    /**
     * @description 课程预览，发布
     * @author hao
     * @version 1.0
     */
    @ApiOperation("课程发布")
    @ResponseBody
    @PostMapping ("/coursepublish/{courseId}")
    public void coursePublish(@PathVariable("courseId") Long courseId){
        //暂时写死
        Long companyId = 1232141425L;
        coursePublishService.coursePublish(companyId,courseId);

        return;
    }

}
