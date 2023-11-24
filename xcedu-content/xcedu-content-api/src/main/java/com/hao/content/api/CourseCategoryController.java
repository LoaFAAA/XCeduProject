package com.hao.content.api;

import com.hao.content.model.dto.CourseCategoryTreeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDTO> queryTreeNodes(){

        return null;
    }
}
