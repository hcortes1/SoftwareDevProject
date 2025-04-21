public class Employee {
    private int empId;
    private String name;
    private String department;
    private String jobTitle;
    private double salary;
    private String ssn;
    private String role; // 

    public Employee(int empId, String name, String department, String jobTitle, double salary, String ssn, String role) {
        this.empId = empId;
        this.name = name;
        this.department = department;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.ssn = ssn;
        this.role = role;
    }

    // ✅ Getters
    public int getEmpId() { return empId; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getJobTitle() { return jobTitle; }
    public double getSalary() { return salary; }
    public String getSsn() { return ssn; }
    public String getRole() { return role; }

    // ✅ Setters (optional)
    public void setName(String name) { this.name = name; }
    public void setDepartment(String department) { this.department = department; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setSsn(String ssn) { this.ssn = ssn; }
    public void setRole(String role) { this.role = role; }
}