import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class MainFX extends Application {
    private TextArea outputArea;
    private EmployeeDatabase db = new EmployeeDatabase();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Employee Management System");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        // Output Area
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);

        // Search Section
        TextField searchField = new TextField();
        searchField.setPromptText("Enter name, ID or SSN");
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> {
            Employee emp = db.searchEmployee(searchField.getText());
            if (emp != null) {
                outputArea.setText("Found: " + emp.getName() + " | ID: " + emp.getEmpId() +
                        " | Dept: " + emp.getDepartment() + " | Role: " + emp.getRole());
            } else {
                outputArea.setText("Employee not found.");
            }
        });

        // Update Salary Section
        TextField updateIdField = new TextField();
        updateIdField.setPromptText("Employee ID");
        TextField updateSalaryField = new TextField();
        updateSalaryField.setPromptText("New Salary");
        Button updateSalaryBtn = new Button("Update Salary");
        updateSalaryBtn.setOnAction(e -> {
            int id = Integer.parseInt(updateIdField.getText());
            double salary = Double.parseDouble(updateSalaryField.getText());
            Employee emp = db.searchEmployee(String.valueOf(id));
            if (emp != null) {
                Employee updatedEmp = new Employee(id, emp.getName(), emp.getDepartment(),
                        emp.getJobTitle(), salary, emp.getSsn(), emp.getRole());
                boolean updated = db.updateEmployee(updatedEmp);
                outputArea.setText(updated ? "Salary updated." : "Update failed.");
            } else {
                outputArea.setText("Employee not found.");
            }
        });

        // Increase Salary Range Section
        TextField minField = new TextField(); minField.setPromptText("Min Salary");
        TextField maxField = new TextField(); maxField.setPromptText("Max Salary");
        TextField percentField = new TextField(); percentField.setPromptText("% Increase");
        Button rangeBtn = new Button("Apply Raise to Range");
        rangeBtn.setOnAction(e -> {
            double min = Double.parseDouble(minField.getText());
            double max = Double.parseDouble(maxField.getText());
            double percent = Double.parseDouble(percentField.getText());
            db.increaseSalaryInRange(percent, min, max);
            outputArea.setText("Applied raise to salary range.");
        });

        // Show All Employees
        Button allBtn = new Button("Show All Employees");
        allBtn.setOnAction(e -> {
            List<Employee> list = db.getAllEmployees();
            StringBuilder sb = new StringBuilder();
            for (Employee emp : list) {
                sb.append("[" + emp.getEmpId() + "] " + emp.getName() + " | " + emp.getDepartment()
                        + " | " + emp.getRole() + " | $" + emp.getSalary() + "\n");
            }
            outputArea.setText(sb.toString());
        });

        // Pay History
        Button historyBtn = new Button("Show Pay History");
        historyBtn.setOnAction(e -> db.showEmployeeWithPayHistory());

        // Report by Job Title
        TextField monthField1 = new TextField("January");
        TextField yearField1 = new TextField("2025");
        Button reportJobBtn = new Button("Total Pay by Job Title");
        reportJobBtn.setOnAction(e -> db.totalPayByJobTitleForMonth(monthField1.getText(), Integer.parseInt(yearField1.getText())));

        // Report by Division
        TextField monthField2 = new TextField("January");
        TextField yearField2 = new TextField("2025");
        Button reportDivBtn = new Button("Total Pay by Division");
        reportDivBtn.setOnAction(e -> db.totalPayByDivisionForMonth(monthField2.getText(), Integer.parseInt(yearField2.getText())));

        // Add Employee (Now Working)
        TextField nameField = new TextField(); nameField.setPromptText("Name");
        TextField deptField = new TextField(); deptField.setPromptText("Department");
        TextField jobField = new TextField(); jobField.setPromptText("Job Title");
        TextField salaryField = new TextField(); salaryField.setPromptText("Salary");
        TextField ssnField = new TextField(); ssnField.setPromptText("SSN");
        TextField roleField = new TextField(); roleField.setPromptText("Role");
        Button addBtn = new Button("Add Employee");
        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String dept = deptField.getText();
                String job = jobField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                String ssn = ssnField.getText();
                String role = roleField.getText();

                String sql = "INSERT INTO employee (name, department, job_title, salary, ssn, role) VALUES (?,?,?,?,?,?)";
                try (var conn = DBConnection.getConnection(); var stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    stmt.setString(2, dept);
                    stmt.setString(3, job);
                    stmt.setDouble(4, salary);
                    stmt.setString(5, ssn);
                    stmt.setString(6, role);
                    stmt.executeUpdate();
                    outputArea.setText(" Employee added successfully!");
                }
            } catch (Exception ex) {
                outputArea.setText("❌ Failed to add employee: " + ex.getMessage());
            }
        });

        layout.getChildren().addAll(
                new Label("Search Employee"), searchField, searchBtn,
                new Label("Update Salary"), updateIdField, updateSalaryField, updateSalaryBtn,
                new Label("Raise Salary in Range"), minField, maxField, percentField, rangeBtn,
                new Label("Reports"), monthField1, yearField1, reportJobBtn, monthField2, yearField2, reportDivBtn,
                allBtn, historyBtn,
                new Label("Add Employee"), nameField, deptField, jobField, salaryField, ssnField, roleField, addBtn,
                new Label("Output"), outputArea
        );

        stage.setScene(new Scene(layout, 600, 800));
        stage.show();
    }
}