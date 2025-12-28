package com.hfuu.auth.controller;

import com.hfuu.auth.entity.DefenseGroup;
import com.hfuu.auth.repository.DefenseGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 答辩分组管理控制器
 *
 * @author hfuu
 */
@RestController
@RequestMapping("/api/admin/defense-group")
public class DefenseGroupController {

    @Autowired
    private DefenseGroupRepository defenseGroupRepository;

    /**
     * 获取所有答辩分组
     *
     * @return 分组列表
     */
    @GetMapping
    public ResponseEntity<List<DefenseGroup>> getDefenseGroupList() {
        List<DefenseGroup> groups = defenseGroupRepository.findAll();
        return ResponseEntity.ok(groups);
    }

    /**
     * 根据ID获取分组详情
     *
     * @param groupId 分组ID
     * @return 分组详情
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<DefenseGroup> getDefenseGroupById(@PathVariable Long groupId) {
        return defenseGroupRepository.findById(groupId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 创建答辩分组
     *
     * @param group 分组信息
     * @return 创建结果
     */
    @PostMapping
    public ResponseEntity<DefenseGroup> createDefenseGroup(@RequestBody DefenseGroup group) {
        DefenseGroup savedGroup = defenseGroupRepository.save(group);
        return ResponseEntity.ok(savedGroup);
    }

    /**
     * 更新答辩分组
     *
     * @param groupId 分组ID
     * @param group 分组信息
     * @return 更新结果
     */
    @PutMapping("/{groupId}")
    public ResponseEntity<DefenseGroup> updateDefenseGroup(@PathVariable Long groupId, @RequestBody DefenseGroup group) {
        if (!defenseGroupRepository.existsById(groupId)) {
            return ResponseEntity.notFound().build();
        }
        group.setGroupId(groupId);
        DefenseGroup updatedGroup = defenseGroupRepository.save(group);
        return ResponseEntity.ok(updatedGroup);
    }

    /**
     * 删除答辩分组
     *
     * @param groupId 分组ID
     * @return 删除结果
     */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteDefenseGroup(@PathVariable Long groupId) {
        if (!defenseGroupRepository.existsById(groupId)) {
            return ResponseEntity.notFound().build();
        }
        defenseGroupRepository.deleteById(groupId);
        return ResponseEntity.noContent().build();
    }
}
