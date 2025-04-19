package ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import ui.util.SampleDataGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Controller for the reports view
 */
public class ReportsViewController {

    @FXML
    private ComboBox<String> reportTypeComboBox;
    
    @FXML
    private ComboBox<Integer> yearComboBox;
    
    @FXML
    private ComboBox<String> monthComboBox;
    
    @FXML
    private Button generateButton;
    
    @FXML
    private TableView<ReportItem> reportTable;
    
    @FXML
    private TableColumn<ReportItem, String> categoryColumn;
    
    @FXML
    private TableColumn<ReportItem, Double> totalPayColumn;
    
    @FXML
    private Button exportButton;
    
    private ObservableList<ReportItem> reportItems;
    
    @FXML
    private void initialize() {
        reportItems = FXCollections.observableArrayList();
        
        // Initialize report type combo box
        reportTypeComboBox.setItems(FXCollections.observableArrayList(
            "Total Pay by Job Title", 
            "Total Pay by Division"
        ));
        reportTypeComboBox.getSelectionModel().selectFirst();
        
        // Initialize year combo box
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int year = 2025; year >= 2020; year--) {
            years.add(year);
        }
        yearComboBox.setItems(years);
        yearComboBox.getSelectionModel().selectFirst();
        
        // Initialize month combo box
        ObservableList<String> months = FXCollections.observableArrayList(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        );
        monthComboBox.setItems(months);
        monthComboBox.getSelectionModel().select(2); // March (index 2)
        
        // Initialize table columns
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        totalPayColumn.setCellValueFactory(new PropertyValueFactory<>("totalPay"));
        
        // Format total pay column to display as currency
        totalPayColumn.setCellFactory(column -> new TableCell<ReportItem, Double>() {
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
        reportTable.setItems(reportItems);
        
        // Disable export button initially
        exportButton.setDisable(true);
    }
    
    @FXML
    private void generateReport() {
        String reportType = reportTypeComboBox.getValue();
        Integer year = yearComboBox.getValue();
        String monthStr = monthComboBox.getValue();
        
        if (reportType == null || year == null || monthStr == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Missing Information", 
                     "Please select report type, year, and month.");
            return;
        }
        
        // Convert month string to month number (1-12)
        int month = monthComboBox.getSelectionModel().getSelectedIndex() + 1;
        
        // Clear previous report items
        reportItems.clear();
        
        List<Object[]> results;
        if ("Total Pay by Job Title".equals(reportType)) {
            results = SampleDataGenerator.getTotalPayByJobTitle(year, month);
        } else { // "Total Pay by Division"
            results = SampleDataGenerator.getTotalPayByDivision(year, month);
        }
        
        if (results.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Data", "No Data Found", 
                     "No data found for the specified period.");
            exportButton.setDisable(true);
            return;
        }
        
        // Add results to the table
        for (Object[] row : results) {
            String category = (String) row[0];
            Double totalPay = (Double) row[1];
            reportItems.add(new ReportItem(category, totalPay));
        }
        
        // Enable export button
        exportButton.setDisable(false);
    }
    
    @FXML
    private void exportReport() {
        if (reportItems.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No Data to Export", 
                     "Please generate a report first.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Report");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        
        String reportType = reportTypeComboBox.getValue();
        Integer year = yearComboBox.getValue();
        String month = monthComboBox.getValue();
        
        fileChooser.setInitialFileName(
            reportType.replace(" ", "_") + "_" + month + "_" + year + ".csv"
        );
        
        File file = fileChooser.showSaveDialog(reportTable.getScene().getWindow());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Write header
                writer.write("Category,Total Pay\n");
                
                // Write data
                for (ReportItem item : reportItems) {
                    writer.write(item.getCategory() + "," + String.format("%.2f", item.getTotalPay()) + "\n");
                }
                
                showAlert(Alert.AlertType.INFORMATION, "Success", "Export Successful", 
                         "Report has been exported successfully to " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Export Failed", 
                         "An error occurred while exporting the report: " + e.getMessage());
            }
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Helper class to represent a report item in the table
     */
    public static class ReportItem {
        private final String category;
        private final double totalPay;
        
        public ReportItem(String category, double totalPay) {
            this.category = category;
            this.totalPay = totalPay;
        }
        
        public String getCategory() {
            return category;
        }
        
        public double getTotalPay() {
            return totalPay;
        }
    }
}
