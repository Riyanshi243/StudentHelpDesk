package com.example.studenthelpdesk;
//at the time of registration all the details of the college will be stored as an object of this class
public class CollegeRegistrationData {
    private String Uname,SAdminemail,password,CName;

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
