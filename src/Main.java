import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EmployeeDatabase db = new EmployeeDatabase();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Employee Database Menu ===");
            System.out.println("1. Search Employee");
            System.out.println("2. Update Employee Salary");
            System.out.println("3. Increase Salary for Range");
            System.out.println("4. Show All Employees");
            System.out.println("5. Show Employee Info with Pay History");
            System.out.println("6. Total Pay by Job Title for a Month");
            System.out.println("7. Total Pay by Division for a Month");
            System.out.println("0. Exit");
            System.out.print("Enter choice (0–7): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 0:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;

                case 1:
                    System.out.print("Enter name, ID, or SSN to search: ");
                    String keyword = scanner.nextLine();
                    Employee found = db.searchEmployee(keyword);
                    if (found != null) {
                        System.out.printf("Found: %s | ID: %d | Dept: %s | Title: %s | Salary: $%.2f | SSN: %s | Role: %s\n",
                            found.getName(),
                            found.getEmpId(),
                            found.getDepartment(),
                            found.getJobTitle(),
                            found.getSalary(),
                            found.getSsn(),
                            found.getRole()
                        );
                    } else {
                        System.out.println("No employee found.");
                    }
                    break;

                case 2:
                    System.out.print("Enter Employee ID to update: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new salary: ");
                    double newSalary = scanner.nextDouble();
                    scanner.nextLine();

                    Employee emp = db.searchEmployee(String.valueOf(id));
                    if (emp != null) {
                        Employee updatedEmp = new Employee(
                            emp.getEmpId(),
                            emp.getName(),
                            emp.getDepartment(),
                            emp.getJobTitle(),
                            newSalary,
                            emp.getSsn(),
                            emp.getRole()
                        );
                        boolean updated = db.updateEmployee(updatedEmp);
                        System.out.println(updated ? "✅ Updated successfully." : "❌ Update failed.");
                    } else {
                        System.out.println("❌ Employee not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter minimum salary: ");
                    double min = scanner.nextDouble();
                    System.out.print("Enter maximum salary: ");
                    double max = scanner.nextDouble();
                    System.out.print("Enter percentage to increase: ");
                    double percent = scanner.nextDouble();
                    db.increaseSalaryInRange(percent, min, max);
                    break;

                case 4:
                    List<Employee> employees = db.getAllEmployees();
                    System.out.println("=== Employee List ===");
                    for (Employee e : employees) {
                        System.out.printf("[%d] %s | %s | %s | $%.2f | SSN: %s | Role: %s\n",
                            e.getEmpId(),
                            e.getName(),
                            e.getDepartment(),
                            e.getJobTitle(),
                            e.getSalary(),
                            e.getSsn(),
                            e.getRole()
                        );
                    }
                    break;

                case 5:
                    db.showEmployeeWithPayHistory();
                    break;

                case 6:
                    System.out.print("Enter month (e.g., January): ");
                    String jobMonth = scanner.nextLine();
                    System.out.print("Enter year (e.g., 2024): ");
                    int jobYear = scanner.nextInt();
                    scanner.nextLine();
                    db.totalPayByJobTitleForMonth(jobMonth, jobYear);
                    break;

                case 7:
                    System.out.print("Enter month (e.g., January): ");
                    String divMonth = scanner.nextLine();
                    System.out.print("Enter year (e.g., 2024): ");
                    int divYear = scanner.nextInt();
                    scanner.nextLine();
                    db.totalPayByDivisionForMonth(divMonth, divYear);
                    break;

                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
}


