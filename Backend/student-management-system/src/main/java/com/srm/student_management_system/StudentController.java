package com.srm.student_management_system;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import java.util.List;

import JDBC.DatabaseConnection;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/")
    public class StudentController {

        @GetMapping
        public List<Student> readStudents(){
            return DatabaseConnection.readStudents();
        }

        @GetMapping("/student")
        public Student readStudentById(@RequestParam Integer id){
            return DatabaseConnection.findStudent(id);
        }

        @PostMapping("/students")
        public void addStudent(@RequestBody Map<String, String> payload){
            DatabaseConnection.createStudent(
                payload.get("firstName"), 
                payload.get("lastName"), 
                payload.get("email"), 
                payload.get("dob"), 
                payload.get("major")
            );
        }

        @PutMapping("/{id}/email")
        public void updateEmail(@PathVariable int id, @RequestBody Map<String, String> payload) {
            DatabaseConnection.updateStudentEmail(id, payload.get("email"));
        }

        @DeleteMapping("/students/{id}")
        public void deleteStudent(@PathVariable int id) {
            DatabaseConnection.deleteStudent(id);
        }
    

}