package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

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
        addQuestion();
    }

    public void addQuestion(View v) {
        if (previusQuestionDone())
            addQuestion();
    }

    public void saveAndNext(View v) {
        int numberOfQuestions = linearLayout.getChildCount();
        CollegeRegisterQuestions allUploadQuestion[] = new CollegeRegisterQuestions[numberOfQuestions];
        for (int i = 0; i < numberOfQuestions; i++) {
            CollegeRegisterQuestions thisQuestion = new CollegeRegisterQuestions();
            View repeatableView = linearLayout.getChildAt(i);
            EditText question = repeatableView.findViewById(R.id.ans);
            CheckBox cumpolsary = repeatableView.findViewById(R.id.compulsoryfield);
            CheckBox changeable = repeatableView.findViewById(R.id.changefield);
            thisQuestion.setQuestion(question.getText().toString());
            thisQuestion.setChangeable(changeable.isChecked());
            thisQuestion.setCompulsory(cumpolsary.isChecked());
            thisQuestion.setType(6);
            allUploadQuestion[i] = thisQuestion;
        }
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
        changeable.setChecked(true);
        changeable.setFocusable(false);
        changeable.setFocusableInTouchMode(false);
        changeable.setClickable(false);
    }


}