package com.translateIdea2Code.controller;

import com.translateIdea2Code.repository.User;
import com.translateIdea2Code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable String id) {
        return userService.getUser(id);
    }

    @GetMapping
    public Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/generate")
    public Iterable<User> generateUsers() {
        return userService.generateUsers();
    }
}
