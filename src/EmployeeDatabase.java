import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDatabase {

    // 🔍 Search by empid, name, or ssn
    public Employee searchEmployee(String keyword) {
        String sql = "SELECT * FROM employee WHERE empid = ? OR name = ? OR ssn = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, keyword);
            stmt.setString(2, keyword);
            stmt.setString(3, keyword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Employee(
                    rs.getInt("empid"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("job_title"),
                    rs.getDouble("salary"),
                    rs.getString("ssn"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✏️ Update employee information
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE employee SET name = ?, department = ?, job_title = ?, salary = ?, ssn = ?, role = ? WHERE empid = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, emp.getName());
            stmt.setString(2, emp.getDepartment());
            stmt.setString(3, emp.getJobTitle());
            stmt.setDouble(4, emp.getSalary());
            stmt.setString(5, emp.getSsn());
            stmt.setString(6, emp.getRole());
            stmt.setInt(7, emp.getEmpId());

            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 💸 Increase salary for employees in a given range
    public void increaseSalaryInRange(double percent, double min, double max) {
        String sql = "UPDATE employee SET salary = salary * ? WHERE salary >= ? AND salary < ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, 1 + percent / 100.0);
            stmt.setDouble(2, min);
            stmt.setDouble(3, max);

            int affected = stmt.executeUpdate();
            System.out.println(affected + " employee(s) had their salary increased.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📋 Report 1: Show all employee info
    public void showAllEmployeeInfo() {
        String sql = "SELECT * FROM employee";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("=== All Employees ===");
            while (rs.next()) {
                System.out.printf("[%d] %s | Dept: %s | Title: %s | Salary: $%.2f | SSN: %s | Role: %s\n",
                        rs.getInt("empid"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("job_title"),
                        rs.getDouble("salary"),
                        rs.getString("ssn"),
                        rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📊 Full-time employee info with pay statement history
    public void showEmployeeWithPayHistory() {
        String sql = """
            SELECT e.empid, e.name, e.department, e.job_title, e.salary, e.role,
                   ps.month, ps.year, ps.amount
            FROM employee e
            LEFT JOIN pay_statement ps ON e.empid = ps.empid
            ORDER BY e.empid, ps.year, ps.month
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("=== Employee Info with Pay History ===");
            int lastEmpId = -1;
            while (rs.next()) {
                int empId = rs.getInt("empid");
                if (empId != lastEmpId) {
                    System.out.printf("\n[%d] %s | Dept: %s | Title: %s | Role: %s | Current Salary: $%.2f\n",
                            empId,
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getString("job_title"),
                            rs.getString("role"),
                            rs.getDouble("salary"));
                    System.out.println("Pay History:");
                    lastEmpId = empId;
                }
                String month = rs.getString("month");
                int year = rs.getInt("year");
                double amount = rs.getDouble("amount");
                if (month != null) {
                    System.out.printf("   - %s %d: $%.2f\n", month, year, amount);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📊 Total pay by job title for a given month
    public void totalPayByJobTitleForMonth(String month, int year) {
        String sql = """
            SELECT e.job_title, SUM(ps.amount) AS total
            FROM pay_statement ps
            JOIN employee e ON ps.empid = e.empid
            WHERE ps.month = ? AND ps.year = ?
            GROUP BY e.job_title
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, month);
            stmt.setInt(2, year);

            ResultSet rs = stmt.executeQuery();
            System.out.printf("=== Total Pay for %s %d by Job Title ===\n", month, year);
            while (rs.next()) {
                System.out.printf("%s: $%.2f\n", rs.getString("job_title"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📊 Total pay by division for a given month
    public void totalPayByDivisionForMonth(String month, int year) {
        String sql = """
            SELECT e.department AS division, SUM(ps.amount) AS total
            FROM pay_statement ps
            JOIN employee e ON ps.empid = e.empid
            WHERE ps.month = ? AND ps.year = ?
            GROUP BY e.department
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, month);
            stmt.setInt(2, year);

            ResultSet rs = stmt.executeQuery();
            System.out.printf("=== Total Pay for %s %d by Division ===\n", month, year);
            while (rs.next()) {
                System.out.printf("%s: $%.2f\n", rs.getString("division"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📋 Show total pay by job title (current salary)
    public void showTotalPayByJobTitle() {
        String sql = "SELECT job_title, SUM(salary) AS total_pay FROM employee GROUP BY job_title";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("=== Total Pay by Job Title (Current Salaries) ===");
            while (rs.next()) {
                System.out.printf("%s: $%.2f\n",
                        rs.getString("job_title"),
                        rs.getDouble("total_pay"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 📋 Show total pay by division (current salary)
    public void showTotalPayByDivision() {
        String sql = "SELECT department AS division, SUM(salary) AS total_pay FROM employee GROUP BY department";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("=== Total Pay by Division (Current Salaries) ===");
            while (rs.next()) {
                System.out.printf("%s: $%.2f\n",
                        rs.getString("division"),
                        rs.getDouble("total_pay"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔄 Return list of employees
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Employee(
                    rs.getInt("empid"),
                    rs.getString("name"),
                    rs.getString("department"),
                    rs.getString("job_title"),
                    rs.getDouble("salary"),
                    rs.getString("ssn"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}