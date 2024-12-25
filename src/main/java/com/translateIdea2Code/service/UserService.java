package com.translateIdea2Code.service;

import com.translateIdea2Code.repository.User;
import com.translateIdea2Code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Service class for managing user-related operations.
 *
 * @author vinod_bokare
 */
@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    public static Iterable<User> getUsers(UserRepository userRepository) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setName("User" + i);
            user.setEmail("user" + i + "@example.com");
            users.add(user);
        }
        return userRepository.saveAll(users);
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    public User createUser(User user) {
        try {
            logger.info("Creating user: " + user);
            return userRepository.save(user);
        } catch (Exception e) {
            logger.severe("Error while creating user: " + e.getMessage());
            throw new RuntimeException("Unable to create user.", e);
        }
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the ID of the user
     * @return the user if found
     */
    public Optional<User> getUser(String id) {
        try {
            logger.info("Fetching user with ID: " + id);
            return userRepository.findById(id);
        } catch (Exception e) {
            logger.severe("Error while fetching user with ID " + id + ": " + e.getMessage());
            throw new RuntimeException("Unable to fetch user.", e);
        }
    }

    /**
     * Retrieves all users.
     *
     * @return list of all users
     */
    public Iterable<User> getAllUsers() {
        try {
            logger.info("Fetching all users");
            return userRepository.findAll();
        } catch (Exception e) {
            logger.severe("Error while fetching all users: " + e.getMessage());
            throw new RuntimeException("Unable to fetch all users.", e);
        }
    }

    /**
     * Generates 1000 users with dummy data.
     *
     * @return list of generated users
     */
    public Iterable<User> generateUsers() {
        try {
            logger.info("Generating 1000 users");
            return getUsers(userRepository);
        } catch (Exception e) {
            logger.severe("Error while generating users: " + e.getMessage());
            throw new RuntimeException("Unable to generate users.", e);
        }
    }
}
