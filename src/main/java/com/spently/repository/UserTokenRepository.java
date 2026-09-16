package com.spently.repository;

import com.spently.entity.UserToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {
    Boolean existsByToken(String token);

    @Transactional
    @Modifying
    void deleteByToken(String token);
}
