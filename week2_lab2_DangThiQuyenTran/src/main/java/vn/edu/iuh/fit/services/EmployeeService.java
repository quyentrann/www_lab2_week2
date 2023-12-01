package vn.edu.iuh.fit.services;

import vn.edu.iuh.fit.models.Employee;
import vn.edu.iuh.fit.repositories.EmployeeRepository;
import java.util.List;

public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService() {
        employeeRepository = new EmployeeRepository();
    }

    public boolean createEmployee(Employee employee) {
        return employeeRepository.createEmployee(employee);
    }

    public boolean updateEmployee(Employee employee) {
        return employeeRepository.updateEmployee(employee);
    }

    public boolean deleteEmployee(long empId) {
        return employeeRepository.deleteEmployee(empId);
    }

    public Employee getEmployeeById(long empId) {
        return employeeRepository.getEmployeeById(empId);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }
}