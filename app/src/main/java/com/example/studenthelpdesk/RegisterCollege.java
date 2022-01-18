package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegisterCollege extends AppCompatActivity {
    //this instance will be referred to untill registration is completed
    static CollegeRegistrationData allData;
    EditText uname,cName,password,password2,adminmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college);
        allData=new CollegeRegistrationData();
        uname=findViewById(R.id.uname);
        cName=findViewById(R.id.cname);
        adminmail=findViewById(R.id.adminemail);
        password=findViewById(R.id.password2);
        password2=findViewById(R.id.confirmpassword2);

    }
    //move to next step of registration
    public void next(View v)
    {
        checkConstraints();
    }
    public Boolean checkConstraints()
    {
        //check if valid mail
        //check if passwords match
        //check if
        return true;
    }
}