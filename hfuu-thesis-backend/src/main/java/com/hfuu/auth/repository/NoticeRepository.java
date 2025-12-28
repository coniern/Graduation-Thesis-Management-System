package com.hfuu.auth.repository;

import com.hfuu.auth.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 通知公告数据访问接口
 *
 * @author hfuu
 */
@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
