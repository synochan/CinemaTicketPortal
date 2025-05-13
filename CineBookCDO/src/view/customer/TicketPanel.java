package view.customer;

import model.Booking;
import model.Seat;
import model.Snack;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Panel to display the digital ticket after a successful booking
 */
public class TicketPanel extends JPanel {
    private MainFrame mainFrame;
    private Booking booking;
    private JTextArea ticketTextArea;

    public TicketPanel(MainFrame mainFrame, Booking booking) {
        this.mainFrame = mainFrame;
        this.booking = booking;
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Your Digital Ticket");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton homeButton = new JButton("Return to Home");
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel(MainFrame.MOVIE_LIST_PANEL);
            }
        });
        headerPanel.add(homeButton, BorderLayout.EAST);
        
        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        
        // Create ticket panel
        JPanel ticketPanel = new JPanel(new BorderLayout());
        ticketPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        
        // Create simulated QR code panel
        JPanel qrCodePanel = createSimulatedQRCode();
        
        // Create ticket information
        JPanel ticketInfoPanel = new JPanel();
        ticketInfoPanel.setLayout(new BoxLayout(ticketInfoPanel, BoxLayout.Y_AXIS));
        
        // Get ticket text
        String ticketText = booking.getBookingSummary();
        
        // Create text area for ticket
        ticketTextArea = new JTextArea(ticketText);
        ticketTextArea.setEditable(false);
        ticketTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        ticketTextArea.setBackground(new Color(255, 255, 240)); // Light yellow
        
        // Add components to ticket panel
        ticketPanel.add(new JScrollPane(ticketTextArea), BorderLayout.CENTER);
        ticketPanel.add(qrCodePanel, BorderLayout.EAST);
        
        // Create action buttons panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton saveButton = new JButton("Save Ticket");
        saveButton.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTicketToFile();
            }
        });
        
        JButton printButton = new JButton("Print Ticket");
        printButton.setIcon(UIManager.getIcon("FileView.printIcon"));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Print simulation: Ticket sent to printer.",
                        "Print Simulation",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JButton emailButton = new JButton("Email Ticket");
        emailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Email simulation: Ticket sent to " + booking.getUsername() + "@example.com",
                        "Email Simulation",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        actionsPanel.add(saveButton);
        actionsPanel.add(printButton);
        actionsPanel.add(emailButton);
        
        // Add to content panel
        contentPanel.add(ticketPanel, BorderLayout.CENTER);
        contentPanel.add(actionsPanel, BorderLayout.SOUTH);
        
        // Add panels to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Display completion message
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showCompletionDialog();
            }
        });
    }
    
    /**
     * Creates a simulated QR code panel
     */
    private JPanel createSimulatedQRCode() {
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setPreferredSize(new Dimension(150, 150));
        qrPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        
        // Create simulated QR code
        JPanel qrCode = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Draw QR code border
                g2d.setColor(Color.BLACK);
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                
                // Draw simulated QR code pattern
                g2d.setColor(Color.BLACK);
                int cellSize = 10;
                int margin = 10;
                
                // Draw finder patterns (the three large squares in corners)
                // Top-left finder pattern
                g2d.fillRect(margin, margin, 7 * cellSize, 7 * cellSize);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(margin + cellSize, margin + cellSize, 5 * cellSize, 5 * cellSize);
                g2d.setColor(Color.BLACK);
                g2d.fillRect(margin + 2 * cellSize, margin + 2 * cellSize, 3 * cellSize, 3 * cellSize);
                
                // Top-right finder pattern
                g2d.fillRect(getWidth() - margin - 7 * cellSize, margin, 7 * cellSize, 7 * cellSize);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(getWidth() - margin - 6 * cellSize, margin + cellSize, 5 * cellSize, 5 * cellSize);
                g2d.setColor(Color.BLACK);
                g2d.fillRect(getWidth() - margin - 5 * cellSize, margin + 2 * cellSize, 3 * cellSize, 3 * cellSize);
                
                // Bottom-left finder pattern
                g2d.fillRect(margin, getHeight() - margin - 7 * cellSize, 7 * cellSize, 7 * cellSize);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(margin + cellSize, getHeight() - margin - 6 * cellSize, 5 * cellSize, 5 * cellSize);
                g2d.setColor(Color.BLACK);
                g2d.fillRect(margin + 2 * cellSize, getHeight() - margin - 5 * cellSize, 3 * cellSize, 3 * cellSize);
                
                // Draw some random modules to simulate QR code data
                g2d.setColor(Color.BLACK);
                for (int i = 0; i < 30; i++) {
                    int x = margin + (int) (Math.random() * (getWidth() - 2 * margin)) / cellSize * cellSize;
                    int y = margin + (int) (Math.random() * (getHeight() - 2 * margin)) / cellSize * cellSize;
                    g2d.fillRect(x, y, cellSize, cellSize);
                }
                
                g2d.dispose();
            }
        };
        qrCode.setBackground(Color.WHITE);
        
        // Add booking code label
        JLabel codeLabel = new JLabel(booking.getBookingCode(), JLabel.CENTER);
        codeLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        
        qrPanel.add(qrCode, BorderLayout.CENTER);
        qrPanel.add(codeLabel, BorderLayout.SOUTH);
        
        return qrPanel;
    }
    
    /**
     * Shows a completion dialog after booking
     */
    private void showCompletionDialog() {
        JOptionPane.showMessageDialog(mainFrame,
                "Booking completed successfully!\n" +
                "Your booking code is: " + booking.getBookingCode() + "\n\n" +
                "Please keep this code for reference when you visit the cinema.",
                "Booking Completed",
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Saves the ticket to a text file
     */
    private void saveTicketToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Ticket");
        
        // Set default file name
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String defaultFileName = "CDO_Ticket_" + booking.getBookingCode() + ".txt";
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(ticketTextArea.getText());
                JOptionPane.showMessageDialog(mainFrame,
                        "Ticket saved successfully to:\n" + fileToSave.getAbsolutePath(),
                        "Ticket Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Error saving ticket: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
