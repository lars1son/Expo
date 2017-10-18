package com.repositories;

import com.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String username);

    @Modifying
    @Query("UPDATE UserEntity u SET u.email = :newEmail WHERE u.email =:email")
    void update(@Param(value = "email") String email, @Param(value = "newEmail") String newEmail);
}
