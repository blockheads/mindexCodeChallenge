package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;
    private String employeeUrl;

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
        employeeUrl = "http://localhost:" + port + "/employee";
    }

    @Test
    public void testCreateReadUpdate(){

        // getting our employee's compensation
        //Employee employee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");

        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // we need to post our test employee
        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        // let us create a compensation for our employee
        Compensation compensation = new Compensation();
        compensation.setEmployee(createdEmployee);
        compensation.setSalary(10000);
        compensation.setEffectiveDate(new Date(1000));

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();
        // checking if this breaks unique creation for Compensation..
        restTemplate.postForEntity(compensationUrl, compensation, Compensation.class);
        assertNotNull(createdCompensation);
        assertCompensation(compensation, createdCompensation);

        // read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, compensation.getEmployee().getEmployeeId()).getBody();
        assertNotNull(readCompensation);
        assertEquals(readCompensation.getEffectiveDate(),compensation.getEffectiveDate());
        assertCompensation(compensation, readCompensation);

    }

    public static void assertCompensation(Compensation compensation1, Compensation compensation2){
        assertEquals(compensation1.getEffectiveDate(), compensation2.getEffectiveDate());
        EmployeeServiceImplTest.assertEmployeeEquivalence(compensation1.getEmployee(), compensation2.getEmployee());
        assertEquals(compensation1.getSalary(), compensation2.getSalary());
    }
}
