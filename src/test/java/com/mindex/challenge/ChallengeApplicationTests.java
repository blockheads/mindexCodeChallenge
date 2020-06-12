package com.mindex.challenge;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.impl.EmployeeServiceImplTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChallengeApplicationTests {

	private String employeeUrl;
	private String structureIdUrl;
	private String compensationUrl;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompensationRepository compensationRepository;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Before
	public void setup() {
		employeeUrl = "http://localhost:" + port + "/employee";
		structureIdUrl =  "http://localhost:" + port + "/reportingStructure/{id}";
		compensationUrl = "http://localhost:" + port + "/compensation";
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void Task1(){

		Employee employee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");

		// now let's get back a ReportingStructure
		ReportingStructure reportingStructure =  restTemplate.getForEntity(structureIdUrl, ReportingStructure.class, employee.getEmployeeId()).getBody();

		// now get reports
		assertEquals(4, reportingStructure.calculateReports(employeeRepository));


	}

	@Test
	public void Task2(){

		// getting our employee's compensation
		Employee employee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");

		// let us create a compensation for our employee
		Compensation compensation = new Compensation();
		compensation.setEmployee(employee);
		compensation.setSalary(10000);
		compensation.setEffectiveDate(new Date(1000));

		// Create checks
		Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();
		System.out.println(restTemplate.postForEntity(compensationUrl, compensation, Compensation.class));
		assertNotNull(createdCompensation);
		assertCompensation(compensation, createdCompensation);

	}

	public static void assertCompensation(Compensation compensation1, Compensation compensation2){
		assertEquals(compensation1.getEffectiveDate(), compensation2.getEffectiveDate());
		EmployeeServiceImplTest.assertEmployeeEquivalence(compensation1.getEmployee(), compensation2.getEmployee());
		assertEquals(compensation1.getSalary(), compensation2.getSalary());
	}


}
