package com.hfuu.auth.entity;

/**
 * 论文题目实体
 * 
 * @author hfuu
 */
public class ThesisTopic {
    /**
     * 题目ID
     */
    private Long topicId;
    
    /**
     * 题目名称
     */
    private String topicName;
    
    /**
     * 题目描述
     */
    private String topicDescription;
    
    /**
     * 发布教师ID
     */
    private Long teacherId;
    
    /**
     * 发布教师姓名
     */
    private String teacherName;
    
    /**
     * 题目类型
     */
    private String topicType;
    
    /**
     * 最大可选人数
     */
    private Integer maxStudentCount;
    
    /**
     * 当前选中人数
     */
    private Integer currentStudentCount;
    
    /**
     * 状态：1-未发布，2-已发布，3-已结束
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private String createTime;
    
    /**
     * 更新时间
     */
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
}