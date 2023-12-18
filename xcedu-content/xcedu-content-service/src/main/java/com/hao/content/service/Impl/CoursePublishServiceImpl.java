package com.hao.content.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hao.base.Constant.CourseAuditStatus;
import com.hao.base.Constant.CourseReleaseStatus;
import com.hao.base.exception.XCException;
import com.hao.content.mapper.*;
import com.hao.content.model.dto.CourseBaseInfoDto;
import com.hao.content.model.dto.TeachplanDto;
import com.hao.content.model.po.*;
import com.hao.content.model.vo.CoursePreviewVO;
import com.hao.content.service.CoursePublishService;
import com.hao.content.service.TeachplanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CoursePublishServiceImpl implements CoursePublishService {
    @Autowired
    private CourseBaseInfoMapper courseBaseInfoMapper;
    @Autowired
    private CourseMarketMapper courseMarketMapper;
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;
    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;
    @Autowired
    private CoursePublishMapper coursePublishMapper;
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private TeachplanService teachplanService;

    /**
     * 根据id获取课程预览信息
     * @param courseId
     * @author hao
     */
    public CoursePreviewVO getCoursePreviewInfo(Long courseId) {
        //对课程id进行判空
        if (courseId == null){
            throw new XCException("未获取到课程Id");
        }

        //根据id获取课程信息
        CourseBase courseBase = courseBaseInfoMapper.selectById(courseId);
        if (courseBase == null){
            throw new XCException("未获取到课程信息");
        }

        //根据id获取课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        if (courseMarket == null){
            throw new XCException("未获取到课程营销信息");
        }

        //获取课程的大分类小分类
        String mtName = courseCategoryMapper.SelectByCategoryId(courseBase.getMt());
        String stName = courseCategoryMapper.SelectByCategoryId(courseBase.getSt());

        //组装courseBaseInfoDto
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        courseBaseInfoDto.setMtName(mtName);
        courseBaseInfoDto.setStName(stName);
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);
        BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);

        //组装teachplanList
        List<TeachplanDto> teachplanDtoList = teachplanService.findTeachplanTree(courseId);
        if (teachplanDtoList == null){
            throw new XCException("未获取课程计划信息");
        }

        //组装coursePreviewVO
        CoursePreviewVO coursePreviewVO = new CoursePreviewVO();
        coursePreviewVO.setCourseBase(courseBaseInfoDto);
        coursePreviewVO.setTeachplanList(teachplanDtoList);

        return coursePreviewVO;
    }

    /**
     * 根据id提交课程
     * @param courseId
     * @param companyId
     * @author hao
     */
    @Transactional
    public void commitAudit(Long companyId, Long courseId) {
        if (courseId == null){
            throw new XCException("未获取到课程id");
        }

        //根据id获取课程信息并判断审批状态
        CourseBase courseBase = courseBaseInfoMapper.selectById(courseId);
        if (courseBase == null && courseBase.getCompanyId().equals(companyId)){
            throw new XCException("未获取到课程信息");
        }
        if (courseBase.getAuditStatus().equals(CourseAuditStatus.SubmittedAudit) && StringUtils.isEmpty(courseBase.getPic())){
            throw new XCException("课程未处于可审批状态");
        }

        //根据id获取课程营销信息并设置为json格式
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        if (courseMarket == null){
            throw new XCException("未获取到课程营销信息");
        }
        String courseMarketJson = JSON.toJSONString(courseMarket);

        //根据id获取课程计划并设置为json格式
        List<TeachplanDto> teachplanDtoList = teachplanService.findTeachplanTree(courseId);
        if (teachplanDtoList.size() <= 0){
            throw new XCException("未获取到课程计划信息");
        }
        String teachplanJson = JSON.toJSONString(teachplanDtoList);

        //获取课程的大分类小分类
        String mtName = courseCategoryMapper.SelectByCategoryId(courseBase.getMt());
        String stName = courseCategoryMapper.SelectByCategoryId(courseBase.getSt());

        //组装coursePublishPre
        CoursePublishPre coursePublishPre = new CoursePublishPre();
        courseBase.setAuditStatus(CourseAuditStatus.SubmittedAudit);
        BeanUtils.copyProperties(courseBase,coursePublishPre);
        coursePublishPre.setCharge(courseMarket.getCharge());
        coursePublishPre.setPrice(courseMarket.getPrice());
        coursePublishPre.setOriginalPrice(courseMarket.getOriginalPrice());
        coursePublishPre.setValidDays(courseMarket.getValidDays());
        coursePublishPre.setMarket(courseMarketJson);
        coursePublishPre.setTeachplan(teachplanJson);
        coursePublishPre.setMtName(mtName);
        coursePublishPre.setStName(stName);


        //改变课程信息表
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseBase::getId,courseId);
        int count = courseBaseInfoMapper.update(courseBase,queryWrapper);
        if (count != 1) {
            throw new XCException("审批未成功改变课程审批状态");
        }

        //插入课程预览表
        count += coursePublishPreMapper.insert(coursePublishPre);
        if (count != 2) {
            throw new XCException("未成功插入课程预审批表");
        }
    }

    /**
     * 根据id发布课程
     * @param courseId
     * @param companyId
     * @author hao
     */
    @Transactional
    public void coursePublish(Long companyId, Long courseId) {
        CoursePublishPre coursePublishPre = coursePublishPreMapper.selectById(courseId);

        if (coursePublishPre == null && coursePublishPre.getCompanyId().equals(companyId)){
            throw new XCException("未获得所需审核课程信息");
        }

        //组装发布课程信息
        CoursePublish coursePublish = new CoursePublish();
        BeanUtils.copyProperties(coursePublishPre,coursePublish);
        coursePublish.setOnlineDate(LocalDateTime.now());
        coursePublish.setStatus(CourseReleaseStatus.已发布);

        int count = coursePublishMapper.insert(coursePublish);
        if (count != 1){
            throw new XCException("课程发布失败");
        }

        //修改课程信息为已发布
        CourseBase courseBase = courseBaseInfoMapper.selectById(courseId);
        if (courseBase == null && !courseBase.getAuditStatus().equals(CourseAuditStatus.PassedAudit)){
            throw new XCException("未查到已通过审核的课程信息");
        }
        courseBase.setAuditStatus(CourseAuditStatus.PassedAudit);
        courseBase.setStatus(CourseReleaseStatus.已发布);
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseBase::getId,courseId);

        count += courseBaseInfoMapper.update(courseBase,queryWrapper);
        if (count != 2){
            throw new XCException("课程未成功修改为已发布");
        }

        //删除预发布表内容
        count += coursePublishPreMapper.deleteById(courseId);
        if (count != 3){
            throw new XCException("未成功删除预发布表内容");
        }
    }
}
