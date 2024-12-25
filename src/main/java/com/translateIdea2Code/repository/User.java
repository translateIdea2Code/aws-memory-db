package com.translateIdea2Code.repository;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * Represents a User entity stored in Redis.
 * This class is annotated with @RedisHash to indicate that it is a Redis hash.
 * The @Data annotation from Lombok generates getters, setters, toString, equals, and hashCode methods.
 */
@Data
@RedisHash("User")
public class User {

    @Id
    private String id;

    private String name;

    private String email;
}