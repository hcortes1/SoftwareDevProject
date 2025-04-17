public class employee
{
    protected String name;
    private int employeeID;
    private double salary;
    private int socialSecurityNumber;
    private division myDivision;
    private role myRole;
    public employee(String n, int id, double sal, int ssn, division division, role role)
    {
        name = n;
        employeeID = id;
        salary = sal;
        socialSecurityNumber = ssn;
        myDivision = division;
        myRole = role;
    }

    public static String GetName(employee em)
    {
        return em.name;
    }

    public static int GetEmployeeID(employee em)
    {
        return em.employeeID;
    }

    public static double GetSalary(employee em)
    {
        return em.salary;
    }

    public static int GetSSN(employee em)
    {
        return em.socialSecurityNumber;
    }

    public static division GetDivision(employee em)
    {
        return em.myDivision;
    }

    public static role GetRole(employee em)
    {
        return em.myRole;
    }

    public static void main(String[] args) {
        employee em1 = new employee("Isaac", 1, 1000, 10100, division.HR, role.HR_REPRESENTATIVE);
        System.out.println(GetName(em1));
        EmployeeManager.updateName(em1);
        System.out.println(GetName(em1));
    }
}