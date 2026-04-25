package com.englishtyping.repository;

import com.englishtyping.entity.TypingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 打字会话数据访问接口
 */
@Repository
public interface TypingSessionRepository extends JpaRepository<TypingSession, Integer> {

    /**
     * 根据用户 ID 查询所有打字会话
     */
    List<TypingSession> findByUserId(String userId);

    /**
     * 根据用户 ID 删除所有打字会话
     */
    void deleteByUserId(String userId);
}
