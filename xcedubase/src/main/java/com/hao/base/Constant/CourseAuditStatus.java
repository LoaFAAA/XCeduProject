package com.hao.base.Constant;

import lombok.Data;

@Data
public class CourseAuditStatus {
    //审核未通过
    public static String FailedAudit = "202001";

    //审核未提交
    public static String NotSubmittedAudit = "202002";

    //审核已提交
    public static String SubmittedAudit = "202003";

    //审核通过
    public static String PassedAudit = "202004";
}
