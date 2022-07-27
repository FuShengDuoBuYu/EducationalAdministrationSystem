package com.se.lab2_backend.util;

public class ConstVariable {
    public static final String USER_NOT_EXIST = "学工号不存在";
    public static final String P_INVALID = "密码错误";
    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String REQUEST_SUCCESS = "操作成功";
    public static final String REQUEST_FAIL = "操作失败";
    public static final String COURSE_SELECTION_CLOSE = "管理员还未打开选课通道！";
    public static final String INITIAL_P = "123456";
    public static final String IDENTITY_TEACHER = "教师";
    public static final String IDENTITY_ADMIN = "管理员";
    public static final String IDENTITY_STUDENT = "学生";
    public static final String INITIAL_STUDENT_STATUS = "a33de6c31ac44b37925bce12df60d91d";//学生在读
    public static final String INITIAL_TEACHER_STATUS = "d0eb226721ce41fba87ce1872956058b";//教师在岗
    public static final String SELECTED_COURSE_STATUS = "bbb9c7eb3a9c4d60845b2ea23c1d81af";//课程已选
    public static final String FINISHED_COURSE_STATUS = "4e8c076151514b98b641901b26688fa0";//课程已修
    public static final String NEW_APPLICATION_TYPE = "5fa11766b62d11eca8f8020017000b7b";//新增
    public static final String MODIFY_APPLICATION_TYPE = "5fa118d8b62d11eca8f8020017000b7b";//修改
    public static final String DELETE_APPLICATION_TYPE = "5fa119beb62d11eca8f8020017000b7b";//删除
    public static final String INITIAL_APPLICATION_RESULT = "483f5006b62d11ecab73020017000b7b";//待审核
    public static final String ACCEPT_APPLICATION_RESULT = "483f5164b62d11ecab73020017000b7b";//已通过
    public static final String REFUSE_APPLICATION_RESULT = "483f524ab62d11ecab73020017000b7b";//已拒绝
    public static final String PUBLIC_AVAILABLE_MAJOR = "1";//公共课程
    public static final String MAJOR_AVAILABLE_MAJOR = "2";//专业课程
    public static final String PARTIAL_AVAILABLE_MAJOR = "3";//面向部分专业课程
    public static final String STUDENT_COURSE_STATUS = "4e8c076151514b98b641901b26688fa0";//已修


    public static String getInfoSuccess(String info){ return "已取到"+info+"信息！"; }
}
