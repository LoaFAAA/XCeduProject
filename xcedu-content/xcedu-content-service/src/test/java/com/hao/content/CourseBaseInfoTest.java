package com.hao.content;

import com.hao.base.model.PageParams;
import com.hao.content.mapper.CourseMarketMapper;
import com.hao.content.model.dto.CourseBaseInfoDto;
import com.hao.content.model.dto.QueryCourseParamsDTO;
import com.hao.content.mapper.CourseBaseInfoMapper;
import com.hao.content.model.po.CourseBase;
import com.hao.content.model.po.CourseMarket;
import com.hao.content.service.CourseBaseInfoService;
import com.hao.content.service.CourseMarketService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@MapperScan("com.hao.content.mapper")
public class CourseBaseInfoTest {
    @Autowired
    private CourseBaseInfoMapper courseBaseInfoMapper;
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;
    @Autowired
    private CourseMarketService courseMarketService;
    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Test
    public void TestCourseBaseMapper(){
//        CourseBase courseBase = courseBaseInfoMapper.selectById(2L);
//        System.out.println(courseBase);
        PageParams pageParams = new PageParams(1L,10L);
        QueryCourseParamsDTO queryCourseParamsDTO = new QueryCourseParamsDTO();
        courseBaseInfoService.selectPage(pageParams,queryCourseParamsDTO);

    }

    @Test
    public void TestSelectById(){
        CourseBase courseBase = courseBaseInfoMapper.selectById(3L);
        if (courseBase == null){
            System.out.println("hhhh");
        }
    }

    @Test
    public void TestI(){
        Integer n = 100;
        System.out.println(n+1);
    }
}
