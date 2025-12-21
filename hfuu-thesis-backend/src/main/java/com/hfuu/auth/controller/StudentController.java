package com.hfuu.auth.controller;

import com.hfuu.auth.entity.StudentInfo;
import com.hfuu.auth.entity.ThesisTopic;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/student")
public class StudentController {

    /**
     * 获取学生个人信息
     * 
     * @return 学生信息
     */
    @GetMapping("/info")
    public StudentInfo getStudentInfo() {
        StudentInfo student = new StudentInfo();
        student.setStudentId(1L);
        student.setUserId(1L);
        student.setStudentNo("2021001");
        student.setName("张三");
        student.setGender("男");
        student.setClassName("金融1班");
        student.setMajor("金融学");
        student.setGrade("2021");
        student.setAdvisorId(1001L);
        student.setStatus(1);
        return student;
    }

    /**
     * 获取可选论文题目列表
     * 
     * @return 论文题目列表
     */
    @GetMapping("/topics")
    public List<ThesisTopic> getAvailableTopics() {
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
        topic2.setTopicId(2L);
        topic2.setTopicName("互联网金融风险管理");
        topic2.setTopicDescription("本课题主要研究互联网金融的风险类型及管理策略");
        topic2.setTeacherId(1002L);
        topic2.setTeacherName("王五");
        topic2.setTopicType("应用型");
        topic2.setMaxStudentCount(4);
        topic2.setCurrentStudentCount(1);
        topic2.setStatus(2);
        topic2.setCreateTime("2025-09-02 14:30:00");
        topic2.setUpdateTime("2025-09-02 14:30:00");
        topics.add(topic2);
        
        ThesisTopic topic3 = new ThesisTopic();
        topic3.setTopicId(3L);
        topic3.setTopicName("区块链技术在金融领域的应用");
        topic3.setTopicDescription("本课题主要研究区块链技术在金融领域的应用场景及挑战");
        topic3.setTeacherId(1001L);
        topic3.setTeacherName("李四");
        topic3.setTopicType("技术型");
        topic3.setMaxStudentCount(3);
        topic3.setCurrentStudentCount(0);
        topic3.setStatus(2);
        topic3.setCreateTime("2025-09-03 09:15:00");
        topic3.setUpdateTime("2025-09-03 09:15:00");
        topics.add(topic3);
        
        return topics;
    }

    /**
     * 选择论文题目
     * 
     * @param topicId 题目ID
     * @return 选择结果
     */
    @PostMapping("/select-topic/{topicId}")
    public String selectTopic(@PathVariable Long topicId) {
        // 模拟选择题目逻辑
        return "选择成功";
    }
}
