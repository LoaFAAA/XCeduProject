package com.hao.content.service.Impl;

//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.content.mapper.CourseCategoryMapper;
import com.hao.content.mapper.CourseMarketMapper;
import com.hao.content.model.dto.AddCourseDto;
import com.hao.content.model.dto.CourseBaseInfoDto;
import com.hao.content.model.dto.EditCourseDto;
import com.hao.content.model.dto.QueryCourseParamsDTO;
import com.hao.content.model.po.CourseBase;
import com.hao.content.mapper.CourseBaseInfoMapper;
import com.hao.content.model.po.CourseCategory;
import com.hao.content.model.po.CourseMarket;
import com.hao.content.service.CourseBaseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {
    @Autowired
    private CourseBaseInfoMapper courseBaseInfoMapper;
    @Autowired
    private CourseMarketMapper courseMarketMapper;
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    public PageResult<CourseBase> selectPage(PageParams pageParams, QueryCourseParamsDTO queryCourseParamsDTO) {
        PageHelper.startPage(pageParams.getPageNo().intValue(),pageParams.getPageSize().intValue());
        Page<CourseBase> page = courseBaseInfoMapper.selectPage(queryCourseParamsDTO);

        PageResult<CourseBase> pageResult = new PageResult();
        List<CourseBase> courseBaseList = page.getResult();
        pageResult.setItems(courseBaseList);
        Long count = Long.valueOf(courseBaseList.size());
        pageResult.setCounts(count);
        pageResult.setPage(pageParams.getPageNo());
        pageResult.setPageSize(pageParams.getPageSize());

        return pageResult;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {
        //参数的合法性校验
        if (StringUtils.isBlank(addCourseDto.getName())) {
            throw new RuntimeException("课程名称为空");

        }

        if (StringUtils.isBlank(addCourseDto.getMt())) {
            throw new RuntimeException("课程分类为空");
        }

        if (StringUtils.isBlank(addCourseDto.getSt())) {
            throw new RuntimeException("课程分类为空");
        }

        if (StringUtils.isBlank(addCourseDto.getGrade())) {
            throw new RuntimeException("课程等级为空");
        }

        if (StringUtils.isBlank(addCourseDto.getTeachmode())) {
            throw new RuntimeException("教育模式为空");
        }

        if (StringUtils.isBlank(addCourseDto.getUsers())) {
            throw new RuntimeException("适应人群为空");
        }

        if (StringUtils.isBlank(addCourseDto.getCharge())) {
            throw new RuntimeException("收费规则为空");
        }

        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDto,courseBase);
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        courseBase.setAuditStatus("202002");
        courseBase.setStatus("203001");
        int count = courseBaseInfoMapper.insert(courseBase);
        if (count <= 0){
            throw new RuntimeException("添加课程失败");
        }

        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto,courseMarket);
        Long courseId = courseBase.getId();
        courseMarket.setId(courseId);
//        saveCourseMarket(courseMarket);
        return null;
    }

    /**
     * 课程修改接口
     * @author hao
     */
    @Transactional
    public CourseBaseInfoDto getCourseBaseById(Long courseId) {
        CourseBase courseBase = new CourseBase();
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        courseBase = courseBaseInfoMapper.selectById(courseId);
        if (courseBase == null){
            throw new RuntimeException("查询结果为空");
        }
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);

        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        if (courseMarket == null){
            throw new RuntimeException("查询结果为空");
        }
        BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);

        CourseCategory courseCategory = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategory.getName());
        CourseCategory courseCategory1 = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategory1.getName());

        return courseBaseInfoDto;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        Long courseId = editCourseDto.getId();
        CourseBase courseBase = courseBaseInfoMapper.selectById(courseId);
        if (courseBase == null){
            throw new RuntimeException("课程不存在");
        }

        if (!courseBase.getCompanyId().equals(companyId)){
            throw new RuntimeException("本机构只能修改本机构的课程");
        }

        BeanUtils.copyProperties(editCourseDto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        Integer count = courseBaseInfoMapper.updateById(courseBase);

        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        if (courseMarket == null){
            throw new RuntimeException("课程信息不存在");
        }
        BeanUtils.copyProperties(editCourseDto,courseMarket);
        count += courseMarketMapper.updateById(courseMarket);

        if (!count.equals(2)){
            throw new RuntimeException("操作异常！");
        }

        CourseBaseInfoDto courseBaseInfoDto = this.getCourseBaseById(courseId);
        return courseBaseInfoDto;
    }


//    public int saveCourseMarket(CourseMarket courseMarket){
//        String charge = courseMarket.getCharge();
//        if(StringUtils.isEmpty(charge)){
//            throw new RuntimeException("收费规则为空");
//        }
//
//        if(charge.equals("201001")){
//            if(courseMarket.getPrice() == null || courseMarket.getPrice().floatValue() <= 0){
//                throw new RuntimeException("课程的价格不能为空并且必须大于0");
//            }
//        }
//
//        Long id = courseMarket.getId();
//
//
//    }
}
