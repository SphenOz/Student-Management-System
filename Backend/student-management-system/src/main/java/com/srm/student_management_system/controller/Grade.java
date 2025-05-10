package com.srm.student_management_system.controller;

public class Grade {
    private EnrollmentInfo enrollmentInfo;
    private Student student;
    private String grade; // A, B, C, D, F, etc.

    public EnrollmentInfo getEnrollmentInfo() {
        return enrollmentInfo;
    }
    public void setEnrollmentInfo(EnrollmentInfo enrollmentInfo) {
        this.enrollmentInfo = enrollmentInfo;
    }
    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public Grade(EnrollmentInfo enrollmentInfo, Student student, String grade) {
        this.enrollmentInfo = enrollmentInfo;
        this.student = student;
        this.grade = grade;
    }
}
