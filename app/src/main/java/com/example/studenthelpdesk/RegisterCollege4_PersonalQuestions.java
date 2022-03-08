package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

public class RegisterCollege4_PersonalQuestions extends AppCompatActivity {
    LinearLayout linearLayout;
    CollegeRegistrationData allData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college4_personal_questions);
        linearLayout = findViewById(R.id.linearL);
        allData=RegisterCollege.allData;
        setCompulsaryQuestions("Name",0);
        showData();
        //all other questions can be added by admin
        //addQuestion();
    }
    public void addQuestion(View v)
    {
        if(previusQuestionDone())
            addQuestion();
    }
    public  void saveAndNext(View v){

        int numberOfQuestions=linearLayout.getChildCount();
        CollegeRegisterQuestions allPersonalQuestion[]=new CollegeRegisterQuestions[numberOfQuestions];
        int c=0;
        for(int i=0;i<linearLayout.getChildCount();i++)
        {
            CollegeRegisterQuestions thisQuestion=new CollegeRegisterQuestions();
            View repeatableView=linearLayout.getChildAt(i);
            EditText question=repeatableView.findViewById(R.id.ans);
            CheckBox cumpolsary=repeatableView.findViewById(R.id.compulsoryfield);
            CheckBox changeable=repeatableView.findViewById(R.id.changefield);
            Spinner dropdown=repeatableView.findViewById(R.id.dropdown);
            if(question.getText().toString().length()==0)
                continue;
            thisQuestion.setQuestion(question.getText().toString());
            thisQuestion.setChangeable(changeable.isChecked());
            thisQuestion.setCompulsory(cumpolsary.isChecked());
            thisQuestion.setType(dropdown.getSelectedItemPosition());
            if(thisQuestion==null)
                continue;
            allPersonalQuestion[c++]=thisQuestion;
        }
        allData.setTotalPersonal(c);
        allData.setQuestions_personal(allPersonalQuestion);
        //intent to registration step 5
        startActivity(new Intent(RegisterCollege4_PersonalQuestions.this,RegisterCollege5_AcademicQuestions.class));
    }
    public boolean previusQuestionDone()
    {
        int numberOfQuestions=linearLayout.getChildCount();
        View repeatableLastView=linearLayout.getChildAt(numberOfQuestions-1);
        EditText question=repeatableLastView.findViewById(R.id.ans);
        question.requestFocus();
        if(question.getText().toString().length()==0){
            question.setError("ENTER THIS VALUE");
            question.requestFocus();
            return false;
        }
        return true;
    }
    public void addQuestion()
    {
        View questionRepeatable=getLayoutInflater().inflate(R.layout.repeatable_college_register_questions,null);
        Spinner dropdown = questionRepeatable.findViewById(R.id.dropdown);
        String[] list={"SingleLine String","Multiline String","Numerical Value","Numerical Decimal Value","Gender Choices","Date Picker"};
        ArrayAdapter spinnerList=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        dropdown.setAdapter(spinnerList);
        linearLayout.addView(questionRepeatable);
        ///0-string 1 line
        //1-string multiline
        // 2-numerical
        //3-numeric decimal
        //4-radio
        //5-date
        //6-upload
        //7-dropdown
    }
    public void setCompulsaryQuestions(String value,int i)
    {
        View questionRepeatable=getLayoutInflater().inflate(R.layout.repeatable_college_register_questions,null);
        Spinner dropdown = questionRepeatable.findViewById(R.id.dropdown);
        String[] list={"SingleLine String"};
        ArrayAdapter spinnerList=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        dropdown.setAdapter(spinnerList);
        dropdown.setSelection(i);
        linearLayout.addView(questionRepeatable);
        TextView questionhead=questionRepeatable.findViewById(R.id.Ques);
        String s=questionhead.getText()+" *";
        questionhead.setText(s);
        EditText question=questionRepeatable.findViewById(R.id.ans);
        question.setText(value);
        question.setFocusable(false);
        question.setFocusableInTouchMode(false);
        question.setClickable(false);
        CheckBox cumpolsary=questionRepeatable.findViewById(R.id.compulsoryfield);
        cumpolsary.setChecked(true);
        cumpolsary.setFocusable(false);
        cumpolsary.setFocusableInTouchMode(false);
        cumpolsary.setClickable(false);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder saveDetails=new AlertDialog.Builder(this);
        saveDetails.setTitle("ARE YOU SURE?");
        saveDetails.setMessage("All unsaved data will be lost.");
        saveDetails.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RegisterCollege4_PersonalQuestions.super.onBackPressed();
            }
        }).setNegativeButton("Save & Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                saveAndNext(new View(RegisterCollege4_PersonalQuestions.this));
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
        if(allData.getQuestions_personal()==null)
            return;
        CollegeRegisterQuestions[] t = allData.getQuestions_personal();
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
            String[] list={"SingleLine String","Multiline String","Numerical Value","Numerical Decimal Value","Gender Choices","Date Picker"};
            ArrayAdapter spinnerList=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
            dropdown.setAdapter(spinnerList);
            linearLayout.addView(questionRepeatable);

        }
    }

}