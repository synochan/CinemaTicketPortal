import view.MainFrame;

import javax.swing.*;

/**
 * Main entry point for the CineBook CDO application
 */
public class Main {
    /**
     * Main method to start the application
     */
    public static void main(String[] args) {
        // Set look and feel to system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
        
        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create and show the main frame
                new MainFrame();
                
                // Print welcome message
                System.out.println("CineBook CDO Cinema Booking System");
                System.out.println("--------------------------------");
                System.out.println("Application started successfully!");
                System.out.println("Admin login: username = admin, password = admin123");
            }
        });
    }
}
