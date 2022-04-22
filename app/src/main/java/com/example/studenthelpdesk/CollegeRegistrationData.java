package com.example.studenthelpdesk;

import java.util.ArrayList;

//at the time of registration all the details of the college will be stored as an object of this class
public class CollegeRegistrationData {
    private String Uname,SAdminemail,password,CName,Location,adminName,adminNo,adminDept;
    private ArrayList<String> deptName,courseName,dataDept;
    private ArrayList<ArrayList<String>> branchForEachCourse,depForEachCourse;

    int totalPersonal,totalAcademic,totalUpload;

    public int getTotalPersonal() {
        return totalPersonal;
    }

    public void setTotalPersonal(int totalPersonal) {
        this.totalPersonal = totalPersonal;
    }

    public int getTotalAcademic() {
        return totalAcademic;
    }

    public void setTotalAcademic(int totalAcademic) {
        this.totalAcademic = totalAcademic;
    }

    public int getTotalUpload() {
        return totalUpload;
    }

    public void setTotalUpload(int totalUpload) {
        this.totalUpload = totalUpload;
    }

    private CollegeRegisterQuestions questions_personal[],questions_academic[],questions_upload[];
    public void setDepForEachCourse(ArrayList<ArrayList<String>> depForEachCourse) {
        this.depForEachCourse = depForEachCourse;
    }

    public void setDataDept(ArrayList<String> dataDept) {
        this.dataDept = dataDept;
    }

    public ArrayList<String> getDataDept() {
        return dataDept;
    }

    public ArrayList<ArrayList<String>> getDepForEachCourse() {
        return depForEachCourse;
    }
    public void setAdminDept(String adminDept) {
        this.adminDept = adminDept;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void setAdminNo(String adminNo) {
        this.adminNo = adminNo;
    }

    public String getAdminDept() {
        return adminDept;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminNo() {
        return adminNo;
    }

    public void setQuestions_academic(CollegeRegisterQuestions[] questions_academic) {
        this.questions_academic = questions_academic;
    }

    public void setQuestions_personal(CollegeRegisterQuestions[] questions_personal) {
        this.questions_personal = questions_personal;
    }

    public void setQuestions_upload(CollegeRegisterQuestions[] questions_upload) {
        this.questions_upload = questions_upload;
    }

    public CollegeRegisterQuestions[] getQuestions_academic() {
        return questions_academic;
    }

    public CollegeRegisterQuestions[] getQuestions_personal() {
        return questions_personal;
    }

    public CollegeRegisterQuestions[] getQuestions_upload() {
        return questions_upload;
    }

    public void setBranchForEachCourse(ArrayList<ArrayList<String>> branchForEachCourse) {
        this.branchForEachCourse = branchForEachCourse;
    }

    public void setCourseName(ArrayList<String> courseName) {
        this.courseName = courseName;
    }

    public ArrayList<ArrayList<String>> getBranchForEachCourse() {
        return branchForEachCourse;
    }

    public ArrayList<String> getCourseName() {
        return courseName;
    }

    public void setLocation(String location) {
        Location = location;
    }
    public ArrayList<String> getDeptName() {
        return deptName;
    }
    public String getLocation() {
        return Location;
    }
    public void setDeptName(ArrayList<String> deptName) {
        this.deptName = deptName;
    }
    public String getUname() {
        return Uname;
    }

    public String getCName() {
        return CName;
    }

    public String getPassword() {
        return password;
    }

    public String getSAdminemail() {
        return SAdminemail;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSAdminemail(String SAdminemail) {
        this.SAdminemail = SAdminemail;
    }

    public void setUname(String uname) {
        Uname = uname;
    }

}
