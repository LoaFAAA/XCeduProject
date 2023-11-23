package com.hao.base.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * 查询结果模型类
 *
 * @author hao
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {
    //查询结果
    private List<T> items;

    //总记录数
    private Long counts;

    //当前页码
    private Long page;

    //每页记录数
    private Long pageSize;
}
