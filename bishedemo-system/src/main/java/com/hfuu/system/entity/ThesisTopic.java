package com.hfuu.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 论文题目实体类
 * 
 * @author hfuu
 */
@Data
@TableName("thesis_topic")
public class ThesisTopic implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 题目ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 题目名称
     */
    @NotBlank(message = "题目名称不能为空")
    @Length(min = 5, max = 200, message = "题目名称长度必须在5-200个字符之间")
    private String title;

    /**
     * 指导教师ID
     */
    @NotNull(message = "指导教师ID不能为空")
    private Long teacherId;

    /**
     * 最大选题人数
     */
    @NotNull(message = "最大选题人数不能为空")
    private Integer maxSelections;

    /**
     * 当前选题人数
     */
    @NotNull(message = "当前选题人数不能为空")
    private Integer currentSelections;

    /**
     * 题目类型
     */
    private String topicType;

    /**
     * 题目描述
     */
    @NotBlank(message = "题目描述不能为空")
    private String description;

    /**
     * 选题要求
     */
    @NotBlank(message = "选题要求不能为空")
    private String requirement;

    /**
     * 状态（0:未发布, 1:已发布, 2:已结束）
     */
    @NotNull(message = "状态不能为空")
    private Integer status;

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