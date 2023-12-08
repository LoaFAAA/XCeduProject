package com.hao.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hao.media.model.po.MediaFiles;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MediaFileMapper extends BaseMapper<MediaFiles> {
}
