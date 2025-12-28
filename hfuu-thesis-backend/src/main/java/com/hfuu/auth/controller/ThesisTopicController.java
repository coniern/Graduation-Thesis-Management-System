package com.hfuu.auth.controller;

import com.hfuu.auth.entity.ThesisTopic;
import com.hfuu.auth.repository.ThesisTopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 论文题目与选题管理控制器
 *
 * @author hfuu
 */
@RestController
@RequestMapping("/api/admin/topic")
public class ThesisTopicController {

    @Autowired
    private ThesisTopicRepository thesisTopicRepository;

    /**
     * 获取所有题目列表
     *
     * @return 题目列表
     */
    @GetMapping
    public ResponseEntity<List<ThesisTopic>> getTopicList() {
        List<ThesisTopic> topics = thesisTopicRepository.findAll();
        return ResponseEntity.ok(topics);
    }

    /**
     * 根据ID获取题目详情
     *
     * @param topicId 题目ID
     * @return 题目详情
     */
    @GetMapping("/{topicId}")
    public ResponseEntity<ThesisTopic> getTopicById(@PathVariable Long topicId) {
        return thesisTopicRepository.findById(topicId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 审核题目
     *
     * @param topicId 题目ID
     * @param status 审核状态（2-通过，3-驳回）
     * @return 审核结果
     */
    @PutMapping("/audit/{topicId}")
    public ResponseEntity<ThesisTopic> auditTopic(@PathVariable Long topicId, @RequestBody AuditRequest auditRequest) {
        return thesisTopicRepository.findById(topicId)
                .map(topic -> {
                    topic.setStatus(auditRequest.getStatus());
                    ThesisTopic updatedTopic = thesisTopicRepository.save(topic);
                    return ResponseEntity.ok(updatedTopic);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 删除题目
     *
     * @param topicId 题目ID
     * @return 删除结果
     */
    @DeleteMapping("/{topicId}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Long topicId) {
        if (!thesisTopicRepository.existsById(topicId)) {
            return ResponseEntity.notFound().build();
        }
        thesisTopicRepository.deleteById(topicId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 批量删除题目
     *
     * @param topicIds 题目ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Void> batchDeleteTopics(@RequestBody List<Long> topicIds) {
        thesisTopicRepository.deleteAllById(topicIds);
        return ResponseEntity.noContent().build();
    }

    /**
     * 审核请求DTO
     */
    static class AuditRequest {
        private Integer status;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }
}
