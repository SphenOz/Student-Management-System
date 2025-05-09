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
public class CourseController {
    
    @GetMapping("/courses")
    public List<Course> getCourses() {
        return DatabaseConnection.showCourses();
    }
    
    @PostMapping("/enroll")
    public void enrollStudent(@RequestBody Map<String, Object> payload){
        DatabaseConnection.enrollInCourse(
            (int)payload.get("studentId"), 
            (int)payload.get("courseId"), 
            (String)payload.get("semester"), 
            (int)payload.get("year"));
    }

    @GetMapping("/student-courses")
    public List<EnrollmentInfo> getMethodName(@RequestParam int studentID) {
        return DatabaseConnection.viewEnrolled(studentID);
    }
    

}
