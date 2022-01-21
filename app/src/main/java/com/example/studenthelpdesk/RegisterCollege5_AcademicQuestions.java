package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.Arrays;

public class RegisterCollege5_AcademicQuestions extends AppCompatActivity {
    LinearLayout linearLayout;
    CollegeRegistrationData allData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college5_academic_questions);
        linearLayout = findViewById(R.id.linearL);
        allData=RegisterCollege.allData;
        //all academinc questions can be added by admin
        addQuestion();
    }
    public void addQuestion(View v)
    {
        if(previusQuestionDone())
            addQuestion();
    }
    public  void saveAndNext(View v){

        int numberOfQuestions=linearLayout.getChildCount();
        CollegeRegisterQuestions allAcademicQuestion[]=new CollegeRegisterQuestions[numberOfQuestions];
        for(int i=0;i<numberOfQuestions;i++)
        {
            CollegeRegisterQuestions thisQuestion=new CollegeRegisterQuestions();
            View repeatableView=linearLayout.getChildAt(i);
            EditText question=repeatableView.findViewById(R.id.ans);
            CheckBox cumpolsary=repeatableView.findViewById(R.id.compulsoryfield);
            CheckBox changeable=repeatableView.findViewById(R.id.changefield);
            Spinner dropdown=repeatableView.findViewById(R.id.dropdown);
            thisQuestion.setQuestion(question.getText().toString());
            thisQuestion.setChangeable(changeable.isChecked());
            thisQuestion.setCompulsory(cumpolsary.isChecked());
            thisQuestion.setType(dropdown.getSelectedItemPosition());
            allAcademicQuestion[i]=thisQuestion;
        }
        allData.setQuestions_academic(allAcademicQuestion);
        startActivity(new Intent(RegisterCollege5_AcademicQuestions.this,RegisterCollege6_UploadData.class));
    }
    public boolean previusQuestionDone()
    {
        int numberOfQuestions=linearLayout.getChildCount();
        View repeatableLastView=linearLayout.getChildAt(numberOfQuestions-1);
        EditText question=repeatableLastView.findViewById(R.id.ans);
        if(question.getText().toString().length()==0){
            question.setError("Enter this value");
            return false;
        }
        return true;
    }
    public void addQuestion()
    {
        View questionRepeatable=getLayoutInflater().inflate(R.layout.repeatable_college_register_questions,null);
        Spinner dropdown = questionRepeatable.findViewById(R.id.dropdown);
        String[] list={"Single line String","Multiline String","Numerical Value","Numerical Decimal","Gender Choices","Date"};
        ArrayAdapter spinnerList=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        dropdown.setAdapter(spinnerList);
        linearLayout.addView(questionRepeatable);
        //0-nuemerical
        //1-neumeric decimal
        //2-string 1 line
        //3-string multiline
        //4-radio
        //5-date
    }
}