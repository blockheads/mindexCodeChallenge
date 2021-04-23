package com.mindex.challenge.data;

import com.mindex.challenge.dao.EmployeeRepository;

import java.util.Set;
import java.util.HashSet;

public class ReportingStructure{

    // employee
    private Employee employee;
    private Integer numberOfReports;

    public ReportingStructure(){
    }

    public Employee getEmployee(){return this.employee;}

    public void setEmployee(Employee employee){
        this.employee = employee;
    }

    // getting number of reports
    // returns -1 if no employee set with
    // this structure
    public Integer getNumberOfReports(EmployeeRepository employeeRepository){
        if(this.numberOfReports == null){
            Set<Employee> visited = new HashSet<>();
            this.numberOfReports = this.getReportsHelper(this.employee, visited, employeeRepository);
        }
        return this.numberOfReports;

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
