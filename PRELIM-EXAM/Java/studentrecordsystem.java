/*
 * Student Record System - Java Swing Implementation
 * Programmer: Nagpaton, Francesca Louise May G
 * Student ID: [Your Student ID]
 * 
 * This work is created by Nagpaton, Francesca Louise May G
 * All rights reserved.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class StudentRecordSystem extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtStudentID, txtFirstName, txtLastName;
    private JButton btnAdd, btnDelete;
    
    // Column names matching the CSV structure
    private String[] columnNames = {
        "StudentID", "First Name", "Last Name", "LAB WORK 1", 
        "LAB WORK 2", "LAB WORK 3", "PRELIM EXAM", "ATTENDANCE GRADE"
    };
    
    public StudentRecordSystem() {
        // Set window title with programmer identifier
        setTitle("Student Records - Nagpaton, Francesca Louise May G");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialize components
        initializeComponents();
        
        // Load CSV data on startup
        loadCSVData();
    }
    
    private void initializeComponents() {
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create watermark label at the top
        JLabel watermarkLabel = new JLabel("Created by: Nagpaton, Francesca Louise May G", SwingConstants.CENTER);
        watermarkLabel.setFont(new Font("Arial", Font.BOLD, 16));
        watermarkLabel.setForeground(new Color(0, 102, 204));
        watermarkLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        mainPanel.add(watermarkLabel, BorderLayout.NORTH);
        
        // Table setup
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Input panel for CRUD operations
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Student ID field
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        txtStudentID = new JTextField(15);
        inputPanel.add(txtStudentID, gbc);
        
        // First Name field
        gbc.gridx = 2; gbc.gridy = 0;
        inputPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3;
        txtFirstName = new JTextField(15);
        inputPanel.add(txtFirstName, gbc);
        
        // Last Name field
        gbc.gridx = 4; gbc.gridy = 0;
        inputPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 5;
        txtLastName = new JTextField(15);
        inputPanel.add(txtLastName, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnAdd = new JButton("Add Record");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> addRecord());
        
        btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteRecord());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);
        
        // Add input panel and button panel to a container
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Footer watermark
        JLabel footerLabel = new JLabel("© 2024 - This work belongs to Nagpaton, Francesca Louise May G", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(footerLabel, BorderLayout.SOUTH);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadCSVData() {
        String csvFile = "MOCK_DATA.csv";
        String line;
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header line
            br.readLine();
            
            // Read and parse each line
            while ((line = br.readLine()) != null) {
                // Split by comma
                String[] values = line.split(",");
                
                // Ensure we have all columns
                if (values.length >= 8) {
                    tableModel.addRow(values);
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                "Successfully loaded " + tableModel.getRowCount() + " student records!", 
                "Data Loaded", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, 
                "Error: MOCK_DATA.csv file not found!\nPlease ensure the file is in the same directory as the program.", 
                "File Not Found", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error reading the CSV file: " + e.getMessage(), 
                "Read Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void addRecord() {
        String studentID = txtStudentID.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        
        // Validation
        if (studentID.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please fill in Student ID, First Name, and Last Name!", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create new row with default grades (can be modified later)
        Object[] newRow = {
            studentID,
            firstName,
            lastName,
            "0",  // LAB WORK 1
            "0",  // LAB WORK 2
            "0",  // LAB WORK 3
            "0",  // PRELIM EXAM
            "0"   // ATTENDANCE GRADE
        };
        
        // Add to table
        tableModel.addRow(newRow);
        
        // Clear input fields
        txtStudentID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        
        JOptionPane.showMessageDialog(this, 
            "Student record added successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a row to delete!", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this record?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, 
                "Record deleted successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            StudentRecordSystem frame = new StudentRecordSystem();
            frame.setVisible(true);
        });
    }
}
