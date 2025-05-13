package model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a user in the system with authentication capabilities
 */
public class User {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String fullName;
    private boolean isAdmin;
    private boolean isLoggedIn;
    private LocalDateTime lastLoginTime;
    private LocalDateTime registrationDate;

    /**
     * Constructor for guest users (no authentication)
     */
    public User(String username) {
        this.username = username;
        this.password = null;
        this.isAdmin = false;
        this.isLoggedIn = false;
        this.registrationDate = LocalDateTime.now();
    }

    /**
     * Constructor for creating a new registered user
     */
    public User(String username, String password, String email, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.isAdmin = false;
        this.isLoggedIn = false;
        this.registrationDate = LocalDateTime.now();
    }

    /**
     * Constructor for creating an admin user
     */
    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isLoggedIn = false;
        this.registrationDate = LocalDateTime.now();
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
        if (loggedIn) {
            this.lastLoginTime = LocalDateTime.now();
        }
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Validates if the provided password matches this user's password
     */
    public boolean validatePassword(String inputPassword) {
        return password != null && password.equals(inputPassword);
    }

    @Override
    public String toString() {
        return username;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}