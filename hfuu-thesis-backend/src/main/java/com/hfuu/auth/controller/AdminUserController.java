package com.hfuu.auth.controller;

import com.hfuu.auth.entity.UserInfo;
import com.hfuu.auth.repository.UserRepository;
import com.hfuu.auth.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员用户管理控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    @GetMapping
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<UserInfo> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * 根据ID获取用户
     * 
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserInfo> getUserById(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 创建的用户
     */
    @PostMapping
    public ResponseEntity<UserInfo> createUser(@RequestBody UserInfo user) {
        // 加密密码
        user.setPassword(PasswordUtils.encodePassword(user.getPassword()));
        UserInfo savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * 批量导入用户
     * 
     * @param users 用户列表
     * @return 导入结果
     */
    @PostMapping("/batch")
    public ResponseEntity<String> batchImportUsers(@RequestBody List<UserInfo> users) {
        // 加密所有用户的密码
        users.forEach(user -> user.setPassword(PasswordUtils.encodePassword(user.getPassword())));
        userRepository.saveAll(users);
        return new ResponseEntity<>("成功导入 " + users.size() + " 个用户", HttpStatus.CREATED);
    }

    /**
     * 更新用户
     * 
     * @param userId 用户ID
     * @param user 用户信息
     * @return 更新后的用户
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserInfo> updateUser(@PathVariable Long userId, @RequestBody UserInfo user) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    // 更新用户信息
                    existingUser.setUsername(user.getUsername());
                    existingUser.setRealName(user.getRealName());
                    existingUser.setUserType(user.getUserType());
                    existingUser.setStatus(user.getStatus());
                    // 如果密码不为空，更新密码
                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        existingUser.setPassword(PasswordUtils.encodePassword(user.getPassword()));
                    }
                    UserInfo updatedUser = userRepository.save(existingUser);
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 批量删除用户
     * 
     * @param userIds 用户ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public ResponseEntity<String> batchDeleteUsers(@RequestBody List<Long> userIds) {
        userRepository.deleteAllById(userIds);
        return new ResponseEntity<>("成功删除 " + userIds.size() + " 个用户", HttpStatus.NO_CONTENT);
    }
}
