package com.hfuu.auth.controller;

import com.hfuu.auth.entity.StudentInfo;
import com.hfuu.auth.entity.TeacherInfo;
import com.hfuu.auth.entity.UserInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员控制器
 * 
 * @author hfuu
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /**
     * 获取所有用户列表
     * 
     * @return 用户列表
     */
    @GetMapping("/users")
    public List<UserInfo> getAllUsers() {
        List<UserInfo> users = new ArrayList<>();
        
        UserInfo user1 = new UserInfo();
        user1.setUserId(1L);
        user1.setUsername("student");
        user1.setRealName("张三");
        user1.setUserType(1);
        users.add(user1);
        
        UserInfo user2 = new UserInfo();
        user2.setUserId(2L);
        user2.setUsername("teacher");
        user2.setRealName("李四");
        user2.setUserType(2);
        users.add(user2);
        
        UserInfo user3 = new UserInfo();
        user3.setUserId(3L);
        user3.setUsername("admin");
        user3.setRealName("系统管理员");
        user3.setUserType(4);
        users.add(user3);
        
        return users;
    }

    /**
     * 获取所有学生列表
     * 
     * @return 学生列表
     */
    @GetMapping("/students")
    public List<StudentInfo> getAllStudents() {
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
     * 获取所有教师列表
     * 
     * @return 教师列表
     */
    @GetMapping("/teachers")
    public List<TeacherInfo> getAllTeachers() {
        List<TeacherInfo> teachers = new ArrayList<>();
        
        TeacherInfo teacher1 = new TeacherInfo();
        teacher1.setTeacherId(1001L);
        teacher1.setUserId(3L);
        teacher1.setTeacherNo("T1001");
        teacher1.setName("王五");
        teacher1.setGender("男");
        teacher1.setTitle("教授");
        teacher1.setCollege("金融学院");
        teacher1.setResearchDirection("金融科技");
        teacher1.setCurrentStudentCount(2);
        teacher1.setMaxStudentCount(10);
        teachers.add(teacher1);
        
        return teachers;
    }

    /**
     * 创建用户
     * 
     * @param user 用户信息
     * @return 创建结果
     */
    @PostMapping("/create-user")
    public String createUser(@RequestBody UserInfo user) {
        // 模拟创建用户逻辑
        return "创建成功";
    }

    /**
     * 删除用户
     * 
     * @param userId 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/delete-user/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        // 模拟删除用户逻辑
        return "删除成功";
    }
}
