package com.hfuu.auth.service;

import com.hfuu.auth.entity.TeacherStudentSelection;
import com.hfuu.auth.entity.UserInfo;

import java.util.List;

/**
 * 导师学生选择关系服务接口
 * 
 * @author hfuu
 */
public interface TeacherStudentSelectionService {
    
    /**
     * 学生选择导师
     * @param studentId 学生ID
     * @param teacherId 导师ID
     * @return 选择结果
     */
    TeacherStudentSelection selectTeacher(Long studentId, Long teacherId);
    
    /**
     * 学生放弃选择导师
     * @param selectionId 选择记录ID
     * @return 放弃结果
     */
    TeacherStudentSelection giveUpSelection(Long selectionId);
    
    /**
     * 导师同意学生选择
     * @param selectionId 选择记录ID
     * @return 同意结果
     */
    TeacherStudentSelection approveSelection(Long selectionId);
    
    /**
     * 导师拒绝学生选择
     * @param selectionId 选择记录ID
     * @return 拒绝结果
     */
    TeacherStudentSelection rejectSelection(Long selectionId);
    
    /**
     * 根据学生ID查询选择记录
     * @param studentId 学生ID
     * @return 选择记录列表
     */
    List<TeacherStudentSelection> getSelectionsByStudentId(Long studentId);
    
    /**
     * 根据导师ID查询选择记录
     * @param teacherId 导师ID
     * @return 选择记录列表
     */
    List<TeacherStudentSelection> getSelectionsByTeacherId(Long teacherId);
    
    /**
     * 根据选择记录ID查询详情
     * @param selectionId 选择记录ID
     * @return 选择记录详情
     */
    TeacherStudentSelection getSelectionById(Long selectionId);
    
    /**
     * 查询已匹配的记录
     * @return 匹配记录列表
     */
    List<TeacherStudentSelection> getMatchedSelections();
    
    /**
     * 获取所有导师列表
     * @return 导师列表
     */
    List<UserInfo> getAllTeachers();
    
    /**
     * 获取导师已同意的学生数量
     * @param teacherId 导师ID
     * @return 学生数量
     */
    Integer getApprovedStudentCount(Long teacherId);
    
    /**
     * 获取学生已选择的导师数量
     * @param studentId 学生ID
     * @return 导师数量
     */
    Integer getSelectedTeacherCount(Long studentId);
    
    /**
     * 执行自动匹配算法
     * @return 匹配结果
     */
    List<TeacherStudentSelection> executeAutoMatching();
    
    /**
     * 重置所有匹配记录
     * @return 重置结果
     */
    boolean resetAllSelections();
}