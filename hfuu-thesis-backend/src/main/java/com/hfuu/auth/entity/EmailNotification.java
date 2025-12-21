package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 邮件通知实体
 * 
 * @author hfuu
 */
@Entity
@Table(name = "sys_email_notification")
public class EmailNotification {
    /**
     * 邮件ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long emailId;
    
    /**
     * 收件人邮箱
     */
    @Column(name = "recipient_email")
    private String recipientEmail;
    
    /**
     * 收件人姓名
     */
    @Column(name = "recipient_name")
    private String recipientName;
    
    /**
     * 邮件主题
     */
    @Column(name = "subject")
    private String subject;
    
    /**
     * 邮件内容
     */
    @Column(name = "content")
    private String content;
    
    /**
     * 邮件模板ID
     */
    @Column(name = "template_id")
    private Long templateId;
    
    /**
     * 发送状态：0-待发送，1-已发送，2-发送失败
     */
    @Column(name = "status")
    private Integer status = 0;
    
    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private String sendTime;
    
    /**
     * 重试次数
     */
    @Column(name = "retry_count")
    private Integer retryCount = 0;
    
    /**
     * 错误信息
     */
    @Column(name = "error_message")
    private String errorMessage;

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
