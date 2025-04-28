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

        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(30);
        root.setVgap(20);
        root.setStyle("-fx-background-color: #1e1e2f;");

        // === Output Area ===
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(250);
        outputArea.setStyle("-fx-control-inner-background: #101020; -fx-text-fill: white; -fx-border-color: orange; -fx-border-width: 2;");

        Label outputLabel = new Label("OUTPUT");
        outputLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");

        VBox outputBox = new VBox(10, outputLabel, outputArea);

        // === Search Section ===
        Label searchLabel = new Label("SEARCH EMPLOYEE");
        searchLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        TextField searchField = new TextField();
        searchField.setPromptText("Name, ID, or SSN");
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> {
            Employee emp = db.searchEmployee(searchField.getText());
            if (emp != null) {
                outputArea.setText("Found: " + emp.getName() + " | ID: " + emp.getEmpId() +
                        " | Dept: " + emp.getDepartment() + "\nRole: " + emp.getRole() +
                        " | Salary: $" + emp.getSalary());
            } else {
                outputArea.setText("Employee not found.");
            }
        });
        VBox searchBox = new VBox(5, searchLabel, searchField, searchBtn);

        // === Update Salary Section ===
        Label updateLabel = new Label("UPDATE SALARY");
        updateLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        TextField updateIdField = new TextField();
        updateIdField.setPromptText("Employee ID");
        TextField updateSalaryField = new TextField();
        updateSalaryField.setPromptText("New Salary");
        Button updateSalaryBtn = new Button("Update");
        updateSalaryBtn.setOnAction(e -> {
            try {
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
            } catch (Exception ex) {
                outputArea.setText("Invalid input for ID or salary.");
            }
        });
        VBox updateBox = new VBox(5, updateLabel, updateIdField, updateSalaryField, updateSalaryBtn);

        // === Update Full Info Section ===
        Label fullUpdateLabel = new Label("UPDATE EMPLOYEE DATA");
        fullUpdateLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        TextField eidField = new TextField(); eidField.setPromptText("ID");
        TextField enameField = new TextField(); enameField.setPromptText("Name");
        TextField edepField = new TextField(); edepField.setPromptText("Department");
        TextField ejobField = new TextField(); ejobField.setPromptText("Job Title");
        TextField esalField = new TextField(); esalField.setPromptText("Salary");
        TextField essnField = new TextField(); essnField.setPromptText("SSN");
        TextField eroleField = new TextField(); eroleField.setPromptText("Role");
        Button fullUpdateBtn = new Button("Update Info");
        fullUpdateBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(eidField.getText());
                String name = enameField.getText();
                String dept = edepField.getText();
                String job = ejobField.getText();
                double salary = Double.parseDouble(esalField.getText());
                String ssn = essnField.getText();
                String role = eroleField.getText();

                Employee updatedEmp = new Employee(id, name, dept, job, salary, ssn, role);
                boolean updated = db.updateEmployee(updatedEmp);
                outputArea.setText(updated ? "✅ Employee updated successfully." : "❌ Update failed.");
            } catch (Exception ex) {
                outputArea.setText("❌ Invalid input: " + ex.getMessage());
            }
        });
        VBox fullUpdateBox = new VBox(5, fullUpdateLabel, eidField, enameField, edepField, ejobField, esalField, essnField, eroleField, fullUpdateBtn);

        // === Raise Salary Section ===
        Label rangeLabel = new Label("RAISE SALARY IN RANGE");
        rangeLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        TextField minField = new TextField(); minField.setPromptText("Min Salary");
        TextField maxField = new TextField(); maxField.setPromptText("Max Salary");
        TextField percentField = new TextField(); percentField.setPromptText("% Increase");
        Button rangeBtn = new Button("Apply");
        rangeBtn.setOnAction(e -> {
            try {
                double min = Double.parseDouble(minField.getText());
                double max = Double.parseDouble(maxField.getText());
                double percent = Double.parseDouble(percentField.getText());
                db.increaseSalaryInRange(percent, min, max);
                outputArea.setText("✅ Applied raise to employees between $" + min + " and $" + max + " by " + percent + "%");
            } catch (Exception ex) {
                outputArea.setText("Invalid input for salary range or percentage.");
            }
        });
        VBox raiseBox = new VBox(5, rangeLabel, minField, maxField, percentField, rangeBtn);

        // === Add Employee Section ===
        Label addLabel = new Label("ADD EMPLOYEE");
        addLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        TextField nameField = new TextField(); nameField.setPromptText("Name");
        TextField deptField = new TextField(); deptField.setPromptText("Department");
        TextField jobField = new TextField(); jobField.setPromptText("Job Title");
        TextField salaryField = new TextField(); salaryField.setPromptText("Salary");
        TextField ssnField = new TextField(); ssnField.setPromptText("SSN");
        TextField roleField = new TextField(); roleField.setPromptText("Role");
        Button addBtn = new Button("Add");
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
                    outputArea.setText("Employee added successfully!");
                }
            } catch (Exception ex) {
                outputArea.setText(" Failed to add employee: " + ex.getMessage());
            }
        });
        VBox addBox = new VBox(5, addLabel, nameField, deptField, jobField, salaryField, ssnField, roleField, addBtn);

        // === Reports Section ===
        Label reportLabel = new Label("REPORTS");
        reportLabel.setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
        TextField monthField = new TextField("January");
        TextField yearField = new TextField("2025");
        Button reportJobBtn = new Button("Total Pay by Job Title");
        reportJobBtn.setOnAction(e -> {
            String report = db.getTotalPayByJobTitleForMonth(monthField.getText(), Integer.parseInt(yearField.getText()));
            outputArea.setText(report);
        });
        Button reportDivBtn = new Button("Total Pay by Division");
        reportDivBtn.setOnAction(e -> {
            String report = db.getTotalPayByDivisionForMonth(monthField.getText(), Integer.parseInt(yearField.getText()));
            outputArea.setText(report);
        });
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
        Button historyBtn = new Button("Show Pay History");
        historyBtn.setOnAction(e -> {
            String history = db.getEmployeePayHistory();
            outputArea.setText(history);
        });
        VBox reportBox = new VBox(5, reportLabel, monthField, yearField, reportJobBtn, reportDivBtn, allBtn, historyBtn);

        VBox leftPanel = new VBox(15, searchBox, updateBox, raiseBox, addBox, fullUpdateBox);
        VBox rightPanel = new VBox(15, reportBox, outputBox);

        HBox content = new HBox(40, leftPanel, rightPanel);
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: #1e1e2f;");

        Scene scene = new Scene(scrollPane, 950, 700);
        stage.setScene(scene);
        stage.show();
    }
}
//UI
