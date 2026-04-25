package com.englishtyping.repository;

import com.englishtyping.entity.ChallengeRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRecordRepository extends JpaRepository<ChallengeRecord, Long> {

    /** 获取用户历史记录（最近N条） */
    List<ChallengeRecord> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    /** 限时榜：按通过数降序，相同按正确率降序 */
    @Query("SELECT c FROM ChallengeRecord c WHERE c.contentType = :contentType AND c.timeMode = 'TIMED' AND c.timeLimit = :timeLimit ORDER BY c.passedCount DESC, c.accuracy DESC")
    List<ChallengeRecord> findTimedLeaderboard(
            @Param("contentType") String contentType,
            @Param("timeLimit") Integer timeLimit,
            Pageable pageable);

    /** 无限榜：按通过数降序，相同按用时升序 */
    @Query("SELECT c FROM ChallengeRecord c WHERE c.contentType = :contentType AND c.timeMode = 'INFINITE' ORDER BY c.passedCount DESC, c.timeMs ASC")
    List<ChallengeRecord> findInfiniteLeaderboard(
            @Param("contentType") String contentType,
            Pageable pageable);

    /** 获取用户在限时榜的最佳记录 */
    @Query("SELECT c FROM ChallengeRecord c WHERE c.userId = :userId AND c.contentType = :contentType AND c.timeMode = 'TIMED' AND c.timeLimit = :timeLimit ORDER BY c.passedCount DESC, c.accuracy DESC")
    List<ChallengeRecord> findUserBestTimed(
            @Param("userId") String userId,
            @Param("contentType") String contentType,
            @Param("timeLimit") Integer timeLimit,
            Pageable pageable);

    /** 获取用户在无限榜的最佳记录 */
    @Query("SELECT c FROM ChallengeRecord c WHERE c.userId = :userId AND c.contentType = :contentType AND c.timeMode = 'INFINITE' ORDER BY c.passedCount DESC, c.timeMs ASC")
    List<ChallengeRecord> findUserBestInfinite(
            @Param("userId") String userId,
            @Param("contentType") String contentType,
            Pageable pageable);
}
