package com.hao.content.model.dto;

import com.hao.content.model.po.CourseCategory;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class CourseCategoryTreeDTO extends CourseCategory {
    //子节点
    private List<CourseCategoryTreeDTO> childrenTreeNode;
}
