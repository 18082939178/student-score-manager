package com.score.entity;

/**
 * 用户实体类（教师/学生登录账号）
 */
public class User {

    /** 主键 */
    private Long id;
    /** 登录名 */
    private String username;
    /** 密码（MD5） */
    private String password;
    /** 角色：1=教师 0=学生 */
    private int role;
    /** 姓名 */
    private String name;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
