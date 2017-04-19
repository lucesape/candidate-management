package org.demo.controller;

import java.util.List;

import org.demo.model.Employee;
import org.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	@Autowired
	private EmployeeRepository repository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Employee> findEmployees() {
		return this.repository.findAll();
	}
}