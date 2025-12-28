package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 导师学生选择关系实体
 * 用于存储学生的选择意向和导师的确认结果
 * 
 * @author hfuu
 */
@Entity
@Table(name = "teacher_student_selection")
public class TeacherStudentSelection {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "selection_id")
    private Long selectionId;
    
    /**
     * 学生ID
     */
    @Column(name = "student_id")
    private Long studentId;
    
    /**
     * 学生姓名
     */
    @Column(name = "student_name")
    private String studentName;
    
    /**
     * 导师ID
     */
    @Column(name = "teacher_id")
    private Long teacherId;
    
    /**
     * 导师姓名
     */
    @Column(name = "teacher_name")
    private String teacherName;
    
    /**
     * 学生选择状态：1-已选择，2-已放弃
     */
    @Column(name = "student_status")
    private Integer studentStatus;
    
    /**
     * 导师确认状态：1-待确认，2-已同意，3-已拒绝
     */
    @Column(name = "teacher_status")
    private Integer teacherStatus;
    
    /**
     * 最终匹配状态：0-未匹配，1-已匹配
     */
    @Column(name = "match_status")
    private Integer matchStatus;
    
    /**
     * 选择时间
     */
    @Column(name = "selection_time")
    private String selectionTime;
    
    /**
     * 确认时间
     */
    @Column(name = "confirm_time")
    private String confirmTime;
    
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    
    // getter和setter方法
    public Long getSelectionId() {
        return selectionId;
    }
    
    public void setSelectionId(Long selectionId) {
        this.selectionId = selectionId;
    }
    
    public Long getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
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
    
    public Integer getStudentStatus() {
        return studentStatus;
    }
    
    public void setStudentStatus(Integer studentStatus) {
        this.studentStatus = studentStatus;
    }
    
    public Integer getTeacherStatus() {
        return teacherStatus;
    }
    
    public void setTeacherStatus(Integer teacherStatus) {
        this.teacherStatus = teacherStatus;
    }
    
    public Integer getMatchStatus() {
        return matchStatus;
    }
    
    public void setMatchStatus(Integer matchStatus) {
        this.matchStatus = matchStatus;
    }
    
    public String getSelectionTime() {
        return selectionTime;
    }
    
    public void setSelectionTime(String selectionTime) {
        this.selectionTime = selectionTime;
    }
    
    public String getConfirmTime() {
        return confirmTime;
    }
    
    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
}