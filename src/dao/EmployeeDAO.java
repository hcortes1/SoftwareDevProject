package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Employee;
import model.PayStatement;
import util.DBConnection; // Update the import for DBConnection to use the correct package

/**
 * Data Access Object for Employee-related database operations
 */
public class EmployeeDAO {
    
    /**
     * Get all employees from the database
     * @return List of all employees
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Employee employee = mapResultSetToEmployee(rs);
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employees: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Get an employee by ID
     * @param empId Employee ID
     * @return Employee object if found, null otherwise
     */
    public Employee getEmployeeById(int empId) {
        String sql = "SELECT * FROM employee WHERE empid = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, empId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Employee employee = mapResultSetToEmployee(rs);
                    loadPayStatements(employee);
                    return employee;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employee by ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Search for employees by name
     * @param name Name to search for (first or last)
     * @return List of matching employees
     */
    public List<Employee> searchEmployeesByName(String name) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE firstName LIKE ? OR lastName LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            pstmt.setString(2, "%" + name + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching employees by name: " + e.getMessage());
        }
        
        return employees;
    }
    
    /**
     * Search for an employee by SSN
     * @param ssn SSN to search for
     * @return Employee if found, null otherwise
     */
    public Employee searchEmployeeBySSN(String ssn) {
        String sql = "SELECT * FROM employee WHERE ssn = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, ssn);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Employee employee = mapResultSetToEmployee(rs);
                    loadPayStatements(employee);
                    return employee;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching employee by SSN: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Insert a new employee into the database
     * @param employee Employee to insert
     * @return true if successful, false otherwise
     */
    public boolean insertEmployee(Employee employee) {
        String sql = "INSERT INTO employee (firstName, lastName, ssn, jobTitle, division, salary) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getSsn());
            pstmt.setString(4, employee.getJobTitle());
            pstmt.setString(5, employee.getDivision());
            pstmt.setDouble(6, employee.getSalary());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setEmpId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting employee: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update an existing employee in the database
     * @param employee Employee to update
     * @return true if successful, false otherwise
     */
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employee SET firstName = ?, lastName = ?, ssn = ?, jobTitle = ?, division = ?, salary = ? WHERE empid = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getSsn());
            pstmt.setString(4, employee.getJobTitle());
            pstmt.setString(5, employee.getDivision());
            pstmt.setDouble(6, employee.getSalary());
            pstmt.setInt(7, employee.getEmpId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Delete an employee from the database
     * @param empId ID of employee to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteEmployee(int empId) {
        String sql = "DELETE FROM employee WHERE empid = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, empId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update salaries for employees within a specific salary range
     * @param minSalary Minimum salary in the range
     * @param maxSalary Maximum salary in the range
     * @param percentageIncrease Percentage increase to apply
     * @return Number of employees updated
     */
    public int updateSalariesInRange(double minSalary, double maxSalary, double percentageIncrease) {
        String sql = "UPDATE employee SET salary = salary * (1 + ? / 100) WHERE salary >= ? AND salary < ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, percentageIncrease);
            pstmt.setDouble(2, minSalary);
            pstmt.setDouble(3, maxSalary);
            
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating salaries: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Get total pay for a month by job title
     * @param year Year of the month
     * @param month Month (1-12)
     * @return List of job titles with their total pay
     */
    public List<Object[]> getTotalPayByJobTitle(int year, int month) {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT e.jobTitle, SUM(p.grossPay) as totalPay " +
                     "FROM employee e " +
                     "JOIN paystatement p ON e.empid = p.empid " +
                     "WHERE YEAR(p.payDate) = ? AND MONTH(p.payDate) = ? " +
                     "GROUP BY e.jobTitle";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[2];
                    row[0] = rs.getString("jobTitle");
                    row[1] = rs.getDouble("totalPay");
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting total pay by job title: " + e.getMessage());
        }
        
        return results;
    }
    
    /**
     * Get total pay for a month by division
     * @param year Year of the month
     * @param month Month (1-12)
     * @return List of divisions with their total pay
     */
    public List<Object[]> getTotalPayByDivision(int year, int month) {
        List<Object[]> results = new ArrayList<>();
        String sql = "SELECT e.division, SUM(p.grossPay) as totalPay " +
                     "FROM employee e " +
                     "JOIN paystatement p ON e.empid = p.empid " +
                     "WHERE YEAR(p.payDate) = ? AND MONTH(p.payDate) = ? " +
                     "GROUP BY e.division";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = new Object[2];
                    row[0] = rs.getString("division");
                    row[1] = rs.getDouble("totalPay");
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting total pay by division: " + e.getMessage());
        }
        
        return results;
    }
    
    /**
     * Load pay statements for an employee
     * @param employee Employee to load pay statements for
     */
    private void loadPayStatements(Employee employee) {
        String sql = "SELECT * FROM paystatement WHERE empid = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, employee.getEmpId());
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PayStatement payStatement = new PayStatement(
                        rs.getInt("statementid"),
                        rs.getInt("empid"),
                        rs.getDate("payDate").toLocalDate(),
                        rs.getDouble("grossPay"),
                        rs.getDouble("taxDeductions"),
                        rs.getDouble("otherDeductions")
                    );
                    employee.addPayStatement(payStatement);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading pay statements: " + e.getMessage());
        }
    }
    
    /**
     * Map a ResultSet to an Employee object
     * @param rs ResultSet containing employee data
     * @return Employee object
     * @throws SQLException if a database access error occurs
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmpId(rs.getInt("empid"));
        employee.setFirstName(rs.getString("firstName"));
        employee.setLastName(rs.getString("lastName"));
        employee.setSsn(rs.getString("ssn"));
        employee.setJobTitle(rs.getString("jobTitle"));
        employee.setDivision(rs.getString("division"));
        employee.setSalary(rs.getDouble("salary"));
        return employee;
    }
}
