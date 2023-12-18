package com.hao.content.service;

import com.hao.content.model.vo.CoursePreviewVO;

public interface CoursePublishService {
    /**
     * @description 获取课程预览信息
     * @param courseId 课程id
     * @return com.hao.content.model.dto.CoursePreviewVO
     * @author hao
     */
    public CoursePreviewVO getCoursePreviewInfo(Long courseId);

    /**
     * @description 将课程提交审核
     * @param courseId 课程id
     * @param companyId 机构id
     * @return null
     * @author hao
     */
    void commitAudit(Long companyId, Long courseId);

    /**
     * @description 将审核课程进行发布
     * @param courseId 课程id
     * @param companyId 机构id
     * @return null
     * @author hao
     */
    void coursePublish(Long companyId, Long courseId);
}
