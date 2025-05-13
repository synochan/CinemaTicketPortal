package view.customer;

import controller.BookingController;
import model.Booking;
import model.Snack;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Panel for payment processing
 */
public class PaymentPanel extends JPanel {
    private MainFrame mainFrame;
    private BookingController bookingController;
    private Booking booking;
    private List<Snack> selectedSnacks;
    private JRadioButton gcashButton;
    private JRadioButton payMayaButton;
    private JRadioButton creditCardButton;
    private JPanel paymentDetailsPanel;
    private CardLayout cardLayout;

    public PaymentPanel(MainFrame mainFrame, Booking booking, List<Snack> selectedSnacks) {
        this.mainFrame = mainFrame;
        this.bookingController = mainFrame.getBookingController();
        this.booking = booking;
        this.selectedSnacks = selectedSnacks;
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Payment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("← Back to Snacks");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel("SnackSelectionPanel");
            }
        });
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Create main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        
        // Create summary panel
        JPanel summaryPanel = new JPanel();
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        
        // Add booking information
        JLabel movieLabel = new JLabel("Movie: " + booking.getMovie().getTitle());
        JLabel cinemaLabel = new JLabel("Cinema: " + booking.getCinema());
        JLabel showtimeLabel = new JLabel("Showtime: " + booking.getShowtime());
        
        StringBuilder seatsText = new StringBuilder("Seats: ");
        for (int i = 0; i < booking.getBookedSeats().size(); i++) {
            seatsText.append(booking.getBookedSeats().get(i).getSeatId());
            if (i < booking.getBookedSeats().size() - 1) {
                seatsText.append(", ");
            }
        }
        JLabel seatsLabel = new JLabel(seatsText.toString());
        
        // Calculate ticket total
        double ticketTotal = 0.0;
        for (int i = 0; i < booking.getBookedSeats().size(); i++) {
            ticketTotal += booking.getBookedSeats().get(i).getPrice();
        }
        
        JLabel ticketTotalLabel = new JLabel("Ticket Subtotal: ₱" + String.format("%.2f", ticketTotal));
        
        // Add snacks if selected
        JPanel snacksPanel = new JPanel();
        snacksPanel.setLayout(new BoxLayout(snacksPanel, BoxLayout.Y_AXIS));
        snacksPanel.setBorder(BorderFactory.createTitledBorder("Snacks & Drinks"));
        
        double snacksTotal = 0.0;
        if (!selectedSnacks.isEmpty()) {
            for (Snack snack : selectedSnacks) {
                JLabel snackLabel = new JLabel(snack.getName() + " - ₱" + String.format("%.2f", snack.getPrice()));
                snacksPanel.add(snackLabel);
                snacksTotal += snack.getPrice();
            }
        } else {
            snacksPanel.add(new JLabel("No snacks selected"));
        }
        
        JLabel snacksTotalLabel = new JLabel("Snacks Subtotal: ₱" + String.format("%.2f", snacksTotal));
        
        // Total
        double total = ticketTotal + snacksTotal;
        JLabel totalLabel = new JLabel("Total Amount: ₱" + String.format("%.2f", total));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Add everything to summary panel
        summaryPanel.add(movieLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(cinemaLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(showtimeLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(seatsLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        summaryPanel.add(ticketTotalLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(snacksPanel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        summaryPanel.add(snacksTotalLabel);
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(new JSeparator());
        summaryPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        summaryPanel.add(totalLabel);
        
        // Create payment method panel
        JPanel paymentMethodPanel = new JPanel();
        paymentMethodPanel.setLayout(new BoxLayout(paymentMethodPanel, BoxLayout.Y_AXIS));
        paymentMethodPanel.setBorder(BorderFactory.createTitledBorder("Select Payment Method"));
        
        // Create radio buttons for payment methods
        gcashButton = new JRadioButton("GCash");
        payMayaButton = new JRadioButton("PayMaya");
        creditCardButton = new JRadioButton("Credit Card");
        
        // Group the radio buttons
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(gcashButton);
        paymentGroup.add(payMayaButton);
        paymentGroup.add(creditCardButton);
        
        // Add radio buttons to panel
        JPanel radioPanel = new JPanel(new GridLayout(3, 1));
        radioPanel.add(gcashButton);
        radioPanel.add(payMayaButton);
        radioPanel.add(creditCardButton);
        
        paymentMethodPanel.add(radioPanel);
        
        // Create payment details panel with card layout
        cardLayout = new CardLayout();
        paymentDetailsPanel = new JPanel(cardLayout);
        
        // GCash panel
        JPanel gcashPanel = createGCashPanel();
        paymentDetailsPanel.add(gcashPanel, "GCash");
        
        // PayMaya panel
        JPanel payMayaPanel = createPayMayaPanel();
        paymentDetailsPanel.add(payMayaPanel, "PayMaya");
        
        // Credit Card panel
        JPanel creditCardPanel = createCreditCardPanel();
        paymentDetailsPanel.add(creditCardPanel, "CreditCard");
        
        // Empty panel (initial state)
        JPanel emptyPanel = new JPanel();
        emptyPanel.add(new JLabel("Select a payment method to continue"));
        paymentDetailsPanel.add(emptyPanel, "Empty");
        
        // Show empty panel initially
        cardLayout.show(paymentDetailsPanel, "Empty");
        
        // Add listeners to radio buttons
        gcashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(paymentDetailsPanel, "GCash");
            }
        });
        
        payMayaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(paymentDetailsPanel, "PayMaya");
            }
        });
        
        creditCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(paymentDetailsPanel, "CreditCard");
            }
        });
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(mainFrame, 
                        "Are you sure you want to cancel this booking?", 
                        "Cancel Booking", 
                        JOptionPane.YES_NO_OPTION);
                
                if (choice == JOptionPane.YES_OPTION) {
                    // Cancel booking and go back to movie list
                    JOptionPane.showMessageDialog(mainFrame, 
                            "Booking cancelled successfully.", 
                            "Booking Cancelled", 
                            JOptionPane.INFORMATION_MESSAGE);
                    
                    mainFrame.showPanel(MainFrame.MOVIE_LIST_PANEL);
                }
            }
        });
        
        JButton payButton = new JButton("Pay Now");
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gcashButton.isSelected() && !payMayaButton.isSelected() && !creditCardButton.isSelected()) {
                    JOptionPane.showMessageDialog(mainFrame, 
                            "Please select a payment method.", 
                            "No Payment Method", 
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Process payment (simulation)
                String paymentMethod = getSelectedPaymentMethod();
                processPayment(paymentMethod);
            }
        });
        
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(payButton);
        
        // Add everything to right panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(paymentMethodPanel, BorderLayout.NORTH);
        rightPanel.add(paymentDetailsPanel, BorderLayout.CENTER);
        
        // Add panels to content panel
        contentPanel.add(summaryPanel, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);
        
        // Add to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the GCash payment panel
     */
    private JPanel createGCashPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("GCash Details"));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        formPanel.add(new JLabel("GCash Number:"));
        JTextField numberField = new JTextField();
        formPanel.add(numberField);
        
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Note: This is a simulation. No actual payment will be processed."));
        
        return panel;
    }

    /**
     * Creates the PayMaya payment panel
     */
    private JPanel createPayMayaPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("PayMaya Details"));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        formPanel.add(new JLabel("PayMaya Account:"));
        JTextField accountField = new JTextField();
        formPanel.add(accountField);
        
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Note: This is a simulation. No actual payment will be processed."));
        
        return panel;
    }

    /**
     * Creates the Credit Card payment panel
     */
    private JPanel createCreditCardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Credit Card Details"));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        formPanel.add(new JLabel("Card Number:"));
        JTextField cardNumberField = new JTextField();
        formPanel.add(cardNumberField);
        
        formPanel.add(new JLabel("Cardholder Name:"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Expiry Date:"));
        JTextField expiryField = new JTextField("MM/YY");
        formPanel.add(expiryField);
        
        formPanel.add(new JLabel("CVV:"));
        JPasswordField cvvField = new JPasswordField();
        formPanel.add(cvvField);
        
        panel.add(formPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Note: This is a simulation. No actual payment will be processed."));
        
        return panel;
    }

    /**
     * Gets the selected payment method
     */
    private String getSelectedPaymentMethod() {
        if (gcashButton.isSelected()) {
            return "GCash";
        } else if (payMayaButton.isSelected()) {
            return "PayMaya";
        } else if (creditCardButton.isSelected()) {
            return "Credit Card";
        }
        return "";
    }

    /**
     * Processes the payment (simulated)
     */
    private void processPayment(String paymentMethod) {
        // Show payment processing dialog
        JDialog processingDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Processing Payment", true);
        processingDialog.setLayout(new BorderLayout());
        processingDialog.setSize(300, 150);
        processingDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        
        JPanel dialogPanel = new JPanel(new BorderLayout(20, 20));
        dialogPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel processingLabel = new JLabel("Processing your payment via " + paymentMethod + "...");
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        dialogPanel.add(processingLabel, BorderLayout.NORTH);
        dialogPanel.add(progressBar, BorderLayout.CENTER);
        
        processingDialog.add(dialogPanel);
        
        // Create timer to simulate processing time
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processingDialog.dispose();
                
                // Complete the booking
                booking.setPaymentMethod(paymentMethod);
                bookingController.completeBooking(booking, paymentMethod);
                
                // Show ticket panel
                TicketPanel ticketPanel = new TicketPanel(mainFrame, booking);
                mainFrame.addPanel(ticketPanel, "TicketPanel");
                mainFrame.showPanel("TicketPanel");
                
                // Show success message
                JOptionPane.showMessageDialog(mainFrame, 
                        "Payment successful! Your booking is confirmed.", 
                        "Payment Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        timer.setRepeats(false);
        timer.start();
        
        processingDialog.setVisible(true);
    }
}
