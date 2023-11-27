package com.hao.content.service;


import com.hao.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

public interface CourseCategoryService {
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);


}
