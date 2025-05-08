package com.srm.student_management_system;

public class Course {
    private int course_id;
    private String course_name;
    private String course_code;
    private int professor_id;
    private int credits;

    public int getCourseID() {
        return course_id;
    }

    public void setCourseID(int course_id) {
        this.course_id = course_id;
    }

    public String getCourseName() {
        return course_name;
    }

    public void setCourseName(String course_name) {
        this.course_name = course_name;
    }

    public String getCourseCode() {
        return course_code;
    }

    public void setCourseCode(String course_code) {
        this.course_code = course_code;
    }

    public int getProfessorID() {
        return professor_id;
    }

    public void setProfessorID(int professor_id) {
        this.professor_id = professor_id;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
