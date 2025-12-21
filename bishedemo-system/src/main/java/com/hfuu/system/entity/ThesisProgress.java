package com.hfuu.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 论文进度实体类
 * 
 * @author hfuu
 */
@Data
@TableName("thesis_progress")
public class ThesisProgress implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 进度ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 论文ID
     */
    @NotNull(message = "论文ID不能为空")
    private Long thesisId;

    /**
     * 当前阶段（1:选题, 2:开题, 3:中期, 4:答辩, 5:归档）
     */
    @NotNull(message = "当前阶段不能为空")
    private Integer currentStage;

    /**
     * 阶段状态（0:未开始, 1:已提交, 2:审核通过, 3:审核不通过）
     */
    @NotNull(message = "阶段状态不能为空")
    private Integer stageStatus;

    /**
     * 总体进度（0-100）
     */
    @NotNull(message = "总体进度不能为空")
    private Integer overallProgress;

    /**
     * 阶段描述
     */
    private String stageDescription;

    /**
     * 指导教师意见
     */
    private String teacherComment;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 删除标志（0:未删除, 1:已删除）
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}