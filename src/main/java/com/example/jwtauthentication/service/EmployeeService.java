package com.example.jwtauthentication.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jwtauthentication.model.Employee;

@Service
public class EmployeeService {

    private List<Employee> empList = new ArrayList<>();

    public EmployeeService() {
        empList.add(new Employee("1", "Dhrupa", "d@gmail.com"));
        empList.add(new Employee("2", "Krishna", "k12@gmail.com"));

    }

    public List<Employee> getEmployeesList() {
        return empList;
    }

}
