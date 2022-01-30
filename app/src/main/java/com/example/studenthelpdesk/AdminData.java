package com.example.studenthelpdesk;

public class AdminData {
    private String email,password,collegeId;

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
