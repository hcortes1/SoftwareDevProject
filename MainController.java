package ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Controller for the main view of the application
 */
public class MainController {

    @FXML
    private Button btnEmployees;
    
    @FXML
    private Button btnReports;
    
    @FXML
    private Button btnSalaryUpdate;
    
    @FXML
    private StackPane contentArea;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private void initialize() {
        // Load the employees view by default
        showEmployeesView();
    }
    
    @FXML
    private void showEmployeesView() {
        loadView("/ui/fxml/EmployeesView.fxml");
        statusLabel.setText("Employees View");
    }
    
    @FXML
    private void showReportsView() {
        // We'll implement this later
        statusLabel.setText("Reports View - Not implemented yet");
    }
    
    @FXML
    private void showSalaryUpdateView() {
        // We'll implement this later
        statusLabel.setText("Salary Update View - Not implemented yet");
    }
    
    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(view);
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading view: " + e.getMessage());
        }
    }
}
