package com.hao.media.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hao
 * @version 1.0
 * @description 媒资文件查询请求模型类
 */
@Data
@ToString
public class QueryMediaParamsDto implements Serializable {
    @ApiModelProperty("媒资文件名称")
    private String filename;
    @ApiModelProperty("媒资类型")
    private String fileType;
    @ApiModelProperty("审核状态")
    private String auditStatus;
}
