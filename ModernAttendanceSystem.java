import java.awt.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.*;

/**
 * ModernAttendanceSystem - A sleek attendance tracking application
 * Features modern UI design with gradient backgrounds and real-time updates
 * Author: Your Name
 * Date: January 2026
 */
public class ModernAttendanceSystem extends JFrame {
    
    // UI Components with custom styling
    private JTextField nameField;
    private JTextField courseYearField;
    private JTextField timeInField;
    private JTextArea eSignatureArea;
    private JLabel statusLabel;
    private JLabel clockLabel;
    private Timer clockTimer;
    private int attendanceCount = 0;
    
    // Custom color scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color ACCENT_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    
    /**
     * Constructor - Initializes the modern attendance system
     */
    public ModernAttendanceSystem() {
        setupFrame();
        createModernUI();
        startLiveClock();
        setVisible(true);
    }
    
    /**
     * Configure the main application frame
     */
    private void setupFrame() {
        setTitle("Modern Attendance System v2.0");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Create the modern user interface with custom components
     */
    private void createModernUI() {
        // Main container with gradient background
        JPanel mainContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, LIGHT_BG, 0, getHeight(), Color.WHITE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainContainer.setLayout(new BorderLayout(15, 15));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        // Header section with live clock
        mainContainer.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Form section with styled inputs
        mainContainer.add(createFormPanel(), BorderLayout.CENTER);
        
        // Action buttons section
        mainContainer.add(createActionPanel(), BorderLayout.SOUTH);
        
        add(mainContainer);
    }
    
    /**
     * Create the header panel with title and live clock
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        // Title with icon
        JLabel titleLabel = new JLabel("  ATTENDANCE TRACKING SYSTEM", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Live clock display
        clockLabel = new JLabel("", JLabel.RIGHT);
        clockLabel.setFont(new Font("Consolas", Font.PLAIN, 14));
        clockLabel.setForeground(TEXT_COLOR);
        
        // Status indicator
        statusLabel = new JLabel("● Ready", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(ACCENT_COLOR);
        
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(titleLabel, BorderLayout.WEST);
        topRow.add(clockLabel, BorderLayout.EAST);
        
        headerPanel.add(topRow, BorderLayout.NORTH);
        headerPanel.add(statusLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    /**
     * Create the main form panel with styled input fields
     */
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Add all input fields with custom styling
        formPanel.add(createStyledFieldPanel("Full Name", nameField = createStyledTextField()));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createStyledFieldPanel("Course & Year", courseYearField = createStyledTextField()));
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(createStyledFieldPanel("Time Stamp", timeInField = createStyledTextField()));
        timeInField.setEditable(false);
        timeInField.setBackground(new Color(245, 245, 245));
        formPanel.add(Box.createVerticalStrut(15));
        
        // E-Signature with text area for better display
        JLabel sigLabel = new JLabel("Digital Signature (SHA-256 Hash)");
        sigLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sigLabel.setForeground(TEXT_COLOR);
        sigLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(sigLabel);
        formPanel.add(Box.createVerticalStrut(5));
        
        eSignatureArea = new JTextArea(3, 20);
        eSignatureArea.setEditable(false);
        eSignatureArea.setLineWrap(true);
        eSignatureArea.setWrapStyleWord(true);
        eSignatureArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        eSignatureArea.setBackground(new Color(245, 245, 245));
        eSignatureArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        JScrollPane scrollPane = new JScrollPane(eSignatureArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(scrollPane);
        
        return formPanel;
    }
    
    /**
     * Create a styled input field panel with label
     */
    private JPanel createStyledFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    /**
     * Create a styled text field with modern appearance
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }
    
    /**
     * Create the action button panel
     */
    private JPanel createActionPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        
        // Record Attendance button
        JButton recordBtn = createStyledButton("Record Attendance", ACCENT_COLOR);
        recordBtn.addActionListener(e -> recordAttendance());
        
        // Clear Form button
        JButton clearBtn = createStyledButton("Clear Form", DANGER_COLOR);
        clearBtn.addActionListener(e -> clearForm());
        
