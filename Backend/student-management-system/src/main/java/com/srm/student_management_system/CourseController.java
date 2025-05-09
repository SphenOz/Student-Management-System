package com.srm.student_management_system;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;
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
    
    @PostMapping("/enroll")
    public void enrollStudent(@RequestBody Map<String, Object> payload){
        DatabaseConnection.enrollInCourse(
            (int)payload.get("studentId"), 
            (int)payload.get("courseId"));
    }

    @DeleteMapping("/drop")
    public void dropEnrollment(@RequestParam int studentId, @RequestParam int courseId){
        DatabaseConnection.dropFromCourse(studentId, courseId);
    }

    @GetMapping("/student-courses")
    public List<EnrollmentInfo> viewEnrolledCourses(@RequestParam int studentID) {
        return DatabaseConnection.viewEnrolled(studentID);
    }
    

}
