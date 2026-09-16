package com.spently.repository;

import com.spently.dto.projection.UserProjection;
import com.spently.dto.request.user.UserFilterRequest;
import com.spently.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = """
        select u from User u where u.username = :username or u.email = :username
    """)
    Optional<User> findByUsername(String username);

    List<User> findByUsernameIn(Collection<String> usernames);

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "company")
    @Query(value = "select u from User u where u.userId = :id ")
    Optional<UserProjection> findUserById(String id);

    @Query(value = "select u.userId from User u where u.email = :email")
    Optional<String> findUserIdByEmail(String email);

    @Query("""
        select u.userId
        from User u
        where u.email = :email
        and u.userId <> :id
    """)
    Optional<String> findUserIdByEmailNotId(String id, String email);

    @Query(value = """
        SELECT u.userId
        FROM User u
        WHERE 1 = 1
        AND (
            :#{#filterRequest.keyword} IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%',:#{#filterRequest.keyword}, '%'))
        )
        AND (
            :#{#filterRequest.role} IS NULL OR u.role = :#{#filterRequest.role}
        )
        AND (
            :#{#filterRequest.status} IS NULL OR u.status = :#{#filterRequest.status}
        )
        ORDER BY
                CASE\s
                    WHEN u.role = 'ADMIN' THEN 1
                    WHEN u.role = 'USER' THEN 2
                    ELSE 3
                END ASC,
                u.status DESC,
                u.createdAt DESC, u.updatedAt desc
    """)
    Page<String> getListIdUser(UserFilterRequest filterRequest, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.userId IN :ids")
    List <UserProjection> getListUser(List<String> ids);

    @Query("""
        SELECT COUNT(u.userId)
        FROM User u
        WHERE (:role IS NULL OR u.role = :role)
    """)
    Long countUserByRole(String role);

    @Query("""
        SELECT COUNT(u.userId)
        FROM User u
        WHERE (:status IS NULL OR u.status = :status)
    """)
    Long countUserByStatus(Integer status);
}
