package com.hfuu.auth.controller;

import com.hfuu.auth.entity.StudentInfo;
import com.hfuu.auth.entity.ThesisTopic;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 教师控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    /**
     * 获取教师指导的学生列表
     * 
     * @return 学生列表
     */
    @GetMapping("/students")
    public List<StudentInfo> getSupervisedStudents() {
        List<StudentInfo> students = new ArrayList<>();
        
        StudentInfo student1 = new StudentInfo();
        student1.setStudentId(1L);
        student1.setUserId(1L);
        student1.setStudentNo("2021001");
        student1.setName("张三");
        student1.setGender("男");
        student1.setClassName("金融1班");
        student1.setMajor("金融学");
        student1.setGrade("2021");
        student1.setAdvisorId(1001L);
        student1.setStatus(1);
        students.add(student1);
        
        StudentInfo student2 = new StudentInfo();
        student2.setStudentId(2L);
        student2.setUserId(2L);
        student2.setStudentNo("2021002");
        student2.setName("李四");
        student2.setGender("女");
        student2.setClassName("金融1班");
        student2.setMajor("金融学");
        student2.setGrade("2021");
        student2.setAdvisorId(1001L);
        student2.setStatus(1);
        students.add(student2);
        
        return students;
    }

    /**
     * 获取教师发布的论文题目
     * 
     * @return 论文题目列表
     */
    @GetMapping("/my-topics")
    public List<ThesisTopic> getMyTopics() {
        List<ThesisTopic> topics = new ArrayList<>();
        
        ThesisTopic topic1 = new ThesisTopic();
        topic1.setTopicId(1L);
        topic1.setTopicName("金融科技发展趋势研究");
        topic1.setTopicDescription("本课题主要研究金融科技的发展历程、现状及未来趋势");
        topic1.setTeacherId(1001L);
        topic1.setTeacherName("李四");
        topic1.setTopicType("研究型");
        topic1.setMaxStudentCount(5);
        topic1.setCurrentStudentCount(2);
        topic1.setStatus(2);
        topic1.setCreateTime("2025-09-01 10:00:00");
        topic1.setUpdateTime("2025-09-01 10:00:00");
        topics.add(topic1);
        
        ThesisTopic topic2 = new ThesisTopic();
        topic2.setTopicId(3L);
        topic2.setTopicName("区块链技术在金融领域的应用");
        topic2.setTopicDescription("本课题主要研究区块链技术在金融领域的应用场景及挑战");
        topic2.setTeacherId(1001L);
        topic2.setTeacherName("李四");
        topic2.setTopicType("技术型");
        topic2.setMaxStudentCount(3);
        topic2.setCurrentStudentCount(0);
        topic2.setStatus(2);
        topic2.setCreateTime("2025-09-03 09:15:00");
        topic2.setUpdateTime("2025-09-03 09:15:00");
        topics.add(topic2);
        
        return topics;
    }

    /**
     * 发布论文题目
     * 
     * @param topic 论文题目
     * @return 发布结果
     */
    @PostMapping("/publish-topic")
    public String publishTopic(@RequestBody ThesisTopic topic) {
        // 模拟发布题目逻辑
        return "发布成功";
    }
}
