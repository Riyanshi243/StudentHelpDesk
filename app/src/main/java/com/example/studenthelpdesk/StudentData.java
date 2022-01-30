package com.example.studenthelpdesk;

public class StudentData {
    private String email,password,collegeid,course=" ",branch=" ",name;
    private String yr=" ";
    private String p_ques[],p_id[],p_ans[],a_ques[],a_id[],a_ans[];

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setA_id(String[] a_id) {
        this.a_id = a_id;
    }

    public void setA_ans(String[] a_ans) {
        this.a_ans = a_ans;
    }

    public void setA_ques(String[] a_ques) {
        this.a_ques = a_ques;
    }

    public void setP_ans(String[] p_ans) {
        this.p_ans = p_ans;
    }

    public void setP_id(String[] p_id) {
        this.p_id = p_id;
    }

    public void setP_ques(String[] p_ques) {
        this.p_ques = p_ques;
    }

    public String[] getA_ans() {
        return a_ans;
    }

    public String[] getA_id() {
        return a_id;
    }

    public String[] getA_ques() {
        return a_ques;
    }

    public String[] getP_id() {
        return p_id;
    }

    public String[] getP_ans() {
        return p_ans;
    }

    public String[] getP_ques() {
        return p_ques;
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

    public void setCollegeid(String collegeid) {
        this.collegeid = collegeid;
    }

    public String getCollegeid() {
        return collegeid;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setYr(String yr) {
        this.yr = yr;
    }

    public String getBranch() {
        return branch;
    }

    public String getYr() {
        return yr;
    }

    public String getCourse() {
        return course;
    }
}
