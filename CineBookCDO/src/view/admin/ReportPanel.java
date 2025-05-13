package view.admin;

import controller.ReportController;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

/**
 * Panel for generating and viewing reports
 */
public class ReportPanel extends JPanel {
    private MainFrame mainFrame;
    private ReportController reportController;
    private JTextArea reportTextArea;
    private JComboBox<String> reportTypeComboBox;
    private String currentReportType;

    public ReportPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.reportController = mainFrame.getReportController();
        this.currentReportType = "Tickets Per Movie";
        
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create panel title
        JLabel titleLabel = new JLabel("Reports");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Create controls panel
        JPanel controlsPanel = createControlsPanel();
        
        // Create report display panel
        JPanel reportPanel = createReportPanel();
        
        // Add components to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(controlsPanel, BorderLayout.WEST);
        add(reportPanel, BorderLayout.CENTER);
        
        // Generate initial report
        generateReport();
    }
    
    /**
     * Creates the panel containing the report controls
     */
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(0, 0, 0, 20));
        panel.setPreferredSize(new Dimension(250, 0));
        
        // Create report type selection panel
        JPanel reportTypePanel = new JPanel(new BorderLayout());
        reportTypePanel.setBorder(BorderFactory.createTitledBorder("Report Type"));
        
        String[] reportTypes = {
            "Tickets Per Movie",
            "Tickets Per Cinema",
            "Revenue Per Movie",
            "Revenue Per Cinema",
            "Seat Occupancy"
        };
        
        reportTypeComboBox = new JComboBox<>(reportTypes);
        reportTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentReportType = (String) reportTypeComboBox.getSelectedItem();
                generateReport();
            }
        });
        
        reportTypePanel.add(reportTypeComboBox, BorderLayout.CENTER);
        
        // Create action buttons panel
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        
        JButton generateButton = new JButton("Generate Report");
        generateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        generateButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, generateButton.getPreferredSize().height));
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        
        JButton exportButton = new JButton("Export to File");
        exportButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        exportButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, exportButton.getPreferredSize().height));
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportReport();
            }
        });
        
        JButton printButton = new JButton("Print Report");
        printButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        printButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, printButton.getPreferredSize().height));
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Print simulation: Report sent to printer.",
                        "Print Simulation",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        actionsPanel.add(generateButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(exportButton);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        actionsPanel.add(printButton);
        
        // Create help panel
        JPanel helpPanel = new JPanel(new BorderLayout());
        helpPanel.setBorder(BorderFactory.createTitledBorder("Help"));
        
        JTextArea helpText = new JTextArea(
                "Reports provide insights into ticket sales, revenue, and seat occupancy.\n\n" +
                "- Select a report type from the dropdown\n" +
                "- Click 'Generate Report' to refresh data\n" +
                "- Use 'Export to File' to save as text\n" +
                "- 'Print Report' simulates printing"
        );
        helpText.setEditable(false);
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);
        helpText.setBackground(new Color(240, 240, 240));
        
        helpPanel.add(helpText, BorderLayout.CENTER);
        
        // Add panels to controls panel
        panel.add(reportTypePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(actionsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(helpPanel);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    /**
     * Creates the panel containing the report display
     */
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(),
                new EmptyBorder(10, 10, 10, 10)));
        
        // Create text area for report
        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        // Add to panel
        panel.add(new JScrollPane(reportTextArea), BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Generates and displays the selected report
     */
    private void generateReport() {
        String reportText = reportController.getReportAsString(currentReportType);
        reportTextArea.setText(reportText);
        reportTextArea.setCaretPosition(0); // Scroll to top
    }
    
    /**
     * Exports the current report to a text file
     */
    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Report");
        
        // Set default file name
        String defaultFileName = "CineBook_" + currentReportType.replace(" ", "_") + "_Report.txt";
        fileChooser.setSelectedFile(new File(defaultFileName));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            boolean success = reportController.exportReportToFile(currentReportType, fileToSave.getAbsolutePath());
            
            if (success) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Report exported successfully to:\n" + fileToSave.getAbsolutePath(),
                        "Report Exported",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Error exporting report. Please try again.",
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Creates a visual chart for the current report
     */
    private JPanel createChartPanel() {
        // This is a simple visual representation of data - not a full chart library
        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                // Get data based on report type
                Map<String, ?> data = null;
                
                switch (currentReportType) {
                    case "Tickets Per Movie":
                        data = reportController.generateTicketsPerMovieReport();
                        break;
                    case "Tickets Per Cinema":
                        data = reportController.generateTicketsPerCinemaReport();
                        break;
                    case "Revenue Per Movie":
                        data = reportController.generateRevenuePerMovieReport();
                        break;
                    case "Revenue Per Cinema":
                        data = reportController.generateRevenuePerCinemaReport();
                        break;
                    case "Seat Occupancy":
                        data = reportController.generateSeatOccupancyReport();
                        break;
                }
                
                if (data != null && !data.isEmpty()) {
                    // Draw chart here based on data
                    // This would be a simple bar chart
                    
                    // Chart dimensions
                    int margin = 50;
                    int barWidth = (getWidth() - 2 * margin) / data.size();
                    int maxHeight = getHeight() - 2 * margin;
                    
                    // Find max value for scaling
                    double maxValue = 0;
                    for (Object value : data.values()) {
                        if (value instanceof Number) {
                            maxValue = Math.max(maxValue, ((Number) value).doubleValue());
                        }
                    }
                    
                    // Draw bars
                    int i = 0;
                    for (Map.Entry<String, ?> entry : data.entrySet()) {
                        String key = entry.getKey();
                        double value = 0;
                        
                        if (entry.getValue() instanceof Number) {
                            value = ((Number) entry.getValue()).doubleValue();
                        }
                        
                        // Calculate bar height (scaled)
                        int barHeight = (int) (value / maxValue * maxHeight);
                        
                        // Draw bar
                        g2d.setColor(new Color(51, 153, 255));
                        g2d.fillRect(margin + i * barWidth, getHeight() - margin - barHeight, 
                                barWidth - 10, barHeight);
                        
                        // Draw label
                        g2d.setColor(Color.BLACK);
                        g2d.rotate(-Math.PI/2, margin + i * barWidth + barWidth/2, getHeight() - margin + 25);
                        g2d.drawString(key, margin + i * barWidth + barWidth/2, getHeight() - margin + 25);
                        g2d.rotate(Math.PI/2, margin + i * barWidth + barWidth/2, getHeight() - margin + 25);
                        
                        // Draw value
                        g2d.drawString(String.format("%.1f", value), 
                                margin + i * barWidth + barWidth/2 - 10, 
                                getHeight() - margin - barHeight - 5);
                        
                        i++;
                    }
                }
                
                g2d.dispose();
            }
        };
        
        chartPanel.setPreferredSize(new Dimension(0, 300));
        
        return chartPanel;
    }
}
