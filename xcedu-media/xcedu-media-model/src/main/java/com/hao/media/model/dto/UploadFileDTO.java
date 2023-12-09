package com.hao.media.model.dto;

import com.hao.media.model.po.MediaFiles;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
public class UploadFileDTO extends MediaFiles implements Serializable {
}
