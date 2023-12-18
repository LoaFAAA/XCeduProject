package com.hao.content.service.Impl;

//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hao.base.Constant.CourseAuditStatus;
import com.hao.base.Constant.CourseReleaseStatus;
import com.hao.base.exception.XCException;
import com.hao.base.model.PageParams;
import com.hao.base.model.PageResult;
import com.hao.content.mapper.CourseCategoryMapper;
import com.hao.content.mapper.CourseMarketMapper;
import com.hao.content.mapper.TeacherMapper;
import com.hao.content.model.dto.*;
import com.hao.content.model.po.*;
import com.hao.content.mapper.CourseBaseInfoMapper;
import com.hao.content.model.vo.TeacherVO;
import com.hao.content.service.CourseBaseInfoService;
import com.hao.content.service.CourseMarketService;
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
    @Autowired
    private CourseMarketService courseMarketService;
    @Autowired
    private TeacherMapper teacherMedia;

    /**
     * 课程分页查询接口
     * @author hao
     */
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

    /**
     * 创建课程接口
     * @author hao
     */
    @Transactional
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto) {
        //参数的合法性校验
        if (StringUtils.isBlank(addCourseDto.getName())) {
            throw new XCException("课程名称为空");
        }
        if (StringUtils.isBlank(addCourseDto.getMt())) {
            throw new XCException("课程大分类为空");
        }
        if (StringUtils.isBlank(addCourseDto.getSt())) {
            throw new XCException("课程小分类为空");
        }
        if (StringUtils.isBlank(addCourseDto.getGrade())) {
            throw new XCException("课程等级为空");
        }
        if (StringUtils.isBlank(addCourseDto.getTeachmode())) {
            throw new XCException("教育模式为空");
        }

        //组装课程信息并写入course_base数据库
        CourseBase courseBase = new CourseBase();
        BeanUtils.copyProperties(addCourseDto,courseBase);
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        courseBase.setChangeDate(LocalDateTime.now());
        courseBase.setAuditStatus(CourseAuditStatus.NotSubmittedAudit);
        courseBase.setStatus(CourseReleaseStatus.未发布);
        int count = courseBaseInfoMapper.insert(courseBase);

        //判断插入课程操作是否成功
        if (count <= 0){
            throw new RuntimeException("添加课程失败");
        }

        //组装课程营销信息并写入course_market数据库
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(addCourseDto,courseMarket);
        Long courseId = courseBase.getId();
        courseMarket.setId(courseId);

        //判断插入操作是否失败
        count += courseMarketService.CreateMarket(courseMarket);
        if (count != 2){
            throw new RuntimeException("插入课程营销表失败");
        }

        //查询本次插入数据，并进行判空
        CourseMarket RcourseMarket = courseMarketMapper.selectById(courseMarket.getId());
        CourseBase RcourseBase = courseBaseInfoMapper.selectById(courseBase.getId());
        if (RcourseMarket == null){
            throw new XCException("未查询到本次插入的课程营销信息");
        }
        if (RcourseBase == null){
            throw new XCException("未查询到本次插入的课程信息");
        }

        //进行组装
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(RcourseBase,courseBaseInfoDto);
        BeanUtils.copyProperties(RcourseMarket,courseBaseInfoDto);

        //查询分类名称
        courseBaseInfoDto.setMtName(courseCategoryMapper.SelectByCategoryId(courseBase.getMt()));
        courseBaseInfoDto.setStName(courseCategoryMapper.SelectByCategoryId(courseBase.getSt()));

        return courseBaseInfoDto;
    }

    /**
     * 根据id查询课程接口
     * @author hao
     */
    @Transactional
    public CourseBaseInfoDto getCourseBaseById(Long courseId) {
        CourseBase courseBase = new CourseBase();
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        courseBase = courseBaseInfoMapper.selectById(courseId);
        if (courseBase == null){
            throw new XCException("查询课程信息结果为空");
        }
        BeanUtils.copyProperties(courseBase,courseBaseInfoDto);

        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        if (courseMarket == null){
            throw new RuntimeException("查询课程营销结果为空");
        }
        BeanUtils.copyProperties(courseMarket,courseBaseInfoDto);

        CourseCategory courseCategory = courseCategoryMapper.selectById(courseBase.getMt());
        courseBaseInfoDto.setMtName(courseCategory.getName());
        CourseCategory courseCategory1 = courseCategoryMapper.selectById(courseBase.getSt());
        courseBaseInfoDto.setStName(courseCategory1.getName());

        return courseBaseInfoDto;
    }

    /**
     * 修改课程接口
     * @author hao
     */
    @Transactional
    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {
        Long courseId = editCourseDto.getId();
        //查询本次修改的课程信息
        CourseBase courseBase = courseBaseInfoMapper.selectById(courseId);

        //验证课程机构及有效性
        if (courseBase == null){
            throw new XCException("课程不存在");
        }

        if (!courseBase.getCompanyId().equals(companyId)){
            throw new XCException("本机构只能修改本机构的课程");
        }

        //组装要修改的课程信息
        BeanUtils.copyProperties(editCourseDto,courseBase);
        courseBase.setChangeDate(LocalDateTime.now());

        Integer count = courseBaseInfoMapper.updateById(courseBase);

        //组装要修改的课程营销信息
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        if (courseMarket == null){
            throw new RuntimeException("课程信息不存在");
        }
        BeanUtils.copyProperties(editCourseDto,courseMarket);
        count += courseMarketMapper.updateById(courseMarket);

        //验证本次修改业务整体成功与否
        if (!count.equals(2)){
            throw new RuntimeException("操作异常！");
        }

        CourseBaseInfoDto courseBaseInfoDto = this.getCourseBaseById(courseId);

        return courseBaseInfoDto;
    }


    public Teacher GetTeacherByCourseId(Long courseId) {
        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teacher::getCourseId,courseId);
        Teacher teacher = teacherMedia.selectOne(queryWrapper);

        if (teacher == null){
            throw new XCException("未成功查询到课程教师");
        }

        return teacher;
    }

    @Override
    public TeacherVO saveTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = new Teacher();
        //判断是新增还是修改
        if (teacherDTO.getId() != null){
            teacher = teacherMedia.selectById(teacherDTO.getId());

            if (teacher == null){
                throw new XCException("未查询到所要修改的教师信息");
            }
            BeanUtils.copyProperties(teacherDTO,teacher);

            int count = teacherMedia.updateById(teacher);
            if (count != 1){
                throw new XCException("未成功修改教师信息");
            }
        }else {
            BeanUtils.copyProperties(teacher,teacherDTO);
            teacher.setCreateDate(LocalDateTime.now());
            int count1 = teacherMedia.insert(teacher);
            if (count1 != 1){
                throw new XCException("未成功新增教师信息");
            }
        }

        //组装返回教师信息
        TeacherVO teacherVO = new TeacherVO();
        teacherVO.setId(teacher.getId());
        BeanUtils.copyProperties(teacher,teacherVO);

        return teacherVO;
    }

    @Override
    public void DeleteTeacherByid(Long courseId, Long id) {
        if (courseId == null){
            throw new XCException("所要删除的教师信息的课程id为空");
        }
        if (id == null){
            throw new XCException("所要删除的教师信息的教师id为空");
        }

        LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Teacher::getCourseId,courseId);
        queryWrapper.eq(Teacher::getId,id);

        int count = teacherMedia.delete(queryWrapper);

        if (count != 1){
            throw new XCException("未成功删除课程教师其课程id为: "+courseId+" 教师id为 "+id);
        }
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
