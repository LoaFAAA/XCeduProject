package com.hao.content.api;

import com.hao.content.model.dto.SaveTeachplanDto;
import com.hao.content.model.dto.TeachplanDto;
import com.hao.content.service.TeachplanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hao
 * @description 课程计划管理相关的接口
 */
@Api(value = "课程计划编辑接口",tags = "课程计划编辑接口")
@RestController
public class TeachPlanController {
    @Autowired
    private TeachplanService teachplanService;

    /**
     * 课程计划查询接口
     * @param courseId
     * @author hao
     */
    @ApiOperation("查询课程计划树形结构")
    //查询课程计划  GET /teachplan/22/tree-nodes
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        List<TeachplanDto> teachplanTree = teachplanService.findTeachplanTree(courseId);

        return teachplanTree;
    }

    /**
     * 课程计划新增接口
     * @param saveTeachplanDto
     * @author hao
     */
    @ApiOperation("课程计划新增/修改接口")
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto saveTeachplanDto){

    }
}
