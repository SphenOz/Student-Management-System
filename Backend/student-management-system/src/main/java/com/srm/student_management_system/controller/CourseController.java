package com.srm.student_management_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import JDBC.DatabaseConnection;


@RestController
@RequestMapping("/")
public class CourseController {
    
    @GetMapping("/courses")
    public List<Course> getCourses() {
        return DatabaseConnection.showCourses();
    }

    @GetMapping("/courses/prof/{id}")
    public List<Course> getCoursesByProf(@PathVariable int id){
        return DatabaseConnection.viewClassesByProf(id);
    }

    @GetMapping("/course")
    public Course getCourseById(@RequestParam("courseID") Integer id){
        return DatabaseConnection.findCourse(id);
    }
    
    @PostMapping("/enroll")
    public void enrollStudent(@RequestBody Map<String, Object> payload){
        DatabaseConnection.enrollInCourse(
            (int)payload.get("studentId"), 
            (int)payload.get("courseId"));
    }

    @DeleteMapping("/drop")
    public void dropEnrollment(@RequestParam("studentID") int studentId, @RequestParam("courseID") int courseId){
        DatabaseConnection.dropFromCourse(studentId, courseId);
    }

    @PostMapping("/add-course")
    public void addCourse(@RequestBody Map<String, Object> payload){
        System.out.println("Adding course: " + payload.get("courseName"));
        System.out.println("Course code: " + payload.get("courseCode"));
        DatabaseConnection.createCourse(
            (String)payload.get("courseName"), 
            (String)payload.get("courseCode"),
            (int)payload.get("professorID"),
            (int)payload.get("credits"));
            
        System.out.println("Course added successfully!");
    }

    @GetMapping("/student-courses")
    public List<Grade> viewEnrolledCourses(@RequestParam("studentID") int studentID) {
        System.out.println("Student ID: " + studentID);
        System.out.println(DatabaseConnection.viewEnrolled(studentID).size());
        ArrayList<Grade> grades = new ArrayList<Grade>();
        for(EnrollmentInfo enrollment : DatabaseConnection.viewEnrolled(studentID)) {
            Grade g = new Grade(null, null, null);
            g.setStudent(DatabaseConnection.findStudent(enrollment.getStudentId()));
            g.setEnrollmentInfo(enrollment);
            g.setGrade(DatabaseConnection.getGrade(enrollment.getEnrollmentId()));
            grades.add(g);
        }
        return grades;
    }
    

}
