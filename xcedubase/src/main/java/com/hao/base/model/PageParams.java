package com.hao.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 查询参数模型类
 *
 * @author hao
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageParams implements Serializable {
    //当前页数
    @ApiModelProperty("当前页码")
    private Long pageNo = 1L;

    //每页记录默认书
    @ApiModelProperty("每页显示数")
    private Long pageSize = 30L;
}
