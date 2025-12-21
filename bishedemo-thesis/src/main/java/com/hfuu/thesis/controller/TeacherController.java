package com.hfuu.thesis.controller;

import com.hfuu.common.result.Result;
import com.hfuu.system.entity.SysUser;
import com.hfuu.system.service.TokenService;
import com.hfuu.thesis.entity.ThesisDocument;
import com.hfuu.thesis.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 导师模块API
 */
@RestController
@RequestMapping("/api/teacher")
@Tag(name = "导师接口")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取导师指导学生列表
     * GET /api/teacher/students
     */
    @GetMapping("/students")
    @Operation(summary = "获取指导学生列表", description = "获取导师指导的学生列表")
    @PreAuthorize("hasRole('TEACHER')")
    public Result getStudents(@RequestHeader("Authorization") String token) {
        // 从令牌中获取当前导师ID
        SysUser user = tokenService.parseToken(token.replace("Bearer ", ""));
        // 获取导师指导的学生列表
        PageInfo<StudentInfo> students = teacherService.getStudents(user.getId(), 1, 10);
        return Result.success("获取成功", students);
    }

    /**
     * 审核学生文档
     * POST /api/teacher/review
     */
    @PostMapping("/review")
    @Operation(summary = "审核文档", description = "导师审核学生提交的毕业论文文档")
    @PreAuthorize("hasRole('TEACHER')")
    public Result reviewDocument(@RequestHeader("Authorization") String token, @Valid @RequestBody ReviewRequest request) {
        // 从令牌中获取当前导师ID
        SysUser user = tokenService.parseToken(token.replace("Bearer ", ""));
        // 调用服务审核文档
        teacherService.reviewDocument(request.getDocumentId(), user.getId(), request.getStatus(), request.getRemark());
        return Result.success("审核成功");
    }

    /**
     * 给学生论文评分
     * POST /api/teacher/score
     */
    @PostMapping("/score")
    @Operation(summary = "给论文评分", description = "导师给学生毕业论文评分")
    @PreAuthorize("hasRole('TEACHER')")
    public Result scoreThesis(@RequestHeader("Authorization") String token, @Valid @RequestBody ScoreRequest request) {
        // 从令牌中获取当前导师ID
        SysUser user = tokenService.parseToken(token.replace("Bearer ", ""));
        // 调用服务给论文评分
        teacherService.scoreThesis(request.getThesisId(), user.getId(), request.getScore(), request.getComment());
        return Result.success("评分成功");
    }
}