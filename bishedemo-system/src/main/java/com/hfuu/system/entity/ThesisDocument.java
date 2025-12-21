package com.hfuu.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 文档实体类
 * 
 * @author hfuu
 */
@Data
@TableName("thesis_document")
public class ThesisDocument implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 文档ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 论文ID
     */
    @NotNull(message = "论文ID不能为空")
    private Long thesisId;

    /**
     * 版本号
     */
    @NotNull(message = "版本号不能为空")
    private Integer version;

    /**
     * 文件路径
     */
    @NotBlank(message = "文件路径不能为空")
    private String filePath;

    /**
     * 相似度（重复率%）
     */
    private Double similarityRate;

    /**
     * 文档名称
     */
    @NotBlank(message = "文档名称不能为空")
    @Length(max = 100, message = "文档名称长度不能超过100个字符")
    private String fileName;

    /**
     * 文件大小（字节）
     */
    @NotNull(message = "文件大小不能为空")
    private Long fileSize;

    /**
     * 文档类型（1:开题报告, 2:中期报告, 3:最终论文, 4:查重报告）
     */
    @NotNull(message = "文档类型不能为空")
    private Integer documentType;

    /**
     * 上传者ID
     */
    @NotNull(message = "上传者ID不能为空")
    private Long uploaderId;

    /**
     * 上传时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date uploadTime;

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