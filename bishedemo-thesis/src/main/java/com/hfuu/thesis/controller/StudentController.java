package com.hfuu.thesis.controller;

import com.hfuu.common.result.Result;
import com.hfuu.system.entity.SysUser;
import com.hfuu.system.service.TokenService;
import com.hfuu.thesis.entity.ThesisDocument;
import com.hfuu.thesis.entity.ThesisTopic;
import com.hfuu.thesis.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 学生模块API
 */
@RestController
@RequestMapping("/api/student")
@Tag(name = "学生接口")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取学生仪表盘信息
     * GET /api/student/dashboard
     */
    @GetMapping("/dashboard")
    @Operation(summary = "获取学生仪表盘", description = "获取学生毕业论文进度仪表盘信息")
    @PreAuthorize("hasRole('STUDENT')")
    public Result getDashboard(@RequestHeader("Authorization") String token) {
        // 从令牌中获取当前学生ID
        SysUser user = tokenService.parseToken(token.replace("Bearer ", ""));
        // 获取学生仪表盘信息
        DashboardResponse dashboard = studentService.getDashboard(user.getId());
        return Result.success("获取成功", dashboard);
    }

    /**
     * 申请选题
     * POST /api/student/topic-apply
     */
    @PostMapping("/topic-apply")
    @Operation(summary = "申请选题", description = "学生申请毕业论文选题")
    @PreAuthorize("hasRole('STUDENT')")
    public Result applyTopic(@RequestHeader("Authorization") String token, @Valid @RequestBody TopicApplyRequest request) {
        // 从令牌中获取当前学生ID
        SysUser user = tokenService.parseToken(token.replace("Bearer ", ""));
        // 调用服务申请选题
        studentService.applyTopic(user.getId(), request.getTopicId(), request.getReason());
        return Result.success("选题申请已提交");
    }

    /**
     * 上传文档
     * POST /api/student/document/upload
     */
    @PostMapping("/document/upload")
    @Operation(summary = "上传文档", description = "学生上传毕业论文文档")
    @PreAuthorize("hasRole('STUDENT')")
    public Result uploadDocument(
            @RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file,
            @RequestParam("topicId") Long topicId,
            @RequestParam("stage") Integer stage,
            @RequestParam(value = "remark", required = false) String remark) {
        // 从令牌中获取当前学生ID
        SysUser user = tokenService.parseToken(token.replace("Bearer ", ""));
        // 调用服务上传文档
        ThesisDocument document = studentService.uploadDocument(user.getId(), topicId, stage, file, remark);
        return Result.success("文档上传成功", document);
    }
}