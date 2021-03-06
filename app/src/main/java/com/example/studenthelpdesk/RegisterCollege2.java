package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

//In this activity we ask all the department names of the college
public class RegisterCollege2 extends AppCompatActivity {
    final String deptQ="Enter Department Name",ansHint="Department name here";
    LinearLayout ll;
    int numberOfDept;
    ArrayList<String> departmentName;
    CollegeRegistrationData allData;
    private Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college2);
        ll=findViewById(R.id.linearL);

        allData=RegisterCollege.allData;
        if(allData.getDeptName()!=null)
            showData();

        button3= (Button) findViewById(R.id.login);
        View questionRepeatable = getLayoutInflater().inflate(R.layout.repeatable_edit_text_layout, null);
        TextView question = questionRepeatable.findViewById(R.id.Ques);
        EditText answer = questionRepeatable.findViewById(R.id.editTextTextMultiLine);
        answer.setHint(ansHint);
        question.setText(deptQ);

        ll.addView(questionRepeatable);
        numberOfDept=1;
    }
    //when click on floating plus button... this adds a question
    public void addQuestion(View v)
    {
        if(lastQuestionFilled()) {
            View questionRepeatable = getLayoutInflater().inflate(R.layout.repeatable_edit_text_layout, null);
            TextView question = questionRepeatable.findViewById(R.id.Ques);
            EditText answer = questionRepeatable.findViewById(R.id.editTextTextMultiLine);
            answer.setHint(ansHint);
            answer.requestFocus();
            question.setText(deptQ);
            ll.addView(questionRepeatable);
            numberOfDept++;
        }
    }
    public void saveAndNext(View v)
    {
        departmentName=new ArrayList<>();
        if(numberOfDept==1&&!lastQuestionFilled())
            return;
        for (int i = 0; i < ll.getChildCount(); i++) {
                View question1 = ll.getChildAt(i);
                EditText ans = question1.findViewById(R.id.editTextTextMultiLine);
                String dept = ans.getText().toString().trim();
                if(dept==null || dept.length()==0)
                    continue;
                if (i < departmentName.size()) {
                    departmentName.set(i, dept);
                } else {
                    departmentName.add(dept);
                }
        }
        if(departmentName.size()==0) {
            Toast.makeText(RegisterCollege2.this, "Enter atleast 1 department", Toast.LENGTH_LONG).show();
            return;
        }
        Collections.sort(departmentName,(o1, o2)->o1.compareTo(o2));
        allData.setDeptName(departmentName);
        //intent to registration step 3
        startActivity(new Intent(RegisterCollege2.this,RegisterCollege2_1.class));
    }
    public boolean lastQuestionFilled()
    {
        View question1 = ll.getChildAt(numberOfDept-1);
        EditText ans=question1.findViewById(R.id.editTextTextMultiLine);
        String dept=ans.getText().toString();
        if(dept.length()==0) {
            ans.setError("ENTER DATA HERE");
            ans.requestFocus();
            return false;
        }
        else
            return true;
    }
    public void showData()
    {
        ArrayList <String> deptTillNow=allData.getDeptName();
        for (String currDept:deptTillNow) {
            View questionRepeatable = getLayoutInflater().inflate(R.layout.repeatable_edit_text_layout, null);
            TextView question = questionRepeatable.findViewById(R.id.Ques);
            EditText answer = questionRepeatable.findViewById(R.id.editTextTextMultiLine);
            answer.setHint(ansHint);
            question.setText(deptQ);
            answer.setText(currDept);
            numberOfDept++;
            ll.addView(questionRepeatable);
        }
    }
    @Override
        public void onBackPressed() {
            AlertDialog.Builder saveDetails=new AlertDialog.Builder(this);
            saveDetails.setTitle("ARE YOU SURE?");
            saveDetails.setMessage("All unsaved data will be lost.");
            saveDetails.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RegisterCollege2.super.onBackPressed();
                }
            }).setNegativeButton("Save & Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing
                    saveAndNext(new View(RegisterCollege2.this));
                }
            }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //do nothing
                }
            });
            saveDetails.create().show();
        }
}
