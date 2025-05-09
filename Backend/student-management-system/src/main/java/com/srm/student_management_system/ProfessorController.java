package com.srm.student_management_system;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import JDBC.DatabaseConnection;



@RestController
@RequestMapping("/")
public class ProfessorController {

    @GetMapping("/professor/{id}")
    public Professor readProfessorById(@PathVariable int id){
        return DatabaseConnection.findProf(id);
    }

    @DeleteMapping("/professor")
    public void dropStudentByProf(@RequestParam int profId, @RequestParam int studentId, @RequestParam int courseId){
        DatabaseConnection.dropStudentFromCourse(profId, studentId, courseId);
    }
    
    

}
