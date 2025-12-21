package com.hfuu.auth.entity;

/**
 * 成绩信息实体
 * 
 * @author hfuu
 */
public class GradeInfo {
    /**
     * 成绩ID
     */
    private Long gradeId;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 指导教师成绩（40%）
     */
    private Double advisorScore;
    
    /**
     * 评审教师成绩（30%）
     */
    private Double reviewerScore;
    
    /**
     * 答辩成绩（30%）
     */
    private Double defenseScore;
    
    /**
     * 总成绩
     */
    private Double totalScore;
    
    /**
     * 成绩等级
     */
    private String gradeLevel;
    
    /**
     * 状态：1-待评定，2-已评定，3-已发布
     */
    private Integer status;
    
    /**
     * 最后更新时间
     */
    private String updateTime;
    
    /**
     * 发布时间
     */
    private String publishTime;

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Double getAdvisorScore() {
        return advisorScore;
    }

    public void setAdvisorScore(Double advisorScore) {
        this.advisorScore = advisorScore;
    }

    public Double getReviewerScore() {
        return reviewerScore;
    }

    public void setReviewerScore(Double reviewerScore) {
        this.reviewerScore = reviewerScore;
    }

    public Double getDefenseScore() {
        return defenseScore;
    }

    public void setDefenseScore(Double defenseScore) {
        this.defenseScore = defenseScore;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}