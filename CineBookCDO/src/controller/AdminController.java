package controller;

import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for admin-related operations and user authentication
 */
public class AdminController {
    private List<User> users;
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";
    private User currentUser; // Tracks the currently logged-in user

    public AdminController() {
        this.users = new ArrayList<>();
        this.currentUser = null;
        initializeUsers();
    }

    /**
     * Initializes users
     */
    private void initializeUsers() {
        // Add admin user
        users.add(new User(ADMIN_USERNAME, ADMIN_PASSWORD, true));
        
        // Add some sample regular users with passwords
        users.add(new User("john", "pass123", "john@example.com", "John Smith"));
        users.add(new User("maria", "pass123", "maria@example.com", "Maria Rodriguez"));
        users.add(new User("alex", "pass123", "alex@example.com", "Alex Johnson"));
    }

    /**
     * Validates admin login credentials
     */
    public boolean validateAdminLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        // Check for the default admin
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            User adminUser = getUserByUsername(ADMIN_USERNAME);
            if (adminUser != null) {
                loginUser(adminUser);
            }
            return true;
        }
        
        // Check for other admin users
        for (User user : users) {
            if (user.isAdmin() && username.equals(user.getUsername()) && user.validatePassword(password)) {
                loginUser(user);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validates user login (both regular and admin users)
     */
    public boolean validateUserLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        User user = getUserByUsername(username);
        if (user != null && user.validatePassword(password)) {
            loginUser(user);
            return true;
        }
        
        return false;
    }
    
    /**
     * Sets the user as logged in and updates login time
     */
    private void loginUser(User user) {
        user.setLoggedIn(true);
        this.currentUser = user;
    }
    
    /**
     * Logs out the current user
     */
    public void logoutCurrentUser() {
        if (currentUser != null) {
            currentUser.setLoggedIn(false);
            currentUser = null;
        }
    }

    /**
     * Gets a user by username
     */
    public User getUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    
    /**
     * Checks if username already exists
     */
    public boolean isUsernameAvailable(String username) {
        return getUserByUsername(username) == null;
    }
    
    /**
     * Register a new user
     */
    public User registerUser(String username, String password, String email, String fullName) {
        if (!isUsernameAvailable(username)) {
            return null; // Username already exists
        }
        
        User newUser = new User(username, password, email, fullName);
        addUser(newUser);
        return newUser;
    }

    /**
     * Adds a new user
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Removes a user
     */
    public void removeUser(String username) {
        User userToRemove = null;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                userToRemove = user;
                break;
            }
        }
        
        if (userToRemove != null) {
            users.remove(userToRemove);
        }
    }
    
    /**
     * Updates a user's profile information
     */
    public boolean updateUserProfile(String username, String email, String fullName, String phoneNumber) {
        User user = getUserByUsername(username);
        if (user != null) {
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);
            return true;
        }
        return false;
    }
    
    /**
     * Changes a user's password
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = getUserByUsername(username);
        if (user != null && user.validatePassword(oldPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
    
    /**
     * Gets the currently logged in user
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user is currently logged in
     */
    public boolean isUserLoggedIn() {
        return currentUser != null;
    }

    /**
     * Gets all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Gets all admin users
     */
    public List<User> getAdminUsers() {
        List<User> adminUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isAdmin()) {
                adminUsers.add(user);
            }
        }
        return adminUsers;
    }

    /**
     * Gets all regular users
     */
    public List<User> getRegularUsers() {
        List<User> regularUsers = new ArrayList<>();
        for (User user : users) {
            if (!user.isAdmin()) {
                regularUsers.add(user);
            }
        }
        return regularUsers;
    }

    /**
     * Creates a new user
     */
    public User createUser(String username, boolean isAdmin) {
        User newUser = new User(username);
        newUser.setAdmin(isAdmin);
        addUser(newUser);
        return newUser;
    }

    /**
     * Creates a new admin user
     */
    public User createAdminUser(String username, String password) {
        User newAdmin = new User(username, password, true);
        addUser(newAdmin);
        return newAdmin;
    }
}