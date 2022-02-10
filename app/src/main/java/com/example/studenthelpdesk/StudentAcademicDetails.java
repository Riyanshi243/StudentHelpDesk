package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentAcademicDetails extends AppCompatActivity {
    StudentData studentData;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_academic_details);
        ll=findViewById(R.id.ll);
        studentData=StudentPage.studentData;
        ArrayList<CollegeRegisterQuestions> quesAns = studentData.getAcademic_ques();
        for(CollegeRegisterQuestions a:quesAns)
        {
            View repeatAnswers=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
            TextView ques=repeatAnswers.findViewById(R.id.Ques);
            TextView ans=repeatAnswers.findViewById(R.id.ans);
            ques.setText(a.getQuestion());
            ans.setText(a.getAnswer());
            ll.addView(repeatAnswers);
        }
    }
}