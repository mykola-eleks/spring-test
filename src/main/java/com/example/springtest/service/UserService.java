package com.example.springtest.service;

import com.example.springtest.entity.User;
import com.example.springtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        validateUser(user);
        
        if (userRepository.existsByName(user.getName())) {
            throw new IllegalArgumentException("User with name '" + user.getName() + "' already exists");
        }
        
        if (user.getNickname() != null && userRepository.existsByNickname(user.getNickname())) {
            throw new IllegalArgumentException("User with nickname '" + user.getNickname() + "' already exists");
        }
        
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        validateUser(userDetails);
        
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        
        User user = optionalUser.get();
        user.setName(userDetails.getName());
        user.setNickname(userDetails.getNickname());
        
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
    }
}
