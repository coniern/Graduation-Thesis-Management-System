package com.hfuu.auth.entity;

import jakarta.persistence.*;

/**
 * 答辩分组实体
 *
 * @author hfuu
 */
@Entity
@Table(name = "defense_group")
public class DefenseGroup {
    /**
     * 分组ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    /**
     * 分组名称
     */
    @Column(name = "group_name", nullable = false)
    private String groupName;

    /**
     * 组长ID
     */
    @Column(name = "group_leader_id")
    private Long groupLeaderId;

    /**
     * 组长姓名
     */
    @Column(name = "group_leader_name")
    private String groupLeaderName;

    /**
     * 答辩时间
     */
    @Column(name = "defense_time")
    private String defenseTime;

    /**
     * 答辩地点
     */
    @Column(name = "defense_location")
    private String defenseLocation;

    /**
     * 答辩秘书ID
     */
    @Column(name = "secretary_id")
    private Long secretaryId;

    /**
     * 答辩秘书姓名
     */
    @Column(name = "secretary_name")
    private String secretaryName;

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
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getGroupLeaderId() {
        return groupLeaderId;
    }

    public void setGroupLeaderId(Long groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
    }

    public String getGroupLeaderName() {
        return groupLeaderName;
    }

    public void setGroupLeaderName(String groupLeaderName) {
        this.groupLeaderName = groupLeaderName;
    }

    public String getDefenseTime() {
        return defenseTime;
    }

    public void setDefenseTime(String defenseTime) {
        this.defenseTime = defenseTime;
    }

    public String getDefenseLocation() {
        return defenseLocation;
    }

    public void setDefenseLocation(String defenseLocation) {
        this.defenseLocation = defenseLocation;
    }

    public Long getSecretaryId() {
        return secretaryId;
    }

    public void setSecretaryId(Long secretaryId) {
        this.secretaryId = secretaryId;
    }

    public String getSecretaryName() {
        return secretaryName;
    }

    public void setSecretaryName(String secretaryName) {
        this.secretaryName = secretaryName;
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
