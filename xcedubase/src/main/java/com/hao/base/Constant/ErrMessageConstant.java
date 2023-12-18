package com.hao.base.Constant;

import lombok.Data;

/**
 * 常见错误常量定义
 */
@Data
public class ErrMessageConstant {
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String ACCOUNT_LOCKED = "账号被锁定";
    public static final String UNKNOWN_ERROR = "未知错误";
    public static final String USER_NOT_LOGIN = "用户未登录";
    public static final String LOGIN_FAILED = "登录失败";
    public static final String UPLOAD_FAILED = "文件上传失败";
    public static final String PASSWORD_EDIT_FAILED = "密码修改失败";
    public static final String ORDER_NOT_FOUND = "订单不存在";
    public static final String ALREADY_EXISTS = "用户名已存在";
    public static final String NONEXIST_FIELD = "不存在的字段";
}
