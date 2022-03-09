package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StudentViewAllFAQ extends AppCompatActivity {
    FloatingActionButton addCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_all_faq);
        addCourse=(FloatingActionButton) findViewById(R.id.addCourse);
    }
    public void onClick(View v)
    {
    startActivity(new Intent(StudentViewAllFAQ.this, StudentPostFAQ.class));
    }
}