        // View Records button
        JButton viewBtn = createStyledButton("View Summary", SECONDARY_COLOR);
        viewBtn.addActionListener(e -> viewSummary());
        
        buttonPanel.add(recordBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(viewBtn);
        
        return buttonPanel;
    }
    
    /**
     * Create a styled button with custom color
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(160, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    /**
     * Start the live clock timer
     */
    private void startLiveClock() {
        clockTimer = new Timer(1000, e -> updateClock());
        clockTimer.start();
        updateClock();
    }
    
    /**
     * Update the clock display
     */
    private void updateClock() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM dd, yyyy | hh:mm:ss a");
        clockLabel.setText(now.format(formatter));
    }
    
    /**
     * Record attendance with validation and unique signature generation
     */
    private void recordAttendance() {
        // Validate inputs
        if (nameField.getText().trim().isEmpty()) {
            showNotification("Please enter your full name", DANGER_COLOR);
            nameField.requestFocus();
            return;
        }
        
        if (courseYearField.getText().trim().isEmpty()) {
            showNotification("Please enter your course and year", DANGER_COLOR);
            courseYearField.requestFocus();
            return;
        }
        
        // Generate timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String timeStamp = now.format(formatter);
        timeInField.setText(timeStamp);
        
        // Generate unique cryptographic signature using SHA-256
        String signatureData = nameField.getText() + courseYearField.getText() + timeStamp + System.nanoTime();
        String signature = generateSHA256Signature(signatureData);
        eSignatureArea.setText(signature);
        
        attendanceCount++;
        showNotification("✓ Attendance recorded successfully!", ACCENT_COLOR);
        
        // Show detailed confirmation
        String confirmMessage = String.format(
            "Attendance Record #%d\n\n" +
            "Name: %s\n" +
            "Course/Year: %s\n" +
            "Time In: %s\n\n" +
            "Digital Signature:\n%s",
            attendanceCount,
            nameField.getText(),
            courseYearField.getText(),
            timeStamp,
            signature.substring(0, Math.min(40, signature.length())) + "..."
        );
        
        JOptionPane.showMessageDialog(this, confirmMessage, 
            "Attendance Confirmed", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Generate a SHA-256 cryptographic hash as digital signature
     */
    private String generateSHA256Signature(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes());
            
            // Convert bytes to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            // Fallback to timestamp-based signature
            return "SIG-" + System.currentTimeMillis() + "-" + Math.abs(data.hashCode());
        }
    }
    
    /**
     * Clear all form fields
     */
    private void clearForm() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear the form?", 
            "Confirm Clear", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            nameField.setText("");
            courseYearField.setText("");
            timeInField.setText("");
            eSignatureArea.setText("");
            showNotification("Form cleared", SECONDARY_COLOR);
            nameField.requestFocus();
        }
    }
    
    /**
     * View attendance summary
     */
    private void viewSummary() {
        String summary = String.format(
            "ATTENDANCE SUMMARY\n\n" +
            "Total Records Today: %d\n" +
            "System Status: Active\n" +
            "Current Time: %s\n\n" +
            "Latest Entry:\n" +
            "Name: %s\n" +
            "Course: %s\n" +
            "Time: %s",
            attendanceCount,
            clockLabel.getText(),
            nameField.getText().isEmpty() ? "N/A" : nameField.getText(),
            courseYearField.getText().isEmpty() ? "N/A" : courseYearField.getText(),
            timeInField.getText().isEmpty() ? "N/A" : timeInField.getText()
        );
        
        JOptionPane.showMessageDialog(this, summary, 
            "System Summary", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show status notification
     */
    private void showNotification(String message, Color color) {
        statusLabel.setText("● " + message);
        statusLabel.setForeground(color);
        
        // Reset to ready after 3 seconds
        Timer resetTimer = new Timer(3000, e -> {
            statusLabel.setText("● Ready");
            statusLabel.setForeground(ACCENT_COLOR);
        });
        resetTimer.setRepeats(false);
        resetTimer.start();
    }
    
    /**
     * Main method to launch the application
     */
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Launch application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new ModernAttendanceSystem();
        });
    }
}