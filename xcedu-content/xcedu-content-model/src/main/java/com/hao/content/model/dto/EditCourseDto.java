package com.hao.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value="EditCourseDto", description="修改课程基本信息")
@ToString(callSuper = true)
public class EditCourseDto extends AddCourseDto{

    @ApiModelProperty(value = "课程id", required = true)
    private Long id;
}
