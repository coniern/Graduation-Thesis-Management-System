package com.hfuu.auth.controller;

import com.hfuu.auth.entity.StudentInfo;
import com.hfuu.auth.entity.ThesisTopic;
import com.hfuu.auth.repository.ThesisTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private ThesisTopicRepository thesisTopicRepository;

    /**
     * 获取教师指导的学生列表
     * 
     * @return 学生列表
     */
    @GetMapping("/students")
    public List<StudentInfo> getSupervisedStudents() {
        // TODO: 实现根据教师ID获取指导学生列表的逻辑
        return List.of();
    }

    /**
     * 获取教师发布的论文题目
     * 
     * @return 论文题目列表
     */
    @GetMapping("/my-topics")
    public ResponseEntity<List<ThesisTopic>> getMyTopics() {
        List<ThesisTopic> topics = thesisTopicRepository.findAll();
        return ResponseEntity.ok(topics);
    }

    /**
     * 发布论文题目
     * 
     * @param topic 论文题目
     * @return 发布结果
     */
    @PostMapping("/publish-topic")
    public ResponseEntity<ThesisTopic> publishTopic(@RequestBody ThesisTopic topic) {
        ThesisTopic savedTopic = thesisTopicRepository.save(topic);
        return ResponseEntity.ok(savedTopic);
    }

    /**
     * 更新论文题目
     * 
     * @param topicId 题目ID
     * @param topic 题目信息
     * @return 更新结果
     */
    @PutMapping("/update-topic/{topicId}")
    public ResponseEntity<ThesisTopic> updateTopic(@PathVariable Long topicId, @RequestBody ThesisTopic topic) {
        if (!thesisTopicRepository.existsById(topicId)) {
            return ResponseEntity.notFound().build();
        }
        topic.setTopicId(topicId);
        ThesisTopic updatedTopic = thesisTopicRepository.save(topic);
        return ResponseEntity.ok(updatedTopic);
    }

    /**
     * 删除论文题目
     * 
     * @param topicId 题目ID
     * @return 删除结果
     */
    @DeleteMapping("/delete-topic/{topicId}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long topicId) {
        if (!thesisTopicRepository.existsById(topicId)) {
            return ResponseEntity.notFound().build();
        }
        thesisTopicRepository.deleteById(topicId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 撤回论文题目
     * 
     * @param topicId 题目ID
     * @return 撤回结果
     */
    @PutMapping("/withdraw-topic/{topicId}")
    public ResponseEntity<ThesisTopic> withdrawTopic(@PathVariable Long topicId) {
        return thesisTopicRepository.findById(topicId)
                .map(topic -> {
                    topic.setStatus(1); // 设置为未发布状态
                    ThesisTopic updatedTopic = thesisTopicRepository.save(topic);
                    return ResponseEntity.ok(updatedTopic);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
