package com.hao.centent.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 课程查询条件模型类
 *
 * @author hao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryCourseParamsDTO {

    //审核状态
    private String auditStatus;

    //课程名称
    private String courseName;

    //发布状态
    private String publicStatus;
}
