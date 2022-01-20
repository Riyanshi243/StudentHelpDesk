package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class RegisterCollege7 extends AppCompatActivity {
    CollegeRegistrationData allData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college7);
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
        ab.setTitle("DATA UPLOADING");
        ProgressBar progressBar=new ProgressBar(this);
        progressBar.setVisibility(View.VISIBLE);
        ab.setView(progressBar);
        ab.create().show();
    }
}