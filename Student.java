package com.score.entity;

/**
 * 学生信息实体类
 */
public class Student {

    /** 主键 */
    private Long id;
    /** 学号 */
    private String studentNo;
    /** 姓名 */
    private String name;
    /** 班级 */
    private String className;
    /** 关联登录账号ID */
    private Long userId;

    public Student() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
