package com.hfuu.auth.service.impl;

import com.hfuu.auth.entity.TeacherStudentSelection;
import com.hfuu.auth.entity.UserInfo;
import com.hfuu.auth.repository.TeacherStudentSelectionRepository;
import com.hfuu.auth.repository.UserInfoRepository;
import com.hfuu.auth.service.TeacherStudentSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 导师学生选择关系服务实现类
 * 
 * @author hfuu
 */
@Service
public class TeacherStudentSelectionServiceImpl implements TeacherStudentSelectionService {
    
    @Autowired
    private TeacherStudentSelectionRepository selectionRepository;
    
    @Autowired
    private UserInfoRepository userInfoRepository;
    
    @Override
    public TeacherStudentSelection selectTeacher(Long studentId, Long teacherId) {
        // 查询学生和导师信息
        UserInfo student = userInfoRepository.findById(studentId).orElse(null);
        UserInfo teacher = userInfoRepository.findById(teacherId).orElse(null);
        
        if (student == null || teacher == null) {
            throw new RuntimeException("学生或导师不存在");
        }
        
        // 检查学生是否已经选择过该导师
        TeacherStudentSelection existingSelection = selectionRepository.findByStudentIdAndTeacherId(studentId, teacherId);
        if (existingSelection != null) {
            // 如果已经选择过，更新状态为已选择
            existingSelection.setStudentStatus(1);
            existingSelection.setTeacherStatus(1); // 重置为待确认
            existingSelection.setMatchStatus(0); // 重置匹配状态
            existingSelection.setSelectionTime(new Date().toString());
            return selectionRepository.save(existingSelection);
        }
        
        // 创建新的选择记录
        TeacherStudentSelection selection = new TeacherStudentSelection();
        selection.setStudentId(studentId);
        selection.setStudentName(student.getRealName());
        selection.setTeacherId(teacherId);
        selection.setTeacherName(teacher.getRealName());
        selection.setStudentStatus(1); // 已选择
        selection.setTeacherStatus(1); // 待确认
        selection.setMatchStatus(0); // 未匹配
        selection.setSelectionTime(new Date().toString());
        
        return selectionRepository.save(selection);
    }
    
    @Override
    public TeacherStudentSelection giveUpSelection(Long selectionId) {
        TeacherStudentSelection selection = selectionRepository.findById(selectionId).orElse(null);
        if (selection == null) {
            throw new RuntimeException("选择记录不存在");
        }
        
        selection.setStudentStatus(2); // 已放弃
        selection.setTeacherStatus(3); // 设置为已拒绝
        selection.setMatchStatus(0); // 未匹配
        
        return selectionRepository.save(selection);
    }
    
    @Override
    public TeacherStudentSelection approveSelection(Long selectionId) {
        TeacherStudentSelection selection = selectionRepository.findById(selectionId).orElse(null);
        if (selection == null) {
            throw new RuntimeException("选择记录不存在");
        }
        
        selection.setTeacherStatus(2); // 已同意
        selection.setMatchStatus(1); // 已匹配
        selection.setConfirmTime(new Date().toString());
        
        return selectionRepository.save(selection);
    }
    
    @Override
    public TeacherStudentSelection rejectSelection(Long selectionId) {
        TeacherStudentSelection selection = selectionRepository.findById(selectionId).orElse(null);
        if (selection == null) {
            throw new RuntimeException("选择记录不存在");
        }
        
        selection.setTeacherStatus(3); // 已拒绝
        selection.setMatchStatus(0); // 未匹配
        selection.setConfirmTime(new Date().toString());
        
        return selectionRepository.save(selection);
    }
    
    @Override
    public List<TeacherStudentSelection> getSelectionsByStudentId(Long studentId) {
        return selectionRepository.findByStudentId(studentId);
    }
    
    @Override
    public List<TeacherStudentSelection> getSelectionsByTeacherId(Long teacherId) {
        return selectionRepository.findByTeacherId(teacherId);
    }
    
    @Override
    public TeacherStudentSelection getSelectionById(Long selectionId) {
        return selectionRepository.findById(selectionId).orElse(null);
    }
    
    @Override
    public List<TeacherStudentSelection> getMatchedSelections() {
        return selectionRepository.findByMatchStatus(1);
    }
    
    @Override
    public List<UserInfo> getAllTeachers() {
        // 查询所有用户类型为导师（2）的用户
        return userInfoRepository.findByUserType(2);
    }
    
    @Override
    public Integer getApprovedStudentCount(Long teacherId) {
        return selectionRepository.countByTeacherIdAndTeacherStatus(teacherId, 2);
    }
    
    @Override
    public Integer getSelectedTeacherCount(Long studentId) {
        return selectionRepository.countByStudentIdAndStudentStatus(studentId, 1);
    }
    
    @Override
    public List<TeacherStudentSelection> executeAutoMatching() {
        // 获取所有待确认的选择记录
        List<TeacherStudentSelection> pendingSelections = selectionRepository.findAll();
        
        // 按导师分组
        Map<Long, List<TeacherStudentSelection>> teacherSelectionsMap = new HashMap<>();
        for (TeacherStudentSelection selection : pendingSelections) {
            teacherSelectionsMap.computeIfAbsent(selection.getTeacherId(), k -> new ArrayList<>())
                .add(selection);
        }
        
        List<TeacherStudentSelection> matchedSelections = new ArrayList<>();
        
        // 遍历每个导师的选择记录
        for (Map.Entry<Long, List<TeacherStudentSelection>> entry : teacherSelectionsMap.entrySet()) {
            Long teacherId = entry.getKey();
            List<TeacherStudentSelection> selections = entry.getValue();
            
            // 按学生选择时间排序（先选择的优先）
            selections.sort(Comparator.comparing(TeacherStudentSelection::getSelectionTime));
            
            // 获取导师信息，这里可以添加导师可带学生数量的限制
            // 暂时不限制，实际应用中可以从导师信息中获取最大可带学生数
            
            // 处理每个选择记录
            for (TeacherStudentSelection selection : selections) {
                // 如果导师已同意，标记为已匹配
                if (selection.getTeacherStatus() == 2) {
                    selection.setMatchStatus(1);
                    matchedSelections.add(selectionRepository.save(selection));
                } else if (selection.getTeacherStatus() == 3) {
                    // 如果导师已拒绝，标记为未匹配
                    selection.setMatchStatus(0);
                    selectionRepository.save(selection);
                }
            }
        }
        
        return matchedSelections;
    }
    
    @Override
    public boolean resetAllSelections() {
        try {
            // 删除所有选择记录
            selectionRepository.deleteAll();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}