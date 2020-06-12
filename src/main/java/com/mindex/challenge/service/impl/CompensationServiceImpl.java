package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating Compensation [{}]", compensation);

        // error handling non unique creation. could throw exception..
        if(compensation.getEmployee() == null || compensationRepository.findByEmployee_EmployeeId(compensation.getEmployee().getEmployeeId()) != null)
            return null;

        compensationRepository.insert(compensation);
        return compensation;
    }

    @Override
    public Compensation read(Employee employee) {
        LOG.debug("Creating Compensation with employee [{}]", employee);

        Compensation compensation = compensationRepository.findByEmployee_EmployeeId(employee.getEmployeeId());

        if (compensation == null) {
            throw new RuntimeException("Invalid Employee: " + employee);
        }

        return compensation;
    }
}
