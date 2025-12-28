package com.hfuu.auth.repository;

import com.hfuu.auth.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 系统日志数据访问接口
 *
 * @author hfuu
 */
@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}
