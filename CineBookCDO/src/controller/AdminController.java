package controller;

import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for admin-related operations
 */
public class AdminController {
    private List<User> users;
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";

    public AdminController() {
        this.users = new ArrayList<>();
        initializeUsers();
    }

    /**
     * Initializes users
     */
    private void initializeUsers() {
        // Add admin user
        users.add(new User(ADMIN_USERNAME, ADMIN_PASSWORD, true));
        
        // Add some sample regular users
        users.add(new User("john", null, false));
        users.add(new User("maria", null, false));
        users.add(new User("alex", null, false));
    }

    /**
     * Checks admin login credentials
     */
    public boolean validateAdminLogin(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return true;
        }
        
        for (User user : users) {
            if (user.isAdmin() && username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        
        return false;
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
