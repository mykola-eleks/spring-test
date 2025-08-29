package com.example.springtest.repository;

import com.example.springtest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByName(String name);
    
    Optional<User> findByNickname(String nickname);
    
    boolean existsByName(String name);
    
    boolean existsByNickname(String nickname);
}
