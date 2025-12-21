package com.hfuu.auth;

import com.hfuu.auth.entity.UserInfo;
import com.hfuu.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 初始化数据
 * 
 * @author hfuu
 */
@Configuration
public class InitData {
    
    @Bean
    public CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // 检查是否已有用户数据
            if (userRepository.count() == 0) {
                // 创建测试用户：学生
                UserInfo student = new UserInfo();
                student.setUsername("student");
                student.setPassword(passwordEncoder.encode("123456"));
                student.setRealName("张三");
                student.setUserType(1);
                student.setStatus(1);
                userRepository.save(student);
                
                // 创建测试用户：教师
                UserInfo teacher = new UserInfo();
                teacher.setUsername("teacher");
                teacher.setPassword(passwordEncoder.encode("123456"));
                teacher.setRealName("李四");
                teacher.setUserType(2);
                teacher.setStatus(1);
                userRepository.save(teacher);
                
                // 创建测试用户：管理员
                UserInfo admin = new UserInfo();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("123456"));
                admin.setRealName("系统管理员");
                admin.setUserType(4);
                admin.setStatus(1);
                userRepository.save(admin);
                
                System.out.println("测试用户数据初始化完成！");
            }
        };
    }
}
