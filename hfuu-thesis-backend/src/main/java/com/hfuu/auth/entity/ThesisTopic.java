package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 论文题目实体
 * 
 * @author hfuu
 */
@Entity
@Table(name = "thesis_topic")
public class ThesisTopic {
    /**
     * 题目ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long topicId;
    
    /**
     * 题目名称
     */
    @Column(name = "topic_name", nullable = false)
    private String topicName;
    
    /**
     * 题目描述
     */
    @Column(name = "topic_description", columnDefinition = "text")
    private String topicDescription;
    
    /**
     * 发布教师ID
     */
    @Column(name = "teacher_id")
    private Long teacherId;
    
    /**
     * 发布教师姓名
     */
    @Column(name = "teacher_name")
    private String teacherName;
    
    /**
     * 题目类型
     */
    @Column(name = "topic_type")
    private String topicType;
    
    /**
     * 题目要求
     */
    @Column(name = "topic_requirements", columnDefinition = "text")
    private String topicRequirements;
    
    /**
     * 难易度：1-简单，2-中等，3-困难
     */
    @Column(name = "difficulty")
    private Integer difficulty;
    
    /**
     * 限选专业
     */
    @Column(name = "restricted_majors")
    private String restrictedMajors;
    
    /**
     * 最大可选人数
     */
    @Column(name = "max_student_count")
    private Integer maxStudentCount;
    
    /**
     * 当前选中人数
     */
    @Column(name = "current_student_count")
    private Integer currentStudentCount;
    
    /**
     * 状态：1-未发布，2-已发布，3-已结束，4-审核中，5-审核驳回
     */
    @Column(name = "status")
    private Integer status;
    
    /**
     * 审核意见
     */
    @Column(name = "audit_comment", columnDefinition = "text")
    private String auditComment;
    
    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
    
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public Integer getMaxStudentCount() {
        return maxStudentCount;
    }

    public void setMaxStudentCount(Integer maxStudentCount) {
        this.maxStudentCount = maxStudentCount;
    }

    public Integer getCurrentStudentCount() {
        return currentStudentCount;
    }

    public void setCurrentStudentCount(Integer currentStudentCount) {
        this.currentStudentCount = currentStudentCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取题目要求
     * @return 题目要求
     */
    public String getTopicRequirements() {
        return topicRequirements;
    }

    /**
     * 设置题目要求
     * @param topicRequirements 题目要求
     */
    public void setTopicRequirements(String topicRequirements) {
        this.topicRequirements = topicRequirements;
    }

    /**
     * 获取难易度
     * @return 难易度
     */
    public Integer getDifficulty() {
        return difficulty;
    }

    /**
     * 设置难易度
     * @param difficulty 难易度
     */
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * 获取限选专业
     * @return 限选专业
     */
    public String getRestrictedMajors() {
        return restrictedMajors;
    }

    /**
     * 设置限选专业
     * @param restrictedMajors 限选专业
     */
    public void setRestrictedMajors(String restrictedMajors) {
        this.restrictedMajors = restrictedMajors;
    }

    /**
     * 获取审核意见
     * @return 审核意见
     */
    public String getAuditComment() {
        return auditComment;
    }

    /**
     * 设置审核意见
     * @param auditComment 审核意见
     */
    public void setAuditComment(String auditComment) {
        this.auditComment = auditComment;
    }
}