package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdminSearchUser extends AppCompatActivity {
    StudentData studentData;
    CompanyData companyData;
    AdminData adminData;
    EditText email;
    Button search;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_user);
        email=findViewById(R.id.emailsearch);
        search=findViewById(R.id.search);
        ll=findViewById(R.id.ll);
    }
    public void searchUser(View v)
    {
        if(checkEmail())
        {
            String user=checkUserType();
            if(user.equalsIgnoreCase("admin"))
            {
                String name=adminData.getAdminName();
                View name_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                TextView ques_name=name_.findViewById(R.id.Ques);
                TextView ans_name=name_.findViewById(R.id.ans);
                ques_name.setText("NAME:");
                ans_name.setText(name);
                ll.addView(name_);

                String email=adminData.getEmail();
                View email_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                TextView ques_email=email_.findViewById(R.id.Ques);
                TextView ans_email=email_.findViewById(R.id.ans);
                ques_email.setText("EMAIL:");
                ans_email.setText(email);
                ll.addView(email_);

                String department=adminData.getDeptName();
                View dept_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                TextView ques_dept=dept_.findViewById(R.id.Ques);
                TextView ans_dept=dept_.findViewById(R.id.ans);
                ques_dept.setText("DEPARTMENT:");
                ans_dept.setText(department);
                ll.addView(dept_);

                String phone=adminData.getPhoneNumber();
                View phone_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                TextView ques_phone=phone_.findViewById(R.id.Ques);
                TextView ans_phone=phone_.findViewById(R.id.ans);
                ques_phone.setText("PHONE NUMBER:");
                ans_phone.setText(phone);
                ll.addView(phone_);
            }
            if(user.equalsIgnoreCase("student"))
            {
                ArrayList<CollegeRegisterQuestions> quesAns_personal = studentData.getPersonal_ques();
                for(CollegeRegisterQuestions a:quesAns_personal) {
                    View repeatAnswers = getLayoutInflater().inflate(R.layout.repeatable_student_details, null);
                    TextView ques = repeatAnswers.findViewById(R.id.Ques);
                    TextView ans = repeatAnswers.findViewById(R.id.ans);
                    ques.setText(a.getQuestion());
                    ans.setText(a.getAnswer());
                    ll.addView(repeatAnswers);
                }
                ArrayList<CollegeRegisterQuestions> quesAns_academic = studentData.getAcademic_ques();
                for(CollegeRegisterQuestions a:quesAns_academic) {
                    View repeatAnswers = getLayoutInflater().inflate(R.layout.repeatable_student_details, null);
                    TextView ques = repeatAnswers.findViewById(R.id.Ques);
                    TextView ans = repeatAnswers.findViewById(R.id.ans);
                    ques.setText(a.getQuestion());
                    ans.setText(a.getAnswer());
                    ll.addView(repeatAnswers);
                }
            }
            if(user.equalsIgnoreCase("company"))
            {

                String name=companyData.getCompanyName();
                View name_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                TextView ques_name=name_.findViewById(R.id.Ques);
                TextView ans_name=name_.findViewById(R.id.ans);
                ques_name.setText("COMPANY NAME:");
                ans_name.setText(name);
                ll.addView(name_);

                String location=companyData.getLocation();
                View location_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                TextView ques_location=location_.findViewById(R.id.Ques);
                TextView ans_location=location_.findViewById(R.id.ans);
                ques_location.setText("COMPANY LOCATION:");
                ans_location.setText(location);
                ll.addView(location_);

                String rname=companyData.getName();
                View rname_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                TextView ques_rname=rname_.findViewById(R.id.Ques);
                TextView ans_rname=rname_.findViewById(R.id.ans);
                ques_rname.setText("REPRESENTATIVE NAME:");
                ans_rname.setText(rname);
                ll.addView(rname_);

                String remail=companyData.getEmail();
                View remail_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                TextView ques_remail=remail_.findViewById(R.id.Ques);
                TextView ans_remail=remail_.findViewById(R.id.ans);
                ques_remail.setText("REPRESENTATIVE EMAIL:");
                ans_remail.setText(remail);
                ll.addView(remail_);

                String rphone=companyData.getPhone();
                View rphone_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                TextView ques_rphone=rphone_.findViewById(R.id.Ques);
                TextView ans_rphone=rphone_.findViewById(R.id.ans);
                ques_rphone.setText("REPRESENTATIVE PHONE NUMBER");
                ans_rphone.setText(rphone);
                ll.addView(rphone_);

            }
        }
    }

    public String checkUserType()
    {
        adminData=new AdminData();
        adminData.setAdminName("ABC");
        adminData.setEmail("ac@gm.com");
        return "admin";
    }

    public boolean checkEmail() {
        //check if not empty
        if (email.getText().toString().length() == 0) {
            email.setError("ENTER MAIL");
            return false;
        }
        //check if valid
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email1 = email.getText().toString().trim();
        if (!email1.matches(emailPattern)) {
            email.setError("ENTER VALID MAIL");
            return false;
        }
        return true;
    }
}