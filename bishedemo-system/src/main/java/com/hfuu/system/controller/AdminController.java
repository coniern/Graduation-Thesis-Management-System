package com.hfuu.system.controller;

import com.hfuu.common.result.Result;
import com.hfuu.system.entity.SysUser;
import com.hfuu.system.service.AdminService;
import com.hfuu.system.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 管理员模块API
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "管理员接口")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取系统统计数据
     * GET /api/admin/stats
     */
    @GetMapping("/stats")
    @Operation(summary = "获取系统统计数据", description = "获取毕业论文管理系统统计数据")
    @PreAuthorize("hasAnyRole('DEPT_ADMIN', 'SCHOOL_ADMIN')")
    public Result getStats(@RequestHeader("Authorization") String token) {
        // 从令牌中获取当前管理员信息
        SysUser user = tokenService.parseToken(token.replace("Bearer ", ""));
        // 根据管理员类型获取不同的统计数据
        StatsResponse stats;
        if (user.getUserType() == 3) { // 院系管理员
            stats = adminService.getDeptStats(user.getDepartmentId());
        } else { // 校级管理员
            stats = adminService.getSchoolStats();
        }
        return Result.success("获取成功", stats);
    }

    /**
     * 配置系统参数
     * POST /api/admin/config
     */
    @PostMapping("/config")
    @Operation(summary = "配置系统参数", description = "配置毕业论文管理系统参数")
    @PreAuthorize("hasRole('SCHOOL_ADMIN')")
    public Result updateConfig(@RequestHeader("Authorization") String token, @Valid @RequestBody ConfigRequest request) {
        // 从令牌中获取当前管理员信息
        SysUser user = tokenService.parseToken(token.replace("Bearer ", ""));
        // 调用服务更新配置
        adminService.updateConfig(request);
        return Result.success("配置更新成功");
    }
}