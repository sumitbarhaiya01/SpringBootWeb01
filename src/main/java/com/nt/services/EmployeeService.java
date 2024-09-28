package com.nt.services;

import com.nt.Exceptions.ResourceNotFoundException;
import com.nt.dto.EmployeeDTO;
import com.nt.entities.EmployeeEntity;
import com.nt.repositories.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private  final EmployeeRepository employeeRepository;
    private  final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public Optional<EmployeeDTO> getEmployeeById(Long id){
        Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(id);
        return employeeEntity.map(employeeEntity1 -> modelMapper.map(employeeEntity1,EmployeeDTO.class));

    }

    public List<EmployeeDTO> getAllEmployees() {
       List<EmployeeEntity> employeeEntities=  employeeRepository.findAll();
        return employeeEntities
               .stream()
               .map(employeeEntity -> modelMapper.map(employeeEntity,EmployeeDTO.class))
               .collect(Collectors.toList());

    }

    public EmployeeDTO createEmployee(EmployeeDTO inputEmployee) {
       EmployeeEntity toSaveEntity=modelMapper.map(inputEmployee,EmployeeEntity.class);
       EmployeeEntity employeeEntity = employeeRepository.save(toSaveEntity);
       return modelMapper.map(employeeEntity, EmployeeDTO.class);
    }

    public EmployeeDTO updateEmployeeById(Long employeeId, EmployeeDTO employeeDTO) {
        isExistsByEmployeeId(employeeId);
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        employeeEntity.setId(employeeId);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);


    }

    public void isExistsByEmployeeId(Long employeeId){
        Boolean exist=employeeRepository.existsById(employeeId);
        if(!exist) throw  new ResourceNotFoundException( "employee not fount with Id"+ employeeId);

    }

    public boolean deleteEmployeeById(Long employeeId) {
       isExistsByEmployeeId(employeeId);
       employeeRepository.deleteById(employeeId);
       return true;
    }

    public EmployeeDTO updatePartialEmployeeById(Long employeeId, Map<String, Object> updates) {
        isExistsByEmployeeId(employeeId);
        EmployeeEntity employeeEntity=employeeRepository.findById(employeeId).get();
        updates.forEach((field,value)-> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(EmployeeEntity.class,field);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, employeeEntity, value);
        });
        return modelMapper.map(employeeRepository.save(employeeEntity),EmployeeDTO.class);

    }
}
