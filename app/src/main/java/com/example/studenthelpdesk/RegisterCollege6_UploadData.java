package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterCollege6_UploadData extends AppCompatActivity {
    LinearLayout linearLayout;
    CollegeRegistrationData allData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college6_upload_data);
        linearLayout = findViewById(R.id.linearL);
        allData = RegisterCollege.allData;
        setCompulsaryQuestions("Photograph");
        showData();
        //addQuestion();
    }

    public void addQuestion(View v) {
        if (previusQuestionDone())
            addQuestion();
    }

    public void saveAndNext(View v) {
        int numberOfQuestions = linearLayout.getChildCount();
        int c1=0;
        CollegeRegisterQuestions allUploadQuestion[] = new CollegeRegisterQuestions[numberOfQuestions];
        for (int i = 0; i < numberOfQuestions; i++) {
            CollegeRegisterQuestions thisQuestion = new CollegeRegisterQuestions();
            View repeatableView = linearLayout.getChildAt(i);
            EditText question = repeatableView.findViewById(R.id.ans);
            question.requestFocus();
            CheckBox cumpolsary = repeatableView.findViewById(R.id.compulsoryfield);
            if(question.getText().toString().length()==0)
                continue;
            thisQuestion.setQuestion(question.getText().toString());
            thisQuestion.setChangeable(true);
            thisQuestion.setCompulsory(cumpolsary.isChecked());
            thisQuestion.setType(6);
            allUploadQuestion[c1++] = thisQuestion;
        }
        allData.setTotalUpload(c1);
        allData.setQuestions_upload(allUploadQuestion);
        //intent to registration step 7
        startActivity(new Intent(RegisterCollege6_UploadData.this, RegisterCollege7_SuperAdminSignup.class));
    }

    public boolean previusQuestionDone() {
        int numberOfQuestions = linearLayout.getChildCount();
        View repeatableLastView = linearLayout.getChildAt(numberOfQuestions - 1);
        EditText question = repeatableLastView.findViewById(R.id.ans);
        if (question.getText().toString().length() == 0) {
            question.setError("ENTER THIS VALUE");
            question.requestFocus();
            return false;
        }
        return true;
    }

    public void addQuestion() {
        View questionRepeatable = getLayoutInflater().inflate(R.layout.repeatable_college_register_questions, null);
        Spinner dropdown = questionRepeatable.findViewById(R.id.dropdown);
        String[] list = {"Upload Data"};
        ArrayAdapter spinnerList = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        dropdown.setAdapter(spinnerList);
        CheckBox changeable = questionRepeatable.findViewById(R.id.changefield);
        changeable.setVisibility(View.GONE);
        linearLayout.addView(questionRepeatable);

    }

    public void setCompulsaryQuestions(String value) {
        View questionRepeatable = getLayoutInflater().inflate(R.layout.repeatable_college_register_questions, null);
        Spinner dropdown = questionRepeatable.findViewById(R.id.dropdown);
        String[] list = {"Upload Data"};
        ArrayAdapter spinnerList = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        dropdown.setAdapter(spinnerList);
        linearLayout.addView(questionRepeatable);
        TextView questionhead = questionRepeatable.findViewById(R.id.Ques);
        String s = questionhead.getText() + " *";
        questionhead.setText(s);
        EditText question = questionRepeatable.findViewById(R.id.ans);
        question.setText(value);
        question.setFocusable(false);
        question.setFocusableInTouchMode(false);
        question.setClickable(false);
        CheckBox cumpolsary = questionRepeatable.findViewById(R.id.compulsoryfield);
        cumpolsary.setChecked(true);
        cumpolsary.setFocusable(false);
        cumpolsary.setFocusableInTouchMode(false);
        cumpolsary.setClickable(false);
        CheckBox changeable = questionRepeatable.findViewById(R.id.changefield);
        changeable.setVisibility(View.GONE);
        changeable.setChecked(true);
        changeable.setFocusable(false);
        changeable.setFocusableInTouchMode(false);
        changeable.setClickable(false);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder saveDetails=new AlertDialog.Builder(this);
        saveDetails.setTitle("ARE YOU SURE?");
        saveDetails.setMessage("All unsaved data will be lost.");
        saveDetails.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RegisterCollege6_UploadData.super.onBackPressed();
            }
        }).setNegativeButton("Save & Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                saveAndNext(new View(RegisterCollege6_UploadData.this));
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        saveDetails.create().show();
    }
    void showData()
    {
        if(allData.getQuestions_upload()==null)
            return;
        CollegeRegisterQuestions[] t = allData.getQuestions_upload();
        for(int i=1;i<t.length;i++)
        {
            CollegeRegisterQuestions currQ=t[i];
            if(currQ==null)
            {
                continue;
            }
            View questionRepeatable=getLayoutInflater().inflate(R.layout.repeatable_college_register_questions,null);
            Spinner dropdown = questionRepeatable.findViewById(R.id.dropdown);
            EditText ques=questionRepeatable.findViewById(R.id.ans);
            CheckBox compulsory =questionRepeatable.findViewById(R.id.compulsoryfield);
            CheckBox editable =questionRepeatable.findViewById(R.id.changefield);
            ques.setText(currQ.getQuestion());
            if(currQ.isChangeable())
                editable.setChecked(true);
            if(currQ.isCumplolsory())
                compulsory.setChecked(true);
            String[] list={"Upload"};
            ArrayAdapter spinnerList=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
            dropdown.setAdapter(spinnerList);
            linearLayout.addView(questionRepeatable);
        }
    }
}