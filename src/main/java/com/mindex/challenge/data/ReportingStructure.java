package com.mindex.challenge.data;

import com.mindex.challenge.dao.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.HashSet;

public class ReportingStructure{

    // employee
    private Employee employee;

    public ReportingStructure(){
    }

    public Employee getEmployee(){return this.employee;}

    public void setEmployee(Employee employee){
        this.employee = employee;
    }

    // getting number of reports
    // returns -1 if no employee set with
    // this structure
    public int calculateReports(EmployeeRepository employeeRepository){
        Set<Employee> visited = new HashSet<>();
        return this.getReportsHelper(this.employee, visited, employeeRepository);
    }


    // helper for easy recursive call
    private int getReportsHelper(Employee curr, Set<Employee> visited, EmployeeRepository employeeRepository) {
        int total = 0;
        // base case

        if (curr == null || curr.getDirectReports() == null) return 0;

        // recurse on non visited.
        else {
            for (Employee child : curr.getDirectReports()) {
                child = employeeRepository.findByEmployeeId(child.getEmployeeId());
                 if (!visited.contains(child)) {
                    visited.add(child);
                    total += getReportsHelper(child, visited, employeeRepository) + 1;
                }
            }
        }

        return total;
    }
}
