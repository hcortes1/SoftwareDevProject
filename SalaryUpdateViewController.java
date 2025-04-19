package ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import model.Employee;
import ui.util.SampleDataGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the salary update view
 */
public class SalaryUpdateViewController {

    @FXML
    private TextField minSalaryField;
    
    @FXML
    private TextField maxSalaryField;
    
    @FXML
    private TextField percentageField;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private TableView<AffectedEmployee> affectedEmployeesTable;
    
    @FXML
    private TableColumn<AffectedEmployee, Integer> idColumn;
    
    @FXML
    private TableColumn<AffectedEmployee, String> nameColumn;
    
    @FXML
    private TableColumn<AffectedEmployee, String> jobTitleColumn;
    
    @FXML
    private TableColumn<AffectedEmployee, String> divisionColumn;
    
    @FXML
    private TableColumn<AffectedEmployee, Double> currentSalaryColumn;
    
    @FXML
    private TableColumn<AffectedEmployee, Double> newSalaryColumn;
    
    private ObservableList<AffectedEmployee> affectedEmployees;
    
    @FXML
    private void initialize() {
        affectedEmployees = FXCollections.observableArrayList();
        
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        jobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        currentSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("currentSalary"));
        newSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("newSalary"));
        
        // Format salary columns to display as currency
        currentSalaryColumn.setCellFactory(column -> new TableCell<AffectedEmployee, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        
        newSalaryColumn.setCellFactory(column -> new TableCell<AffectedEmployee, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));
                }
            }
        });
        
        // Set the items
        affectedEmployeesTable.setItems(affectedEmployees);
    }
    
    @FXML
    private void updateSalaries() {
        try {
            // Parse input values
            double minSalary = Double.parseDouble(minSalaryField.getText().trim());
            double maxSalary = Double.parseDouble(maxSalaryField.getText().trim());
            double percentageIncrease = Double.parseDouble(percentageField.getText().trim());
            
            // Validate input values
            if (minSalary < 0 || maxSalary < 0 || percentageIncrease < 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid Values", 
                         "Please enter positive values for all fields.");
                return;
            }
            
            if (minSalary >= maxSalary) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid Salary Range", 
                         "Minimum salary must be less than maximum salary.");
                return;
            }
            
            // Find affected employees
            List<Employee> allEmployees = SampleDataGenerator.getAllEmployees();
            List<AffectedEmployee> affected = new ArrayList<>();
            
            for (Employee emp : allEmployees) {
                double currentSalary = emp.getSalary();
                if (currentSalary >= minSalary && currentSalary < maxSalary) {
                    double newSalary = currentSalary * (1 + percentageIncrease / 100);
                    affected.add(new AffectedEmployee(
                        emp.getEmpId(),
                        emp.getFirstName() + " " + emp.getLastName(),
                        emp.getJobTitle(),
                        emp.getDivision(),
                        currentSalary,
                        newSalary
                    ));
                }
            }
            
            if (affected.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Employees Found", "No Affected Employees", 
                         "No employees found within the specified salary range.");
                return;
            }
            
            // Update the table
            affectedEmployees.clear();
            affectedEmployees.addAll(affected);
            
            // Confirm update
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Update");
            alert.setHeaderText("Update Salaries");
            alert.setContentText("Are you sure you want to update salaries for " + 
                                affected.size() + " employee(s) with a " + 
                                percentageIncrease + "% increase?");
            
            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Perform the update
                int updatedCount = SampleDataGenerator.updateSalariesInRange(minSalary, maxSalary, percentageIncrease);
                
                showAlert(Alert.AlertType.INFORMATION, "Success", "Salaries Updated", 
                         updatedCount + " employee(s) updated with a " + 
                         percentageIncrease + "% salary increase.");
                
                // Refresh the table to show the updated salaries
                affectedEmployees.clear();
                for (Employee emp : SampleDataGenerator.getAllEmployees()) {
                    double currentSalary = emp.getSalary();
                    for (AffectedEmployee ae : affected) {
                        if (ae.getId() == emp.getEmpId()) {
                            // This employee was affected, update with the new salary
                            affected.remove(ae);
                            affected.add(new AffectedEmployee(
                                emp.getEmpId(),
                                emp.getFirstName() + " " + emp.getLastName(),
                                emp.getJobTitle(),
                                emp.getDivision(),
                                ae.getCurrentSalary(), // Keep the original salary for reference
                                currentSalary // Updated salary
                            ));
                            break;
                        }
                    }
                }
                affectedEmployees.addAll(affected);
            }
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid Number Format", 
                     "Please enter valid numbers for all fields.");
        }
    }
    
    @FXML
    private void clearFields() {
        minSalaryField.clear();
        maxSalaryField.clear();
        percentageField.clear();
        affectedEmployees.clear();
        minSalaryField.requestFocus();
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Helper class to represent an affected employee in the table
     */
    public static class AffectedEmployee {
        private final int id;
        private final String name;
        private final String jobTitle;
        private final String division;
        private final double currentSalary;
        private final double newSalary;
        
        public AffectedEmployee(int id, String name, String jobTitle, String division, 
                               double currentSalary, double newSalary) {
            this.id = id;
            this.name = name;
            this.jobTitle = jobTitle;
            this.division = division;
            this.currentSalary = currentSalary;
            this.newSalary = newSalary;
        }
        
        public int getId() {
            return id;
        }
        
        public String getName() {
            return name;
        }
        
        public String getJobTitle() {
            return jobTitle;
        }
        
        public String getDivision() {
            return division;
        }
        
        public double getCurrentSalary() {
            return currentSalary;
        }
        
        public double getNewSalary() {
            return newSalary;
        }
    }
}
