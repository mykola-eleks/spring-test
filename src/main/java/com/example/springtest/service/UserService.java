package com.example.springtest.service;

import com.example.springtest.entity.User;
import com.example.springtest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = userRepository.findAll();
        logger.info("Found {} users", users.size());
        return users;
    }

    public Optional<User> getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.info("Found user: {}", user.get().getName());
        } else {
            logger.warn("User with id {} not found", id);
        }
        return user;
    }

    public User createUser(User user) {
        logger.info("Creating new user with name: {}, nickname: {}", user.getName(), user.getNickname());
        validateUser(user);
        
        if (userRepository.existsByName(user.getName())) {
            logger.error("User creation failed: User with name '{}' already exists", user.getName());
            throw new IllegalArgumentException("User with name '" + user.getName() + "' already exists");
        }
        
        if (user.getNickname() != null && userRepository.existsByNickname(user.getNickname())) {
            logger.error("User creation failed: User with nickname '{}' already exists", user.getNickname());
            throw new IllegalArgumentException("User with nickname '" + user.getNickname() + "' already exists");
        }
        
        User savedUser = userRepository.save(user);
        logger.info("Successfully created user with id: {}, name: {}", savedUser.getId(), savedUser.getName());
        return savedUser;
    }

    public User updateUser(Long id, User userDetails) {
        logger.info("Updating user with id: {}, new name: {}, new nickname: {}", id, userDetails.getName(), userDetails.getNickname());
        validateUser(userDetails);
        
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            logger.error("User update failed: User with id {} not found", id);
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        
        User user = optionalUser.get();
        String oldName = user.getName();
        String oldNickname = user.getNickname();
        
        user.setName(userDetails.getName());
        user.setNickname(userDetails.getNickname());
        
        User updatedUser = userRepository.save(user);
        logger.info("Successfully updated user id: {} - name: '{}' -> '{}', nickname: '{}' -> '{}'", 
                   id, oldName, updatedUser.getName(), oldNickname, updatedUser.getNickname());
        return updatedUser;
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            logger.error("User deletion failed: User with id {} not found", id);
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
        logger.info("Successfully deleted user with id: {}", id);
    }

    private void validateUser(User user) {
        logger.debug("Validating user: {}", user.getName());
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            logger.error("User validation failed: name is null or empty");
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        logger.debug("User validation passed for: {}", user.getName());
    }
}
