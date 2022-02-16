package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentPersonalDetails extends AppCompatActivity {
    StudentData studentData;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_personal_details);
        ll=findViewById(R.id.ll);
        studentData=StudentPage.studentData;
        String s="";
        ArrayList<CollegeRegisterQuestions> quesAns = studentData.getPersonal_ques();
        for(CollegeRegisterQuestions a:quesAns)
        {
            View repeatAnswers=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
            TextView ques=repeatAnswers.findViewById(R.id.Ques);
            TextView ans=repeatAnswers.findViewById(R.id.ans);
            ques.setText(a.getQuestion());
            ans.setText(a.getAnswer());
            ans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (a.isChangeable() == true) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(StudentPersonalDetails.this);
                            builder.setTitle(a.getQuestion());
                            builder.setMessage("Enter new value");
                            EditText neww=new EditText(repeatAnswers.getContext());
                            builder.setView(neww);
                            //EditText neww = new EditText(StudentPersonalDetails.this);
                            neww.setText(a.getAnswer());
                            builder.setCancelable(false)
                                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ans.setText(neww.getText().toString());
                                            //save in database
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //do nothing
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                    } else {
                        // Toast.makeText(this,"SEND REQUEST FOR CHANGE",Toast.LENGTH_LONG).show();
                    }
                }
            });
            ll.addView(repeatAnswers);
        }
    }
}