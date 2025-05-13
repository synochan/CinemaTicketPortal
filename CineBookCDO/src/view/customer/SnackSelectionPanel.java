package view.customer;

import controller.BookingController;
import model.Booking;
import model.Snack;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel for snack selection
 */
public class SnackSelectionPanel extends JPanel {
    private MainFrame mainFrame;
    private BookingController bookingController;
    private Booking booking;
    private JTable selectedSnacksTable;
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private double totalAmount;
    private List<Snack> selectedSnacks;

    public SnackSelectionPanel(MainFrame mainFrame, Booking booking) {
        this.mainFrame = mainFrame;
        this.bookingController = mainFrame.getBookingController();
        this.booking = booking;
        this.selectedSnacks = new ArrayList<>();
        this.totalAmount = booking.getTotalAmount(); // Start with ticket amount
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Add Snacks & Drinks");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton backButton = new JButton("← Back to Seats");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showPanel("SeatSelectionPanel");
            }
        });
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        
        // Create snacks catalog panel
        JPanel snacksCatalogPanel = new JPanel();
        snacksCatalogPanel.setLayout(new BoxLayout(snacksCatalogPanel, BoxLayout.Y_AXIS));
        
        // Create panel for each snack category
        String[] categories = {"Popcorn", "Drink", "Combo"};
        for (String category : categories) {
            JPanel categoryPanel = createCategoryPanel(category);
            snacksCatalogPanel.add(categoryPanel);
            snacksCatalogPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        // Create selected snacks panel
        JPanel selectedSnacksPanel = new JPanel(new BorderLayout());
        selectedSnacksPanel.setBorder(BorderFactory.createTitledBorder("Your Selected Items"));
        
        // Create table for selected snacks
        String[] columnNames = {"Item", "Price", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only allow editing the action column
            }
        };
        
        selectedSnacksTable = new JTable(tableModel);
        selectedSnacksTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        selectedSnacksTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        selectedSnacksTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        
        // Add "Remove" button to the action column
        selectedSnacksTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        selectedSnacksTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()) {
            @Override
            public Object getCellEditorValue() {
                int selectedRow = selectedSnacksTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < selectedSnacks.size()) {
                    Snack snackToRemove = selectedSnacks.get(selectedRow);
                    selectedSnacks.remove(selectedRow);
                    tableModel.removeRow(selectedRow);
                    totalAmount -= snackToRemove.getPrice();
                    updateTotalLabel();
                }
                return super.getCellEditorValue();
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(selectedSnacksTable);
        selectedSnacksPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Create summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        
        // Movie and seats info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
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
        
        // Total label
        totalLabel = new JLabel("Total: ₱" + String.format("%.2f", totalAmount));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Add to info panel
        infoPanel.add(movieLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(cinemaLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(showtimeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(seatsLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(totalLabel);
        
        summaryPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton skipButton = new JButton("Skip Snacks");
        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Continue to payment without snacks
                PaymentPanel paymentPanel = new PaymentPanel(mainFrame, booking, new ArrayList<>());
                mainFrame.addPanel(paymentPanel, "PaymentPanel");
                mainFrame.showPanel("PaymentPanel");
            }
        });
        
        JButton continueButton = new JButton("Continue to Payment");
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add selected snacks to booking and continue to payment
                bookingController.addSnacksToBooking(booking, selectedSnacks);
                
                PaymentPanel paymentPanel = new PaymentPanel(mainFrame, booking, selectedSnacks);
                mainFrame.addPanel(paymentPanel, "PaymentPanel");
                mainFrame.showPanel("PaymentPanel");
            }
        });
        
        buttonsPanel.add(skipButton);
        buttonsPanel.add(continueButton);
        
        // Add panels to content panel
        JScrollPane catalogScrollPane = new JScrollPane(snacksCatalogPanel);
        catalogScrollPane.setBorder(BorderFactory.createTitledBorder("Available Snacks & Drinks"));
        
        contentPanel.add(catalogScrollPane, BorderLayout.CENTER);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(selectedSnacksPanel, BorderLayout.CENTER);
        rightPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        // Add panels to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a panel for a specific snack category
     */
    private JPanel createCategoryPanel(String category) {
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.setBorder(BorderFactory.createTitledBorder(category));
        
        JPanel snacksPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        
        List<Snack> categorySnacks = bookingController.getSnacksByCategory(category);
        for (Snack snack : categorySnacks) {
            JPanel snackPanel = new JPanel(new BorderLayout());
            
            JLabel nameLabel = new JLabel(snack.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel priceLabel = new JLabel("₱" + String.format("%.2f", snack.getPrice()));
            
            JButton addButton = new JButton("Add");
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addSnack(snack);
                }
            });
            
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.add(nameLabel, BorderLayout.NORTH);
            
            JLabel descLabel = new JLabel(snack.getDescription());
            descLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            infoPanel.add(descLabel, BorderLayout.CENTER);
            infoPanel.add(priceLabel, BorderLayout.SOUTH);
            
            snackPanel.add(infoPanel, BorderLayout.CENTER);
            snackPanel.add(addButton, BorderLayout.EAST);
            
            snacksPanel.add(snackPanel);
        }
        
        categoryPanel.add(snacksPanel, BorderLayout.CENTER);
        
        return categoryPanel;
    }

    /**
     * Adds a snack to the selected items
     */
    private void addSnack(Snack snack) {
        selectedSnacks.add(snack);
        
        // Add to table
        Object[] rowData = {
            snack.getName(),
            "₱" + String.format("%.2f", snack.getPrice()),
            "Remove"
        };
        tableModel.addRow(rowData);
        
        // Update total
        totalAmount += snack.getPrice();
        updateTotalLabel();
    }

    /**
     * Updates the total label
     */
    private void updateTotalLabel() {
        totalLabel.setText("Total: ₱" + String.format("%.2f", totalAmount));
    }

    /**
     * Renderer for the button in the table
     */
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
     * Editor for the button in the table
     */
    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
