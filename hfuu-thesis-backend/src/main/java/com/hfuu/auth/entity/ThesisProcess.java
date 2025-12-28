package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 论文流程与时间管理实体
 *
 * @author hfuu
 */
@Entity
@Table(name = "thesis_process")
public class ThesisProcess {
    /**
     * 流程ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private Long processId;

    /**
     * 流程名称
     */
    @Column(name = "process_name", nullable = false)
    private String processName;

    /**
     * 流程阶段：1-选题阶段，2-开题阶段，3-初稿阶段，4-定稿阶段，5-答辩阶段，6-归档阶段
     */
    @Column(name = "process_stage")
    private Integer processStage;

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
     * 是否开启权限：0-关闭，1-开启
     */
    @Column(name = "permission_enabled")
    private Integer permissionEnabled;

    /**
     * 学年
     */
    @Column(name = "school_year")
    private String schoolYear;

    /**
     * 状态：1-有效，0-无效
     */
    @Column(name = "status")
    private Integer status;

    // getter and setter methods
    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Integer getProcessStage() {
        return processStage;
    }

    public void setProcessStage(Integer processStage) {
        this.processStage = processStage;
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

    public Integer getPermissionEnabled() {
        return permissionEnabled;
    }

    public void setPermissionEnabled(Integer permissionEnabled) {
        this.permissionEnabled = permissionEnabled;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
