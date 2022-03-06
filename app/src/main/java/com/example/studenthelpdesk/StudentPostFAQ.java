package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class StudentPostFAQ extends AppCompatActivity {

    EditText FAQ_content,hastag;
    CheckBox anonymous;
    AutoCompleteTextView concernedAdmin;
    Button postFAQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_post_faq);
        FAQ_content=findViewById(R.id.FAQ_content);
        concernedAdmin=findViewById(R.id.concerned_admin);
        hastag=findViewById(R.id.hashtags);
        anonymous=findViewById(R.id.anonymous);
        postFAQ=(Button) findViewById(R.id.post);
    }
}