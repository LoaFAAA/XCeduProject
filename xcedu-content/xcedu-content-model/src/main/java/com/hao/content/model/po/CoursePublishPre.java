package com.hao.content.model.po;

import com.baomidou.mybatisplus.annotation.*;
import com.hao.content.model.dto.CourseBaseInfoDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("course_publish_pre")
public class CoursePublishPre implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 机构ID
     */
    private Long companyId;

    /**
     * 机构名称
     */
    private String companyName;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 适用人群
     */
    private String users;

    /**
     * 课程标签
     */
    private String tags;

    /**
     * 大分类
     */
    private String mt;

    /**
     * 大分类名称
     */
    private String mtName;

    /**
     * 小分类
     */
    private String st;

    /**
     * 小分类名称
     */
    private String stName;

    /**
     * 课程等级
     */
    private String grade;

    /**
     * 教育模式(common普通，record 录播，live直播等）
     */
    private String teachmode;

    /**
     * 课程介绍
     */
    private String description;

    /**
     * 课程图片
     */
    private String pic;
    //课程营销信息的Json格式数据
    private String market;

    //课程计划的Json格式数据
    private String teachplan;

    //课程相关老师的Json格式数据
    private String teachers;

    /**
     * 提交时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 审核时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime auditDate;

    /**
     * 课程发布状态 未发布  已发布 下线
     */
    private String status;

    /**
     * 收费规则，对应数据字典
     */
    private String charge;

    /**
     * 现价
     */
    private Float price;

    /**
     * 原价
     */
    private Float originalPrice;

    /**
     * 有效期天数
     */
    private Integer validDays;
}
