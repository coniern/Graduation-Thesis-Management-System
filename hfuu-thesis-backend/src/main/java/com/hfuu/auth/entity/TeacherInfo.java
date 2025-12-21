package com.hfuu.auth.entity;

/**
 * 教师信息实体
 * 
 * @author hfuu
 */
public class TeacherInfo {
    /**
     * 教师ID
     */
    private Long teacherId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 工号
     */
    private String teacherNo;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 性别
     */
    private String gender;
    
    /**
     * 职称
     */
    private String title;
    
    /**
     * 所在学院
     */
    private String college;
    
    /**
     * 研究方向
     */
    private String researchDirection;
    
    /**
     * 当前指导学生数
     */
    private Integer currentStudentCount;
    
    /**
     * 最大指导学生数
     */
    private Integer maxStudentCount;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTeacherNo() {
        return teacherNo;
    }

    public void setTeacherNo(String teacherNo) {
        this.teacherNo = teacherNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getResearchDirection() {
        return researchDirection;
    }

    public void setResearchDirection(String researchDirection) {
        this.researchDirection = researchDirection;
    }

    public Integer getCurrentStudentCount() {
        return currentStudentCount;
    }

    public void setCurrentStudentCount(Integer currentStudentCount) {
        this.currentStudentCount = currentStudentCount;
    }

    public Integer getMaxStudentCount() {
        return maxStudentCount;
    }

    public void setMaxStudentCount(Integer maxStudentCount) {
        this.maxStudentCount = maxStudentCount;
    }
}