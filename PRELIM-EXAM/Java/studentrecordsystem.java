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

public class StudentRecordSystem_FIXED extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtStudentID, txtFirstName, txtLastName;
    private JTextField txtLabWork1, txtLabWork2, txtLabWork3, txtPrelimExam, txtAttendance;
    private JTextField txtSearch;
    private JButton btnAdd, btnDelete, btnEditGrades, btnSearch;
    
    // Column names matching the CSV structure
    private String[] columnNames = {
        "StudentID", "First Name", "Last Name", "LAB WORK 1", 
        "LAB WORK 2", "LAB WORK 3", "PRELIM EXAM", "ATTENDANCE GRADE"
    };
    
    public StudentRecordSystem_FIXED() {
        // Set window title with programmer identifier
        setTitle("Student Records - Nagpaton, Francesca Louise May G");
        setSize(1200, 700);
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
        
        // Add listener to populate fields when row is selected
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                populateFieldsFromSelectedRow();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Student"));
        searchPanel.add(new JLabel("Student ID:"));
        txtSearch = new JTextField(20);
        searchPanel.add(txtSearch);
        
        btnSearch = new JButton("🔍 Search");
        btnSearch.setBackground(new Color(155, 89, 182));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.addActionListener(e -> searchStudent());
        searchPanel.add(btnSearch);
        
        JButton btnShowAll = new JButton("Show All");
        btnShowAll.setBackground(new Color(149, 165, 166));
        btnShowAll.setForeground(Color.WHITE);
        btnShowAll.setFocusPainted(false);
        btnShowAll.addActionListener(e -> showAllStudents());
        searchPanel.add(btnShowAll);
        
        // Panel to hold search and table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Input panel for CRUD operations
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Row 1: Student Info
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        txtStudentID = new JTextField(12);
        inputPanel.add(txtStudentID, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 3;
        txtFirstName = new JTextField(12);
        inputPanel.add(txtFirstName, gbc);
        
        gbc.gridx = 4;
        inputPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 5;
        txtLastName = new JTextField(12);
        inputPanel.add(txtLastName, gbc);
        
        // Row 2: Grades
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Lab Work 1:"), gbc);
        gbc.gridx = 1;
        txtLabWork1 = new JTextField(8);
        inputPanel.add(txtLabWork1, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Lab Work 2:"), gbc);
        gbc.gridx = 3;
        txtLabWork2 = new JTextField(8);
        inputPanel.add(txtLabWork2, gbc);
        
        gbc.gridx = 4;
        inputPanel.add(new JLabel("Lab Work 3:"), gbc);
        gbc.gridx = 5;
        txtLabWork3 = new JTextField(8);
        inputPanel.add(txtLabWork3, gbc);
        
        // Row 3: More Grades
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Prelim Exam:"), gbc);
        gbc.gridx = 1;
        txtPrelimExam = new JTextField(8);
        inputPanel.add(txtPrelimExam, gbc);
        
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Attendance:"), gbc);
        gbc.gridx = 3;
        txtAttendance = new JTextField(8);
        inputPanel.add(txtAttendance, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        btnAdd = new JButton("Add Record");
        btnAdd.setBackground(new Color(46, 204, 113));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(e -> addRecord());
        
        btnEditGrades = new JButton("Edit Grades");
        btnEditGrades.setBackground(new Color(52, 152, 219));
        btnEditGrades.setForeground(Color.WHITE);
        btnEditGrades.setFocusPainted(false);
        btnEditGrades.addActionListener(e -> editGrades());
        
        btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteRecord());
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEditGrades);
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
    
    private void populateFieldsFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            txtStudentID.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtFirstName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtLastName.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtLabWork1.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtLabWork2.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtLabWork3.setText(tableModel.getValueAt(selectedRow, 5).toString());
            txtPrelimExam.setText(tableModel.getValueAt(selectedRow, 6).toString());
            txtAttendance.setText(tableModel.getValueAt(selectedRow, 7).toString());
        }
    }
    
    private void loadCSVData() {
        String csvFile = "MOCK_DATA.csv";
        String line;
        int recordsLoaded = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Skip header line
            br.readLine();
            
            // Read and parse each line
            while ((line = br.readLine()) != null) {
                // Split by comma
                String[] values = line.split(",");
                
                // Ensure we have all columns (at least 8)
                if (values.length >= 8) {
                    tableModel.addRow(values);
                    recordsLoaded++;
                }
            }
            
            JOptionPane.showMessageDialog(this, 
                "Successfully loaded " + recordsLoaded + " student records!", 
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
        
        // Get grade values or use default "0"
        String labWork1 = txtLabWork1.getText().trim().isEmpty() ? "0" : txtLabWork1.getText().trim();
        String labWork2 = txtLabWork2.getText().trim().isEmpty() ? "0" : txtLabWork2.getText().trim();
        String labWork3 = txtLabWork3.getText().trim().isEmpty() ? "0" : txtLabWork3.getText().trim();
        String prelimExam = txtPrelimExam.getText().trim().isEmpty() ? "0" : txtPrelimExam.getText().trim();
        String attendance = txtAttendance.getText().trim().isEmpty() ? "0" : txtAttendance.getText().trim();
        
        // Create new row
        Object[] newRow = {
            studentID,
            firstName,
            lastName,
            labWork1,
            labWork2,
            labWork3,
            prelimExam,
            attendance
        };
        
        // Add to table
        tableModel.addRow(newRow);
        
        // Clear input fields
        clearFields();
        
        JOptionPane.showMessageDialog(this, 
            "Student record added successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void editGrades() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a row to edit grades!", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate grade inputs
        try {
            String labWork1 = txtLabWork1.getText().trim();
            String labWork2 = txtLabWork2.getText().trim();
            String labWork3 = txtLabWork3.getText().trim();
            String prelimExam = txtPrelimExam.getText().trim();
            String attendance = txtAttendance.getText().trim();
            
            if (labWork1.isEmpty() || labWork2.isEmpty() || labWork3.isEmpty() || 
                prelimExam.isEmpty() || attendance.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please fill in all grade fields!", 
                    "Input Required", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validate numeric values
            Integer.parseInt(labWork1);
            Integer.parseInt(labWork2);
            Integer.parseInt(labWork3);
            Integer.parseInt(prelimExam);
            Integer.parseInt(attendance);
            
            // Update the selected row
            tableModel.setValueAt(labWork1, selectedRow, 3);
            tableModel.setValueAt(labWork2, selectedRow, 4);
            tableModel.setValueAt(labWork3, selectedRow, 5);
            tableModel.setValueAt(prelimExam, selectedRow, 6);
            tableModel.setValueAt(attendance, selectedRow, 7);
            
            JOptionPane.showMessageDialog(this, 
                "Grades updated successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numeric values for grades!", 
                "Invalid Input", 
                JOptionPane.ERROR_MESSAGE);
        }
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
            clearFields();
            JOptionPane.showMessageDialog(this, 
                "Record deleted successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void clearFields() {
        txtStudentID.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtLabWork1.setText("");
        txtLabWork2.setText("");
        txtLabWork3.setText("");
        txtPrelimExam.setText("");
        txtAttendance.setText("");
    }
    
    private void searchStudent() {
        String searchID = txtSearch.getText().trim();
        
        if (searchID.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a Student ID to search!", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Search through the table
        boolean found = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String studentID = tableModel.getValueAt(i, 0).toString();
            
            if (studentID.equalsIgnoreCase(searchID)) {
                // Select and scroll to the row
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                populateFieldsFromSelectedRow();
                found = true;
                
                JOptionPane.showMessageDialog(this, 
                    "Student found!", 
                    "Search Result", 
                    JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
        
        if (!found) {
            JOptionPane.showMessageDialog(this, 
                "Student ID not found: " + searchID, 
                "Not Found", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showAllStudents() {
        table.clearSelection();
        txtSearch.setText("");
        clearFields();
        
        JOptionPane.showMessageDialog(this, 
            "Showing all " + tableModel.getRowCount() + " students", 
            "All Students", 
            JOptionPane.INFORMATION_MESSAGE);
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
            StudentRecordSystem_FIXED frame = new StudentRecordSystem_FIXED();
            frame.setVisible(true);
        });
    }
}