package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Employee class representing the employee entity in the system
 */
public class Employee {
    private int empId;
    private String firstName;
    private String lastName;
    private String ssn;
    private String jobTitle;
    private String division;
    private double salary;
    private List<PayStatement> payStatements;

    public Employee() {
        this.payStatements = new ArrayList<>();
    }

    public Employee(int empId, String firstName, String lastName, String ssn, 
                   String jobTitle, String division, double salary) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.jobTitle = jobTitle;
        this.division = division;
        this.salary = salary;
        this.payStatements = new ArrayList<>();
    }

    // Getters and Setters
    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public List<PayStatement> getPayStatements() {
        return payStatements;
    }

    public void setPayStatements(List<PayStatement> payStatements) {
        this.payStatements = payStatements;
    }

    public void addPayStatement(PayStatement payStatement) {
        this.payStatements.add(payStatement);
    }

    @Override
    public String toString() {
        return "Employee [ID=" + empId + ", Name=" + firstName + " " + lastName + 
               ", Job Title=" + jobTitle + ", Division=" + division + 
               ", Salary=$" + String.format("%.2f", salary) + "]";
    }
}
