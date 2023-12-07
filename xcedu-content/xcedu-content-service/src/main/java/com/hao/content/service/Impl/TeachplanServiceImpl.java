package com.hao.content.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.base.exception.XCException;
import com.hao.content.mapper.TeachplanMapper;
import com.hao.content.mapper.TeachplanMediaMapper;
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
    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    /**
     * 根据id查询课程计划接口
     * @param courseId
     * @author hao
     */
    @Transactional
    @Override
    public List<TeachplanDto> findTeachplanTree(Long courseId) {
        if (courseId == null){
            throw new XCException("查询课程id为空");
        }

        List<TeachplanDto> teachplanDtoList = teachplanMapper.selectTreeNodes(courseId);

        return teachplanDtoList;
    }

    /**
     * 新增或修改课程计划接口
     * @param saveTeachplanDto
     * @author hao
     */
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

    /**
     * 根据id删除课程计划接口
     * @param courseId
     * @author hao
     */
    @Transactional
    @Override
    public void DeleteTeachplanById(Long courseId) {
        Teachplan Deleteteachplan = teachplanMapper.selectById(courseId);

        if (Deleteteachplan == null){
            throw new XCException("删除失败，不存在该id课程计划");
        }

        int count = teachplanMapper.DeleteTeachplanById(courseId);
        if (count != 1){
            throw new XCException("删除课程计划失败！");
        }

        count += teachplanMediaMapper.deleteByCourseId(Deleteteachplan.getId());
        if (count != 2){
            throw new XCException("删除课程计划的视频失败！");
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
