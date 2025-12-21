package com.hfuu.auth.entity;

/**
 * 论文进度实体
 * 
 * @author hfuu
 */
public class ThesisProgress {
    /**
     * 进度ID
     */
    private Long progressId;
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 阶段：1-选题阶段，2-开题阶段，3-中期阶段，4-论文阶段，5-评审阶段，6-答辩阶段，7-成绩阶段
     */
    private Integer stage;
    
    /**
     * 阶段名称
     */
    private String stageName;
    
    /**
     * 进度状态：1-未开始，2-进行中，3-已完成
     */
    private Integer status;
    
    /**
     * 开始时间
     */
    private String startTime;
    
    /**
     * 结束时间
     */
    private String endTime;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 更新时间
     */
    private String updateTime;

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}