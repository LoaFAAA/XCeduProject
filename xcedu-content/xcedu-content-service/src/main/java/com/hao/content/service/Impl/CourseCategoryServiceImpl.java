package com.hao.content.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hao.content.mapper.CourseBaseInfoMapper;
import com.hao.content.mapper.CourseCategoryMapper;
import com.hao.content.model.dto.AddCourseDto;
import com.hao.content.model.dto.CourseBaseInfoDto;
import com.hao.content.model.dto.CourseCategoryTreeDto;
import com.hao.content.model.po.CourseBase;
import com.hao.content.service.CourseCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseCategoryServiceImpl implements CourseCategoryService {
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        //查询出所有课程分类，并按照各级节点进行排序
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);
        //先将list转成map，key就是结点的id，value为各个课程的全部信息，如果遇见key重复的问题，就保留后者
        Map<String,CourseCategoryTreeDto> maptemp = courseCategoryTreeDtos.stream().filter(item -> !id.equals(item.getId())).collect(Collectors.toMap(item -> item.getId(), value -> value, (key1,key2) -> key2));
        //

        //排除掉根节点及三级节点，courseCategoryList列表元素为各个二级节点,是最终的返回数据
        List<CourseCategoryTreeDto> courseCategoryList = new ArrayList<>();
        courseCategoryTreeDtos.stream().filter(item -> !id.equals(item.getId())).forEach(item -> {
            //将二级元素加入返回列表
            if (item.getParentid().equals(id)){
                courseCategoryList.add(item);
            }
            //找出节点的父节点
            CourseCategoryTreeDto parentTreeDto = maptemp.get(item.getParentid());
            //因为二级节点的父节点为空，将其排除掉
            if (parentTreeDto != null){
                //对父节点的子节点列表进行判空，为空时新建该父节点的子节点列表
                if (parentTreeDto.getChildrenTreeNodes() == null){
                List<CourseCategoryTreeDto> parentList = new ArrayList<CourseCategoryTreeDto>();
                    parentTreeDto.setChildrenTreeNodes(parentList);
                }
                //将该节点添加至父节点的列表中
                parentTreeDto.getChildrenTreeNodes().add(item);
            }
        });
        //返回由二级节点构成的列表
        return courseCategoryList;
    }

}
