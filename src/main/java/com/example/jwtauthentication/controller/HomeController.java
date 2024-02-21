package com.example.jwtauthentication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtauthentication.model.Employee;
import com.example.jwtauthentication.service.EmployeeService;

@RestController
@RequestMapping("/welcome")
public class HomeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getEmployeesList() {
        return employeeService.getEmployeesList();
    }
}
