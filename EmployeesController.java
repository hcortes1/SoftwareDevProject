package ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import model.Employee;
import dao.EmployeeDAO;

import java.util.List;

/**
 * Controller for the employees view
 */
public class EmployeesController {

    @FXML
    private TextField searchField;
    
    @FXML
    private ComboBox<String> searchTypeComboBox;
    
    @FXML
    private Button searchButton;
    
    @FXML
    private Button clearButton;
    
    @FXML
    private TableView<Employee> employeeTable;
    
    @FXML
    private TableColumn<Employee, Integer> idColumn;
    
    @FXML
    private TableColumn<Employee, String> firstNameColumn;
    
    @FXML
    private TableColumn<Employee, String> lastNameColumn;
    
    @FXML
    private TableColumn<Employee, String> ssnColumn;
    
    @FXML
    private TableColumn<Employee, String> jobTitleColumn;
    
    @FXML
    private TableColumn<Employee, String> divisionColumn;
    
    @FXML
    private TableColumn<Employee, Double> salaryColumn;
    
    @FXML
    private Button addButton;
    
    @FXML
    private Button editButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Button viewPayStatementsButton;
    
    private EmployeeDAO employeeDAO;
    private ObservableList<Employee> employeeList;
    
    @FXML
    private void initialize() {
        employeeDAO = new EmployeeDAO();
        employeeList = FXCollections.observableArrayList();
        
        // Initialize search type combo box
        searchTypeComboBox.setItems(FXCollections.observableArrayList("Name", "SSN", "ID"));
        searchTypeComboBox.getSelectionModel().selectFirst();
        
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("empId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        ssnColumn.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        jobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        
        // Format salary column to display as currency
        salaryColumn.setCellFactory(column -> new TableCell<Employee, Double>() {
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
        
        // Load all employees
        loadEmployees();
        
        // Disable buttons when no employee is selected
        editButton.setDisable(true);
        deleteButton.setDisable(true);
        viewPayStatementsButton.setDisable(true);
        
        // Enable buttons when an employee is selected
        employeeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            editButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
            viewPayStatementsButton.setDisable(!hasSelection);
        });
    }
    
    private void loadEmployees() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            employeeList.clear();
            employeeList.addAll(employees);
            employeeTable.setItems(employeeList);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading employees", e.getMessage());
        }
    }
    
    @FXML
    private void searchEmployee() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            loadEmployees();
            return;
        }
        
        String searchType = searchTypeComboBox.getValue();
        try {
            if ("Name".equals(searchType)) {
                List<Employee> employees = employeeDAO.searchEmployeesByName(searchText);
                employeeList.clear();
                employeeList.addAll(employees);
            } else if ("SSN".equals(searchType)) {
                Employee employee = employeeDAO.searchEmployeeBySSN(searchText);
                employeeList.clear();
                if (employee != null) {
                    employeeList.add(employee);
                }
            } else if ("ID".equals(searchType)) {
                try {
                    int empId = Integer.parseInt(searchText);
                    Employee employee = employeeDAO.getEmployeeById(empId);
                    employeeList.clear();
                    if (employee != null) {
                        employeeList.add(employee);
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid ID", "Please enter a valid employee ID.");
                }
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error searching employees", e.getMessage());
        }
    }
    
    @FXML
    private void clearSearch() {
        searchField.clear();
        loadEmployees();
    }
    
    @FXML
    private void showAddEmployeeDialog() {
        // This will be implemented later
        showAlert(Alert.AlertType.INFORMATION, "Not Implemented", "Add Employee", 
                 "This feature is not yet implemented.");
    }
    
    @FXML
    private void showEditEmployeeDialog() {
        // This will be implemented later
        showAlert(Alert.AlertType.INFORMATION, "Not Implemented", "Edit Employee", 
                 "This feature is not yet implemented.");
    }
    
    @FXML
    private void deleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Employee");
        alert.setContentText("Are you sure you want to delete employee " + 
                            selectedEmployee.getFirstName() + " " + 
                            selectedEmployee.getLastName() + "?");
        
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                boolean success = employeeDAO.deleteEmployee(selectedEmployee.getEmpId());
                if (success) {
                    employeeList.remove(selectedEmployee);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Employee Deleted", 
                             "Employee has been deleted successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Delete Failed", 
                             "Failed to delete employee.");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error deleting employee", e.getMessage());
            }
        }
    }
    
    @FXML
    private void viewPayStatements() {
        // This will be implemented later
        showAlert(Alert.AlertType.INFORMATION, "Not Implemented", "View Pay Statements", 
                 "This feature is not yet implemented.");
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
