package com.hfuu.auth.repository;

import com.hfuu.auth.entity.DefenseGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 答辩分组数据访问接口
 *
 * @author hfuu
 */
@Repository
public interface DefenseGroupRepository extends JpaRepository<DefenseGroup, Long> {
}
