package com.hfuu.auth.controller;

import com.hfuu.auth.entity.TeacherStudentSelection;
import com.hfuu.auth.entity.UserInfo;
import com.hfuu.auth.service.TeacherStudentSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 导师学生选择关系控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/selection")
public class TeacherStudentSelectionController {
    
    @Autowired
    private TeacherStudentSelectionService selectionService;
    
    /**
     * 学生选择导师
     * @param studentId 学生ID
     * @param teacherId 导师ID
     * @return 选择结果
     */
    @PostMapping("/select")
    public ResponseEntity<?> selectTeacher(@RequestParam Long studentId, @RequestParam Long teacherId) {
        try {
            TeacherStudentSelection result = selectionService.selectTeacher(studentId, teacherId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 学生放弃选择导师
     * @param selectionId 选择记录ID
     * @return 放弃结果
     */
    @PostMapping("/give-up")
    public ResponseEntity<?> giveUpSelection(@RequestParam Long selectionId) {
        try {
            TeacherStudentSelection result = selectionService.giveUpSelection(selectionId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 导师同意学生选择
     * @param selectionId 选择记录ID
     * @return 同意结果
     */
    @PostMapping("/approve")
    public ResponseEntity<?> approveSelection(@RequestParam Long selectionId) {
        try {
            TeacherStudentSelection result = selectionService.approveSelection(selectionId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 导师拒绝学生选择
     * @param selectionId 选择记录ID
     * @return 拒绝结果
     */
    @PostMapping("/reject")
    public ResponseEntity<?> rejectSelection(@RequestParam Long selectionId) {
        try {
            TeacherStudentSelection result = selectionService.rejectSelection(selectionId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 根据学生ID查询选择记录
     * @param studentId 学生ID
     * @return 选择记录列表
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getSelectionsByStudentId(@PathVariable Long studentId) {
        try {
            List<TeacherStudentSelection> results = selectionService.getSelectionsByStudentId(studentId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 根据导师ID查询选择记录
     * @param teacherId 导师ID
     * @return 选择记录列表
     */
    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<?> getSelectionsByTeacherId(@PathVariable Long teacherId) {
        try {
            List<TeacherStudentSelection> results = selectionService.getSelectionsByTeacherId(teacherId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 根据选择记录ID查询详情
     * @param selectionId 选择记录ID
     * @return 选择记录详情
     */
    @GetMapping("/detail/{selectionId}")
    public ResponseEntity<?> getSelectionById(@PathVariable Long selectionId) {
        try {
            TeacherStudentSelection result = selectionService.getSelectionById(selectionId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 查询已匹配的记录
     * @return 匹配记录列表
     */
    @GetMapping("/matched")
    public ResponseEntity<?> getMatchedSelections() {
        try {
            List<TeacherStudentSelection> results = selectionService.getMatchedSelections();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 获取所有导师列表
     * @return 导师列表
     */
    @GetMapping("/teachers")
    public ResponseEntity<?> getAllTeachers() {
        try {
            List<UserInfo> teachers = selectionService.getAllTeachers();
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 获取导师已同意的学生数量
     * @param teacherId 导师ID
     * @return 学生数量
     */
    @GetMapping("/teacher/{teacherId}/approved-count")
    public ResponseEntity<?> getApprovedStudentCount(@PathVariable Long teacherId) {
        try {
            Integer count = selectionService.getApprovedStudentCount(teacherId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 获取学生已选择的导师数量
     * @param studentId 学生ID
     * @return 导师数量
     */
    @GetMapping("/student/{studentId}/selected-count")
    public ResponseEntity<?> getSelectedTeacherCount(@PathVariable Long studentId) {
        try {
            Integer count = selectionService.getSelectedTeacherCount(studentId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 执行自动匹配算法
     * @return 匹配结果
     */
    @PostMapping("/auto-match")
    public ResponseEntity<?> executeAutoMatching() {
        try {
            List<TeacherStudentSelection> results = selectionService.executeAutoMatching();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    /**
     * 重置所有匹配记录
     * @return 重置结果
     */
    @PostMapping("/reset")
    public ResponseEntity<?> resetAllSelections() {
        try {
            boolean result = selectionService.resetAllSelections();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}