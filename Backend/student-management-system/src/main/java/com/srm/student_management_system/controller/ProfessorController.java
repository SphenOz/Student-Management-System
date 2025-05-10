package com.srm.student_management_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import JDBC.DatabaseConnection;



@RestController
@RequestMapping("/")
public class ProfessorController {

    @GetMapping("/professors")
    public Professor readProfessorById(@RequestParam Integer id){
        return DatabaseConnection.findProf(id);
    }

    @DeleteMapping("/professor")
    public void dropStudentByProf(@RequestParam("professorID") int profID, @RequestParam("studentID") int studentID, @RequestParam("courseID") int courseID){
        DatabaseConnection.dropStudentFromCourse(profID, studentID, courseID);
    }

    @GetMapping("/professor-courses")
    public List<Grade> viewEnrolledCourses(@RequestParam("courseID") int courseID) {
        ArrayList<Grade> grades = new ArrayList<Grade>();
        for(Student s : DatabaseConnection.viewStudentsByCourse(courseID)) {
            for(EnrollmentInfo enrollment : DatabaseConnection.viewEnrolled(s.getId())) {
                if(enrollment.getCourseId() == courseID) { 
                    Grade g = new Grade(null, null, null);
                    g.setStudent(DatabaseConnection.findStudent(enrollment.getStudentId()));
                    g.setEnrollmentInfo(enrollment);
                    g.setGrade(DatabaseConnection.getGrade(enrollment.getEnrollmentId()));
                    grades.add(g);
                }
                
                
            }
        }
        return grades;  
    }

    @PutMapping("/professor/grade")
    public void gradeStudent(@RequestParam("studentID") int studentID, @RequestParam("courseID") int courseID, @RequestParam("grade") String grade, @RequestParam("professorID") int professorID) {
        DatabaseConnection.updateGrade(professorID, studentID, courseID, grade);
    }
    
    

}
