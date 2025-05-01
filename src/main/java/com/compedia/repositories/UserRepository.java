package com.compedia.repositories;

import com.compedia.entities.UserEntity;
import com.compedia.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    @Query(value = "Select u from UserEntity u join u.roles r where r.roleName=:roleName")
    List<UserEntity> findUsersByRoleName(@Param("roleName") RoleName roleName);
}
