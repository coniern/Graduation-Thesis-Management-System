package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 消息通知实体
 * 
 * @author hfuu
 */
@Entity
@Table(name = "sys_message")
public class Message {
    /**
     * 消息ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;
    
    /**
     * 发送者ID
     */
    @Column(name = "sender_id")
    private Long senderId;
    
    /**
     * 接收者ID
     */
    @Column(name = "receiver_id")
    private Long receiverId;
    
    /**
     * 消息类型：1-系统通知，2-导师消息，3-评审消息
     */
    @Column(name = "message_type")
    private Integer messageType;
    
    /**
     * 消息标题
     */
    @Column(name = "title")
    private String title;
    
    /**
     * 消息内容
     */
    @Column(name = "content")
    private String content;
    
    /**
     * 消息状态：0-未读，1-已读
     */
    @Column(name = "status")
    private Integer status = 0;
    
    /**
     * 发送时间
     */
    @Column(name = "send_time")
    private String sendTime;
    
    /**
     * 阅读时间
     */
    @Column(name = "read_time")
    private String readTime;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }
}
