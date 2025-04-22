import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDatabase {

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

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String increaseSalaryInRange(double percent, double min, double max) {
        String sql = "UPDATE employee SET salary = salary * ? WHERE salary >= ? AND salary < ?";
        StringBuilder result = new StringBuilder();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, 1 + percent / 100.0);
            stmt.setDouble(2, min);
            stmt.setDouble(3, max);

            int count = stmt.executeUpdate();
            result.append("✅ ").append(count).append(" employee(s) had their salary increased.\n");
        } catch (SQLException e) {
            result.append("❌ Error: ").append(e.getMessage()).append("\n");
        }
        return result.toString();
    }

    public String getEmployeePayHistory() {
        StringBuilder sb = new StringBuilder();
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

            int lastEmpId = -1;
            while (rs.next()) {
                int empId = rs.getInt("empid");
                if (empId != lastEmpId) {
                    sb.append(String.format("\n[%d] %s | Dept: %s | Title: %s | Role: %s | Current Salary: $%.2f\n",
                        empId,
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("job_title"),
                        rs.getString("role"),
                        rs.getDouble("salary")));
                    sb.append("Pay History:\n");
                    lastEmpId = empId;
                }
                String month = rs.getString("month");
                int year = rs.getInt("year");
                double amount = rs.getDouble("amount");
                if (month != null) {
                    sb.append(String.format("   - %s %d: $%.2f\n", month, year, amount));
                }
            }
        } catch (SQLException e) {
            sb.append("Error: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public String getTotalPayByJobTitleForMonth(String month, int year) {
        StringBuilder sb = new StringBuilder("=== Total Pay by Job Title for " + month + " " + year + " ===\n");
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
            while (rs.next()) {
                sb.append(String.format("%s: $%.2f\n", rs.getString("job_title"), rs.getDouble("total")));
            }
        } catch (SQLException e) {
            sb.append("Error: ").append(e.getMessage());
        }
        return sb.toString();
    }

    public String getTotalPayByDivisionForMonth(String month, int year) {
        StringBuilder sb = new StringBuilder("=== Total Pay by Division for " + month + " " + year + " ===\n");
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
            while (rs.next()) {
                sb.append(String.format("%s: $%.2f\n", rs.getString("division"), rs.getDouble("total")));
            }
        } catch (SQLException e) {
            sb.append("Error: ").append(e.getMessage());
        }
        return sb.toString();
    }

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
