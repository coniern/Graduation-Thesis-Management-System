package com.hfuu.auth.controller;

import com.hfuu.auth.entity.Notice;
import com.hfuu.auth.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 通知公告控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    @Autowired
    private NoticeRepository noticeRepository;

    /**
     * 获取通知列表
     * 
     * @param noticeType 通知类型
     * @param status 状态
     * @return 通知列表
     */
    @GetMapping
    public ResponseEntity<List<Notice>> getNoticeList(@RequestParam(required = false) Integer noticeType, @RequestParam(required = false) Integer status) {
        List<Notice> notices = noticeRepository.findAll();
        return ResponseEntity.ok(notices);
    }

    /**
     * 获取通知详情
     * 
     * @param noticeId 通知ID
     * @return 通知详情
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<Notice> getNoticeById(@PathVariable Long noticeId) {
        Optional<Notice> notice = noticeRepository.findById(noticeId);
        return notice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 发布通知
     * 
     * @param notice 通知信息
     * @return 发布结果
     */
    @PostMapping
    public ResponseEntity<Notice> publishNotice(@RequestBody Notice notice) {
        Notice savedNotice = noticeRepository.save(notice);
        return ResponseEntity.ok(savedNotice);
    }

    /**
     * 更新通知
     * 
     * @param noticeId 通知ID
     * @param notice 通知信息
     * @return 更新结果
     */
    @PutMapping("/{noticeId}")
    public ResponseEntity<Notice> updateNotice(@PathVariable Long noticeId, @RequestBody Notice notice) {
        if (!noticeRepository.existsById(noticeId)) {
            return ResponseEntity.notFound().build();
        }
        notice.setNoticeId(noticeId);
        Notice updatedNotice = noticeRepository.save(notice);
        return ResponseEntity.ok(updatedNotice);
    }

    /**
     * 删除通知
     * 
     * @param noticeId 通知ID
     * @return 删除结果
     */
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        if (!noticeRepository.existsById(noticeId)) {
            return ResponseEntity.notFound().build();
        }
        noticeRepository.deleteById(noticeId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取未读通知数量
     * 
     * @param userId 用户ID
     * @return 未读通知数量
     */
    @GetMapping("/unread-count/{userId}")
    public ResponseEntity<Integer> getUnreadCount(@PathVariable Long userId) {
        // 模拟获取未读通知数量
        return ResponseEntity.ok(3);
    }
}
