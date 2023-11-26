package com.hao.content.model.dto;

import com.hao.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable{
    //子节点
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
