package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 论文文档实体
 * 
 * @author hfuu
 */
@Entity
@Table(name = "thesis_document")
public class ThesisDocument {
    /**
     * 文档ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;
    
    /**
     * 学生ID
     */
    @Column(name = "student_id")
    private Long studentId;
    
    /**
     * 文档类型：1-开题报告，2-中期报告，3-论文初稿，4-论文终稿，5-查重报告，6-答辩材料，7-支撑材料
     */
    @Column(name = "document_type")
    private Integer documentType;
    
    /**
     * 文档名称
     */
    @Column(name = "document_name")
    private String documentName;
    
    /**
     * 文档路径
     */
    @Column(name = "file_path")
    private String filePath;
    
    /**
     * 文档大小
     */
    @Column(name = "file_size")
    private Long fileSize;
    
    /**
     * 提交时间
     */
    @Column(name = "submit_time")
    private String submitTime;
    
    /**
     * 状态：1-待审核，2-审核通过，3-审核不通过，4-已修改
     */
    @Column(name = "status")
    private Integer status;
    
    /**
     * 审核教师ID
     */
    @Column(name = "reviewer_id")
    private Long reviewerId;
    
    /**
     * 审核意见
     */
    @Column(name = "review_comment")
    private String reviewComment;
    
    /**
     * 审核时间
     */
    @Column(name = "review_time")
    private String reviewTime;
    
    /**
     * 查重率
     */
    @Column(name = "similarity_rate")
    private Double similarityRate;
    
    /**
     * 版本号
     */
    @Column(name = "version")
    private Integer version;
    
    /**
     * 文件格式
     */
    @Column(name = "file_format")
    private String fileFormat;
    
    /**
     * 文件MD5值
     */
    @Column(name = "file_md5")
    private String fileMd5;
    
    /**
     * 上传用户ID
     */
    @Column(name = "upload_user_id")
    private Long uploadUserId;
    
    /**
     * 上传时间
     */
    @Column(name = "upload_time")
    private String uploadTime;

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

    public Integer getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Integer documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Double getSimilarityRate() {
        return similarityRate;
    }

    public void setSimilarityRate(Double similarityRate) {
        this.similarityRate = similarityRate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public Long getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(Long uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }
}