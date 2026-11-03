package com.score.entity;

/**
 * 课程实体类
 */
public class Course {

    /** 主键 */
    private Long id;
    /** 课程名称 */
    private String courseName;
    /** 学分 */
    private double credit;
    /** 任课教师 */
    private String teacher;

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
