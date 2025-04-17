package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.PayStatement;
import util.DBConnection;

/**
 * Data Access Object for PayStatement-related database operations
 */
public class PayStatementDAO {
    
    /**
     * Get all pay statements for an employee
     * @param empId Employee ID
     * @return List of pay statements
     */
    public List<PayStatement> getPayStatementsByEmployeeId(int empId) {
        List<PayStatement> payStatements = new ArrayList<>();
        String sql = "SELECT * FROM paystatement WHERE empid = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, empId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PayStatement payStatement = mapResultSetToPayStatement(rs);
                    payStatements.add(payStatement);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving pay statements: " + e.getMessage());
        }
        
        return payStatements;
    }
    
    /**
     * Insert a new pay statement into the database
     * @param payStatement PayStatement to insert
     * @return true if successful, false otherwise
     */
    public boolean insertPayStatement(PayStatement payStatement) {
        String sql = "INSERT INTO paystatement (empid, payDate, grossPay, taxDeductions, otherDeductions) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, payStatement.getEmpId());
            pstmt.setDate(2, Date.valueOf(payStatement.getPayDate()));
            pstmt.setDouble(3, payStatement.getGrossPay());
            pstmt.setDouble(4, payStatement.getTaxDeductions());
            pstmt.setDouble(5, payStatement.getOtherDeductions());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        payStatement.setStatementId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting pay statement: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Update an existing pay statement in the database
     * @param payStatement PayStatement to update
     * @return true if successful, false otherwise
     */
    public boolean updatePayStatement(PayStatement payStatement) {
        String sql = "UPDATE paystatement SET payDate = ?, grossPay = ?, taxDeductions = ?, otherDeductions = ? WHERE statementid = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, Date.valueOf(payStatement.getPayDate()));
            pstmt.setDouble(2, payStatement.getGrossPay());
            pstmt.setDouble(3, payStatement.getTaxDeductions());
            pstmt.setDouble(4, payStatement.getOtherDeductions());
            pstmt.setInt(5, payStatement.getStatementId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating pay statement: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Delete a pay statement from the database
     * @param statementId ID of pay statement to delete
     * @return true if successful, false otherwise
     */
    public boolean deletePayStatement(int statementId) {
        String sql = "DELETE FROM paystatement WHERE statementid = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, statementId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting pay statement: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Map a ResultSet to a PayStatement object
     * @param rs ResultSet containing pay statement data
     * @return PayStatement object
     * @throws SQLException if a database access error occurs
     */
    private PayStatement mapResultSetToPayStatement(ResultSet rs) throws SQLException {
        PayStatement payStatement = new PayStatement();
        payStatement.setStatementId(rs.getInt("statementid"));
        payStatement.setEmpId(rs.getInt("empid"));
        payStatement.setPayDate(rs.getDate("payDate").toLocalDate());
        payStatement.setGrossPay(rs.getDouble("grossPay"));
        payStatement.setTaxDeductions(rs.getDouble("taxDeductions"));
        payStatement.setOtherDeductions(rs.getDouble("otherDeductions"));
        return payStatement;
    }
}
