package com.hao.content.model.vo;

import com.hao.content.model.dto.CourseBaseInfoDto;
import com.hao.content.model.dto.TeachplanDto;
import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.io.Serializable;
import java.util.List;

@Data
public class CoursePreviewVO implements Serializable {
    private CourseBaseInfoDto courseBase;

    List<TeachplanDto> teachplanList;
}
