package com.mindex.challenge.data;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String employeeUrl;
    private String structureIdUrl;

    private final String JOHN_LENNON_ID = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    private final String PAUL_MCARTNEY_ID = "b7839309-3348-463b-a7e3-5de1c168beb3";
    private final String RINGO_STARR_ID = "03aa1462-ffa9-4978-901b-7c001562cf6f";

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        structureIdUrl =  "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testReportStructure(){

        // here we test a employee we are sure of

        // testing the employees guarnteed in the employee_database.json
        Employee john = employeeRepository.findByEmployeeId(JOHN_LENNON_ID);

        // now let's get back a ReportingStructure
        ReportingStructure johnStructure =  restTemplate.getForEntity(structureIdUrl, ReportingStructure.class, john.getEmployeeId()).getBody();
        // now get reports
        assertEquals(4, johnStructure.calculateReports(employeeRepository));

        // now let's get back a ReportingStructure
        ReportingStructure paulStructure =  restTemplate.getForEntity(structureIdUrl, ReportingStructure.class, PAUL_MCARTNEY_ID).getBody();
        assertEquals(0, paulStructure.calculateReports(employeeRepository));


        ReportingStructure ringoStructure =  restTemplate.getForEntity(structureIdUrl, ReportingStructure.class, RINGO_STARR_ID).getBody();
        assertEquals(2, ringoStructure.calculateReports(employeeRepository));


        // testing a new employee
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // we need to post our test employee
        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        assertNotNull(createdEmployee);
        ReportingStructure createdEmployeeReportingStructure =  restTemplate.getForEntity(structureIdUrl, ReportingStructure.class, createdEmployee.getEmployeeId()).getBody();
        System.out.println("created employee: " + createdEmployeeReportingStructure.calculateReports(employeeRepository));

    }
}
