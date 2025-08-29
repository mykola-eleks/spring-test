package com.example.springtest.controller;

import com.example.springtest.entity.User;
import com.example.springtest.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        logger.info("GET /api/users - Fetching all users");
        List<User> users = userService.getAllUsers();
        logger.info("GET /api/users - Returning {} users", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        logger.info("GET /api/users/{} - Fetching user by id", id);
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            logger.info("GET /api/users/{} - User found, returning 200 OK", id);
            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("GET /api/users/{} - User not found, returning 404 NOT FOUND", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("POST /api/users - Creating user with name: {}, nickname: {}", user.getName(), user.getNickname());
        try {
            User savedUser = userService.createUser(user);
            logger.info("POST /api/users - User created successfully with id: {}, returning 200 OK", savedUser.getId());
            return ResponseEntity.ok(savedUser);
        } catch (IllegalArgumentException e) {
            logger.error("POST /api/users - User creation failed: {}, returning 400 BAD REQUEST", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        logger.info("PUT /api/users/{} - Updating user with name: {}, nickname: {}", id, userDetails.getName(), userDetails.getNickname());
        try {
            User updatedUser = userService.updateUser(id, userDetails);
            logger.info("PUT /api/users/{} - User updated successfully, returning 200 OK", id);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            logger.error("PUT /api/users/{} - User update failed: {}, returning 404 NOT FOUND", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        logger.info("DELETE /api/users/{} - Deleting user", id);
        try {
            userService.deleteUser(id);
            logger.info("DELETE /api/users/{} - User deleted successfully, returning 200 OK", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            logger.error("DELETE /api/users/{} - User deletion failed: {}, returning 404 NOT FOUND", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
