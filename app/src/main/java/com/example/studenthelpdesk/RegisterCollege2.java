package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        button3= (Button) findViewById(R.id.login);
        View questionRepeatable = getLayoutInflater().inflate(R.layout.repeatable_edit_text_layout, null);
        TextView question = questionRepeatable.findViewById(R.id.Ques);
        EditText answer = questionRepeatable.findViewById(R.id.editTextTextMultiLine);
        answer.setHint(ansHint);
        question.setText(deptQ);
        departmentName=new ArrayList<>();
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
        for (int i = 0; i < numberOfDept; i++) {
                View question1 = ll.getChildAt(i);
                EditText ans = question1.findViewById(R.id.editTextTextMultiLine);
                String dept = ans.getText().toString();
                if(dept==null || dept.length()==0)
                    continue;
                if (i < departmentName.size()) {
                    departmentName.set(i, dept);
                } else {
                    departmentName.add(dept);
                }
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
            return false;
        }
        else
            return true;
    }
}