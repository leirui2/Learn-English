package com.englishtyping.repository;

import com.englishtyping.entity.User;
import com.englishtyping.entity.UserRole;
import com.englishtyping.entity.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 * @author lei
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /** 根据邮箱查找用户 */
    Optional<User> findByEmail(String email);

    /** 根据用户名查找用户 */
    Optional<User> findByUsername(String username);

    /** 判断邮箱是否已存在 */
    boolean existsByEmail(String email);

    /** 判断是否存在指定角色的用户 */
    boolean existsByRole(UserRole role);

    /**
     * 根据条件查询用户列表（分页）
     * 支持按用户名或邮箱搜索，按角色和状态筛选
     */
    @Query("SELECT u FROM User u WHERE " +
            "(:keyword IS NULL OR u.username LIKE %:keyword% OR u.email LIKE %:keyword%) AND " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:status IS NULL OR u.status = :status) " +
            "ORDER BY u.createdAt DESC")
    Page<User> findByConditions(
            @Param("keyword") String keyword,
            @Param("role") UserRole role,
            @Param("status") UserStatus status,
            Pageable pageable
    );
}
