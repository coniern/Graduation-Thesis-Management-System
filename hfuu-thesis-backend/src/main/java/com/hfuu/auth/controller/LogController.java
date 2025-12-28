package com.hfuu.auth.controller;

import com.hfuu.auth.entity.Log;
import com.hfuu.auth.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统日志控制器
 *
 * @author hfuu
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    /**
     * 获取日志列表
     *
     * @return 日志列表
     */
    @GetMapping
    public ResponseEntity<List<Log>> getLogList() {
        List<Log> logs = logRepository.findAll();
        return ResponseEntity.ok(logs);
    }

    /**
     * 根据ID获取日志详情
     *
     * @param logId 日志ID
     * @return 日志详情
     */
    @GetMapping("/{logId}")
    public ResponseEntity<Log> getLogById(@PathVariable Long logId) {
        return logRepository.findById(logId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 删除日志
     *
     * @param logId 日志ID
     * @return 删除结果
     */
    @DeleteMapping("/{logId}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long logId) {
        if (!logRepository.existsById(logId)) {
            return ResponseEntity.notFound().build();
        }
        logRepository.deleteById(logId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 批量删除日志
     *
     * @param logIds 日志ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<Void> batchDeleteLogs(@RequestBody List<Long> logIds) {
        logRepository.deleteAllById(logIds);
        return ResponseEntity.noContent().build();
    }
}
