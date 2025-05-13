package controller;

import model.User;

/**
 * Controller class for user-related operations
 */
public class UserController {
    private AdminController adminController;

    public UserController(AdminController adminController) {
        this.adminController = adminController;
    }

    /**
     * Registers a new user
     */
    public User registerUser(String username, String password, String email, String fullName) {
        return adminController.registerUser(username, password, email, fullName);
    }

    /**
     * Authenticates a user
     */
    public boolean login(String username, String password) {
        return adminController.validateUserLogin(username, password);
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        adminController.logoutCurrentUser();
    }

    /**
     * Gets the currently logged in user
     */
    public User getCurrentUser() {
        return adminController.getCurrentUser();
    }

    /**
     * Checks if a user is currently logged in
     */
    public boolean isUserLoggedIn() {
        return adminController.isUserLoggedIn();
    }

    /**
     * Updates user profile information
     */
    public boolean updateProfile(String username, String email, String fullName, String phoneNumber) {
        return adminController.updateUserProfile(username, email, fullName, phoneNumber);
    }

    /**
     * Changes user password
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return adminController.changePassword(username, oldPassword, newPassword);
    }

    /**
     * Checks if a given username is available (not already registered)
     */
    public boolean isUsernameAvailable(String username) {
        return adminController.isUsernameAvailable(username);
    }
}