package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 用户信息实体
 * 
 * @author hfuu
 */
@Entity
@Table(name = "sys_user")
public class UserInfo {
    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "real_name")
    private String realName;

    /**
     * 用户类型：1-学生，2-导师，3-院系管理员，4-校级管理员
     */
    @Column(name = "user_type")
    private Integer userType;

    /**
     * 状态：0-禁用，1-启用
     */
    @Column(name = "status")
    private Integer status = 1;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
