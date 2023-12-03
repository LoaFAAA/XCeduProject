package com.hao.content.service.Impl;

import com.hao.content.mapper.CourseMarketMapper;
import com.hao.content.model.po.CourseMarket;
import com.hao.content.service.CourseMarketService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseMarketServiceImpl implements CourseMarketService {
    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Override
    public CourseMarket selectById(Long id) {
        return null;
    }

    @Override
    public int CreateMarket(CourseMarket courseMarket) {
        //对字段进行判空
        if (StringUtils.isBlank(courseMarket.getCharge())) {
            throw new RuntimeException("课程收费情况为空");
        }
        if (StringUtils.isBlank(String.valueOf(courseMarket.getId()))) {
            throw new RuntimeException("课程营销表id为空");
        }

        //判断插入操作是否失败
        int count = courseMarketMapper.insert(courseMarket);
        if (count != 1){
            throw new RuntimeException("插入课程营销表失败");
        }

        return count;
    }
}
