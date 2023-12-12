package com.hao.content.service;

import com.hao.content.model.dto.SaveTeachplanDto;
import com.hao.content.model.dto.TeachplanDto;
import com.hao.content.model.po.TeachplanMedia;
import com.hao.media.model.dto.BindTeachplanMediaDTO;

import java.util.List;

public interface TeachplanService {
    List<TeachplanDto> findTeachplanTree(Long courseId);

    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto);

    void DeleteTeachplanById(Long teachplanId);

    /**
     * 课程计划查询接口
     * @param bindTeachplanMediaDTO
     * @author hao
     */
    void associationMedia(BindTeachplanMediaDTO bindTeachplanMediaDTO);
}
