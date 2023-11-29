package com.hao.content.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.content.mapper.TeachplanMapper;
import com.hao.content.model.dto.SaveTeachplanDto;
import com.hao.content.model.dto.TeachplanDto;
import com.hao.content.model.po.Teachplan;
import com.hao.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeachplanServiceImpl implements TeachplanService {
    @Autowired
    private TeachplanMapper teachplanMapper;

    @Transactional
    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        if (courseId == null){
            throw new RuntimeException("查询课程id为空");
        }

        List<TeachplanDto> teachplanDtoList = teachplanMapper.selectTreeNodes(courseId);

        return teachplanDtoList;
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto saveTeachplanDto) {
        if (saveTeachplanDto == null){
            throw new RuntimeException("获得的课程计划为空");
        }

        if (saveTeachplanDto.getId() == null){
            int count = getTeachplanCount(saveTeachplanDto.getCourseId(),saveTeachplanDto.getParentid());
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachplanDto,teachplan);
            teachplan.setOrderby(count + 1);
            teachplanMapper.insert(teachplan);
        }else {
            Teachplan teachplan1 = teachplanMapper.selectById(saveTeachplanDto.getId());
            BeanUtils.copyProperties(saveTeachplanDto,teachplan1);
            teachplanMapper.updateById(teachplan1);
        }
    }

    private int getTeachplanCount(long courseId,long parentId){
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId,courseId);
        queryWrapper.eq(Teachplan::getParentid,parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }

}
