package com.hao.content;

import com.hao.base.model.PageParams;
import com.hao.content.model.dto.QueryCourseParamsDTO;
import com.hao.content.mapper.CourseBaseInfoMapper;
import com.hao.content.service.CourseBaseInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
//@MapperScan("com.hao.content")
public class CourseBaseInfoTest {
    @Autowired
    private CourseBaseInfoMapper courseBaseInfoMapper;
    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @Test
    public void TestCourseBaseMapper(){
//        CourseBase courseBase = courseBaseInfoMapper.selectById(2L);
//        System.out.println(courseBase);
        PageParams pageParams = new PageParams(1L,10L);
        QueryCourseParamsDTO queryCourseParamsDTO = new QueryCourseParamsDTO();
        courseBaseInfoService.selectPage(pageParams,queryCourseParamsDTO);

    }
}
