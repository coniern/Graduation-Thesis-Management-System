package com.hfuu.auth.repository;

import com.hfuu.auth.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户信息数据访问接口
 * 
 * @author hfuu
 */
@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    UserInfo findByUsername(String username);
    
    /**
     * 根据用户名判断用户是否存在
     * 
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
}
