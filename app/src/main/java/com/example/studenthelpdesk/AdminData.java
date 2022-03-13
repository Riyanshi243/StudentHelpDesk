package com.example.studenthelpdesk;

import java.util.HashSet;

public class AdminData {
    private String email,password,collegeId,adminName,phoneNumber,deptName;
    HashSet<String> token=new HashSet<>();


    public void setToken(HashSet<String> token) {
        this.token = token;
    }

    public HashSet<String> getToken() {
        return token;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

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
