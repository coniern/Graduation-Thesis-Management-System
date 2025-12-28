package com.hfuu.auth.repository;

import com.hfuu.auth.entity.ThesisProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 论文流程与时间管理数据访问接口
 *
 * @author hfuu
 */
@Repository
public interface ThesisProcessRepository extends JpaRepository<ThesisProcess, Long> {
}
