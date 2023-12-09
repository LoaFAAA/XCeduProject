package com.hao.media.model.vo;

import com.hao.media.model.po.MediaFiles;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class UploadFileResultVO extends MediaFiles implements Serializable {
}
