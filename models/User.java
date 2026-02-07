package models;

import models.enums.Role;

public class User {
    private final String userId;
    private final String name;
    private final Role role;

    // Constructor
    public User(String userId, String name, Role role) {
        if(userId == null || userId.isEmpty()) { // Validate userId
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        if(name == null || name.isEmpty()) { // Validate name
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if(role == null) { // Validate role
            throw new IllegalArgumentException("Role cannot be null");
        }

        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    
}
