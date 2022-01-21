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

public class RegisterCollege4_PersonalQuestions extends AppCompatActivity {
    LinearLayout linearLayout;
    CollegeRegistrationData allData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college4_personal_questions);
        linearLayout = findViewById(R.id.linearL);
        allData=RegisterCollege.allData;
        //These fields are required by the database so they are compulsary questions from the developer
        setCompulsaryQuestions("Course");
        setCompulsaryQuestions("Branch");
        setCompulsaryQuestions("Year");
        //all other questions can be added by admin
        addQuestion();
    }
    public void addQuestion(View v)
    {
        if(previusQuestionDone())
            addQuestion();
    }
    public  void saveAndNext(View v){

        int numberOfQuestions=linearLayout.getChildCount();
        CollegeRegisterQuestions allPersonalQuestion[]=new CollegeRegisterQuestions[numberOfQuestions];
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
            allPersonalQuestion[i]=thisQuestion;
        }
        allData.setQuestions_personal(allPersonalQuestion);
        Log.e("Hi", Arrays.toString(allPersonalQuestion));
        //intent to registration step 5
        startActivity(new Intent(RegisterCollege4_PersonalQuestions.this,RegisterCollege5_AcademicQuestions.class));
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
    public void setCompulsaryQuestions(String value)
    {
        View questionRepeatable=getLayoutInflater().inflate(R.layout.repeatable_college_register_questions,null);
        Spinner dropdown = questionRepeatable.findViewById(R.id.dropdown);
        String[] list={"Single line String"};
        ArrayAdapter spinnerList=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        dropdown.setAdapter(spinnerList);
        linearLayout.addView(questionRepeatable);
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
        CheckBox changeable=questionRepeatable.findViewById(R.id.changefield);
        changeable.setFocusable(false);
        changeable.setFocusableInTouchMode(false);
        changeable.setClickable(false);
    }
}