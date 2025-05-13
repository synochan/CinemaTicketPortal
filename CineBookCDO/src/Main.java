import view.MainFrame;

import javax.swing.*;

/**
 * Main entry point for CineBook CDO application
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting CineBook CDO Application...");
        
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set Look and Feel: " + e.getMessage());
        }
        
        // Start application on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame();
                    System.out.println("CineBook CDO application started successfully!");
                } catch (Exception e) {
                    System.err.println("Error starting application: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}