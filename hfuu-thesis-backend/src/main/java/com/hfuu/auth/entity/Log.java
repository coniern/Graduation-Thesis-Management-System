package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 系统日志实体
 *
 * @author hfuu
 */
@Entity
@Table(name = "sys_log")
public class Log {
    /**
     * 日志ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 操作类型
     */
    @Column(name = "operation")
    private String operation;

    /**
     * 操作内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 操作IP
     */
    @Column(name = "ip")
    private String ip;

    /**
     * 操作时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 操作结果
     */
    @Column(name = "result")
    private String result;

    /**
     * 操作耗时(毫秒)
     */
    @Column(name = "duration")
    private Long duration;

    // getter and setter methods
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
