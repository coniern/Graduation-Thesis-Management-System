package com.hfuu.auth.repository;

import com.hfuu.auth.entity.TeacherStudentSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 导师学生选择关系Repository
 * 
 * @author hfuu
 */
@Repository
public interface TeacherStudentSelectionRepository extends JpaRepository<TeacherStudentSelection, Long> {
    
    /**
     * 根据学生ID查询选择记录
     * @param studentId 学生ID
     * @return 选择记录列表
     */
    List<TeacherStudentSelection> findByStudentId(Long studentId);
    
    /**
     * 根据导师ID查询选择记录
     * @param teacherId 导师ID
     * @return 选择记录列表
     */
    List<TeacherStudentSelection> findByTeacherId(Long teacherId);
    
    /**
     * 根据学生ID和导师ID查询选择记录
     * @param studentId 学生ID
     * @param teacherId 导师ID
     * @return 选择记录
     */
    TeacherStudentSelection findByStudentIdAndTeacherId(Long studentId, Long teacherId);
    
    /**
     * 根据导师ID和导师确认状态查询选择记录
     * @param teacherId 导师ID
     * @param teacherStatus 导师确认状态
     * @return 选择记录列表
     */
    List<TeacherStudentSelection> findByTeacherIdAndTeacherStatus(Long teacherId, Integer teacherStatus);
    
    /**
     * 根据学生ID和学生选择状态查询选择记录
     * @param studentId 学生ID
     * @param studentStatus 学生选择状态
     * @return 选择记录列表
     */
    List<TeacherStudentSelection> findByStudentIdAndStudentStatus(Long studentId, Integer studentStatus);
    
    /**
     * 查询已匹配的记录
     * @param matchStatus 匹配状态
     * @return 匹配记录列表
     */
    List<TeacherStudentSelection> findByMatchStatus(Integer matchStatus);
    
    /**
     * 查询导师已同意的学生数量
     * @param teacherId 导师ID
     * @param teacherStatus 导师确认状态
     * @return 学生数量
     */
    @Query("SELECT COUNT(t) FROM TeacherStudentSelection t WHERE t.teacherId = ?1 AND t.teacherStatus = ?2")
    Integer countByTeacherIdAndTeacherStatus(Long teacherId, Integer teacherStatus);
    
    /**
     * 查询学生已选择的导师数量
     * @param studentId 学生ID
     * @param studentStatus 学生选择状态
     * @return 导师数量
     */
    @Query("SELECT COUNT(t) FROM TeacherStudentSelection t WHERE t.studentId = ?1 AND t.studentStatus = ?2")
    Integer countByStudentIdAndStudentStatus(Long studentId, Integer studentStatus);
}