package com.nt.controller;

import com.nt.Exceptions.ResourceNotFoundException;
import com.nt.dto.EmployeeDTO;
import com.nt.entities.EmployeeEntity;
import com.nt.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable (name="employeeId")Long id){
       Optional<EmployeeDTO> employeeDTO= employeeService.getEmployeeById(id);
        return employeeDTO.map(employeeDTO1 -> ResponseEntity.ok(employeeDTO1))
                .orElseThrow(()->new ResourceNotFoundException("employee not found with Id "+ id));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(){
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee( @RequestBody EmployeeDTO inputEmployee){
        EmployeeDTO savedEmployee=employeeService.createEmployee(inputEmployee);
       return  new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PutMapping(path="/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@RequestBody EmployeeDTO employeeDTO , @PathVariable Long employeeId){
        return ResponseEntity.ok(employeeService.updateEmployeeById(employeeId, employeeDTO));
    }

    @DeleteMapping(path = "{employeeId}")
    public ResponseEntity<Boolean>deleteEmployeeById(@PathVariable Long employeeId){
        boolean getDeleted =employeeService.deleteEmployeeById(employeeId);
         if(getDeleted) return  ResponseEntity.ok(true);
         return ResponseEntity.notFound().build();

    }

    @PatchMapping(path = "{employeeId}")
    public ResponseEntity<EmployeeDTO> updatePartialEmployeeById(@RequestBody Map<String,Object> updates,
                                                          @PathVariable Long employeeId){
     EmployeeDTO employeeDTO = employeeService.updatePartialEmployeeById(employeeId,updates);
     if(employeeDTO ==null) return ResponseEntity.notFound().build();
     return  ResponseEntity.ok(employeeDTO);
    }

}
