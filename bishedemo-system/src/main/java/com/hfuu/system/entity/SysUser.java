package com.hfuu.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 * 
 * @author hfuu
 */
@Data
@TableName("sys_user")
public class SysUser implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    private String username;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Length(min = 2, max = 50, message = "真实姓名长度必须在2-50个字符之间")
    private String realName;

    /**
     * 部门ID
     */
    @NotNull(message = "部门ID不能为空")
    private Long departmentId;

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /**
     * 最大指导学生数
     */
    @NotNull(message = "最大指导学生数不能为空")
    private Integer maxStudents;

    /**
     * 当前指导学生数
     */
    @NotNull(message = "当前指导学生数不能为空")
    private Integer currentStudents;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    private Integer userType;

    /**
     * 状态（0:禁用, 1:启用）
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