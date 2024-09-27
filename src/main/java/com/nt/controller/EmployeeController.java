package com.nt.controller;

import com.nt.dto.EmployeeDTO;
import com.nt.entities.EmployeeEntity;
import com.nt.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/{employeeId}")
    public EmployeeDTO getEmployeeById(@PathVariable (name="employeeId")Long id){
        return employeeService.getEmployeeById(id);
    }

    @GetMapping
    public List<EmployeeDTO> getAllEmployees(){
       return employeeService.getAllEmployees();
    }

    @PostMapping
    public EmployeeDTO createEmployee( @RequestBody EmployeeDTO inputEmployee){
       return  employeeService.createEmployee(inputEmployee);
    }

}
