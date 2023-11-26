package com.hao.content;

import com.hao.content.mapper.CourseCategoryMapper;
import com.hao.content.model.dto.CourseCategoryTreeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CourseCategoryMapperTest {

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Test
    public void TestSelectTreeNode(){
        List<CourseCategoryTreeDto> courseCategoryTreeDTOList = courseCategoryMapper.selectTreeNodes("1");
        System.out.println(courseCategoryTreeDTOList);
    }
}
