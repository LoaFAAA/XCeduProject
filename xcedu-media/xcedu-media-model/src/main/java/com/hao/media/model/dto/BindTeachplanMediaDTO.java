package com.hao.media.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="BindTeachplanMediaDto", description="教学计划-媒资绑定提交数据")
public class BindTeachplanMediaDTO implements Serializable {
    //绑定的媒资id
    private String mediaId;

    //文件名称
    private String fileName;

    //绑定的课程计划id
    private Long teachplanId;
}
