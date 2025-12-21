package com.hfuu.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 文档版本实体类
 * 
 * @author hfuu
 */
@Data
@TableName("document_version")
public class DocumentVersion implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 版本ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文档ID
     */
    @NotNull(message = "文档ID不能为空")
    private Long documentId;

    /**
     * 父版本ID
     */
    private Long parentVersionId;

    /**
     * 差异内容
     */
    @NotNull(message = "差异内容不能为空")
    private String deltaContent;

    /**
     * 版本号
     */
    @NotNull(message = "版本号不能为空")
    private Integer versionNumber;

    /**
     * 版本备注
     */
    private String remark;

    /**
     * 上传者ID
     */
    @NotNull(message = "上传者ID不能为空")
    private Long uploaderId;

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