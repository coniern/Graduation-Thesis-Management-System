package com.hfuu.auth.repository;

import com.hfuu.auth.entity.ThesisTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 论文题目数据访问接口
 *
 * @author hfuu
 */
@Repository
public interface ThesisTopicRepository extends JpaRepository<ThesisTopic, Long> {
}
