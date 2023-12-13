package com.hao.content.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 教师实体类
 * </p>
 *
 * @author hao
 */
@Data
@TableName("course_teacher")
public class Teacher {
    private static final long serialVersionUID = 1L;

    //教师id
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    //课程id
    private Long courseId;

    //教师名称
    private String teacherName;

    //教师职称
    private String position;

    //教师简介
    private String introduction;

    //教师照片
    private String photograph;

    //创建时间
    private LocalDateTime createDate;
}
