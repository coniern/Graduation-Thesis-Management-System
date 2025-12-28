package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 通知公告实体
 * 
 * @author hfuu
 */
@Entity
@Table(name = "sys_notice")
public class Notice {
    /**
     * 通知ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;
    
    /**
     * 通知标题
     */
    @Column(name = "title", nullable = false)
    private String title;
    
    /**
     * 通知内容
     */
    @Column(name = "content", columnDefinition = "text")
    private String content;
    
    /**
     * 发布人ID
     */
    @Column(name = "publisher_id")
    private Long publisherId;
    
    /**
     * 发布人姓名
     */
    @Column(name = "publisher_name")
    private String publisherName;
    
    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    private String publishTime;
    
    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private String startTime;
    
    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private String endTime;
    
    /**
     * 通知类型：1-系统通知，2-公告，3-消息推送
     */
    @Column(name = "notice_type")
    private Integer noticeType;
    
    /**
     * 状态：1-未发布，2-已发布，3-已过期
     */
    @Column(name = "status")
    private Integer status;
    
    /**
     * 点击量
     */
    @Column(name = "click_count")
    private Integer clickCount;

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
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

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(Integer noticeType) {
        this.noticeType = noticeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }
}