package com.hfuu.auth.controller;

import com.hfuu.auth.entity.ThesisProcess;
import com.hfuu.auth.repository.ThesisProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 论文流程与时间管理控制器
 *
 * @author hfuu
 */
@RestController
@RequestMapping("/api/admin/process")
public class ThesisProcessController {

    @Autowired
    private ThesisProcessRepository thesisProcessRepository;

    /**
     * 获取所有流程与时间配置
     *
     * @return 流程列表
     */
    @GetMapping
    public ResponseEntity<List<ThesisProcess>> getProcessList() {
        List<ThesisProcess> processes = thesisProcessRepository.findAll();
        return ResponseEntity.ok(processes);
    }

    /**
     * 根据ID获取流程详情
     *
     * @param processId 流程ID
     * @return 流程详情
     */
    @GetMapping("/{processId}")
    public ResponseEntity<ThesisProcess> getProcessById(@PathVariable Long processId) {
        return thesisProcessRepository.findById(processId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 创建流程配置
     *
     * @param process 流程配置信息
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<ThesisProcess> createProcess(@RequestBody ThesisProcess process) {
        ThesisProcess savedProcess = thesisProcessRepository.save(process);
        return ResponseEntity.ok(savedProcess);
    }

    /**
     * 更新流程配置
     *
     * @param processId 流程ID
     * @param process 流程配置信息
     * @return 更新结果
     */
    @PutMapping("/{processId}")
    public ResponseEntity<ThesisProcess> updateProcess(@PathVariable Long processId, @RequestBody ThesisProcess process) {
        if (!thesisProcessRepository.existsById(processId)) {
            return ResponseEntity.notFound().build();
        }
        process.setProcessId(processId);
        ThesisProcess updatedProcess = thesisProcessRepository.save(process);
        return ResponseEntity.ok(updatedProcess);
    }

    /**
     * 开启/关闭流程权限
     *
     * @param processId 流程ID
     * @param permissionEnabled 是否开启权限：0-关闭，1-开启
     * @return 操作结果
     */
    @PutMapping("/permission/{processId}")
    public ResponseEntity<ThesisProcess> updateProcessPermission(@PathVariable Long processId, @RequestBody PermissionRequest request) {
        return thesisProcessRepository.findById(processId)
                .map(process -> {
                    process.setPermissionEnabled(request.getPermissionEnabled());
                    ThesisProcess updatedProcess = thesisProcessRepository.save(process);
                    return ResponseEntity.ok(updatedProcess);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 删除流程配置
     *
     * @param processId 流程ID
     * @return 删除结果
     */
    @DeleteMapping("/{processId}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Long processId) {
        if (!thesisProcessRepository.existsById(processId)) {
            return ResponseEntity.notFound().build();
        }
        thesisProcessRepository.deleteById(processId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 权限请求DTO
     */
    static class PermissionRequest {
        private Integer permissionEnabled;

        public Integer getPermissionEnabled() {
            return permissionEnabled;
        }

        public void setPermissionEnabled(Integer permissionEnabled) {
            this.permissionEnabled = permissionEnabled;
        }
    }
}
