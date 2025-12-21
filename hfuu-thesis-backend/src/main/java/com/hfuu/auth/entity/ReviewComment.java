package com.hfuu.auth.entity;

/**
 * 评审意见实体
 * 
 * @author hfuu
 */
public class ReviewComment {
    /**
     * 评审ID
     */
    private Long reviewId;
    
    /**
     * 文档ID
     */
    private Long documentId;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 评审教师ID
     */
    private Long reviewerId;
    
    /**
     * 评审教师姓名
     */
    private String reviewerName;
    
    /**
     * 评审类型：1-指导教师评审，2-评审教师评审，3-答辩评审
     */
    private Integer reviewType;
    
    /**
     * 评审成绩
     */
    private Double score;
    
    /**
     * 评审意见
     */
    private String comment;
    
    /**
     * 评审时间
     */
    private String reviewTime;
    
    /**
     * 是否通过：1-通过，2-不通过
     */
    private Integer isPass;

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Integer getReviewType() {
        return reviewType;
    }

    public void setReviewType(Integer reviewType) {
        this.reviewType = reviewType;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Integer getIsPass() {
        return isPass;
    }

    public void setIsPass(Integer isPass) {
        this.isPass = isPass;
    }
}