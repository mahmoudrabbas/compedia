package com.compedia.repositories;

import com.compedia.entities.RefreshToken;
import com.compedia.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(UserEntity user);
    void deleteByToken(String token);
}
