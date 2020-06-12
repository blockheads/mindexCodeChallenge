package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportingStructureController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/reportingStructure/{id}")
    public ReportingStructure read(@PathVariable String id) {
        LOG.debug("Received Report Structure read request for id [{}]", id);
        // get the employee at the id
        Employee employee = employeeService.read(id);
        ReportingStructure reportingStructure = new ReportingStructure();
        reportingStructure.setEmployee(employee);

        return reportingStructure;
    }
}
