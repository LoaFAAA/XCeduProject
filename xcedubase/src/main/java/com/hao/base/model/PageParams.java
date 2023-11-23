package com.hao.base.model;

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
    private Long pageNo = 1L;

    //每页记录默认书
    private Long pageSize = 30L;
}
