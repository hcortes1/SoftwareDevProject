package model;

import java.time.LocalDate;

/**
 * PayStatement class representing an employee's pay statement
 */
public class PayStatement {
    private int statementId;
    private int empId;
    private LocalDate payDate;
    private double grossPay;
    private double netPay;
    private double taxDeductions;
    private double otherDeductions;
    
    public PayStatement() {
    }
    
    public PayStatement(int statementId, int empId, LocalDate payDate, double grossPay, 
                       double taxDeductions, double otherDeductions) {
        this.statementId = statementId;
        this.empId = empId;
        this.payDate = payDate;
        this.grossPay = grossPay;
        this.taxDeductions = taxDeductions;
        this.otherDeductions = otherDeductions;
        this.netPay = grossPay - taxDeductions - otherDeductions;
    }
    
    // Getters and Setters
    public int getStatementId() {
        return statementId;
    }
    
    public void setStatementId(int statementId) {
        this.statementId = statementId;
    }
    
    public int getEmpId() {
        return empId;
    }
    
    public void setEmpId(int empId) {
        this.empId = empId;
    }
    
    public LocalDate getPayDate() {
        return payDate;
    }
    
    public void setPayDate(LocalDate payDate) {
        this.payDate = payDate;
    }
    
    public double getGrossPay() {
        return grossPay;
    }
    
    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
        calculateNetPay();
    }
    
    public double getNetPay() {
        return netPay;
    }
    
    public double getTaxDeductions() {
        return taxDeductions;
    }
    
    public void setTaxDeductions(double taxDeductions) {
        this.taxDeductions = taxDeductions;
        calculateNetPay();
    }
    
    public double getOtherDeductions() {
        return otherDeductions;
    }
    
    public void setOtherDeductions(double otherDeductions) {
        this.otherDeductions = otherDeductions;
        calculateNetPay();
    }
    
    private void calculateNetPay() {
        this.netPay = this.grossPay - this.taxDeductions - this.otherDeductions;
    }
    
    @Override
    public String toString() {
        return "PayStatement [ID=" + statementId + ", Date=" + payDate + 
               ", Gross=$" + String.format("%.2f", grossPay) + 
               ", Net=$" + String.format("%.2f", netPay) + "]";
    }
}
