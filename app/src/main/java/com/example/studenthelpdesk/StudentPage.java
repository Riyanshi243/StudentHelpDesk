package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class StudentPage extends AppCompatActivity {
    FirebaseAuth f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);
        f=FirebaseAuth.getInstance();

    }

    public void personalDetails(View v){
        startActivity(new Intent(StudentPage.this, StudentPersonalDetails.class));
    }
    public void academicDetails(View v){
        startActivity(new Intent(StudentPage.this, StudentAcademicDetails.class));
    }
    public void uploadedDetails(View v){
        startActivity(new Intent(StudentPage.this, StudentUploadDetails.class));
    }
    public void requestStatus(View v){
        startActivity(new Intent(StudentPage.this, StudentCheckRequestStatus.class));
    }
    public void viewNotifications(View v){
        startActivity(new Intent(StudentPage.this, StudentViewNotifications.class));
    }
    public void viewFaq(View v){
        startActivity(new Intent(StudentPage.this, StudentViewAllFAQ.class));
    }
    public void logout(View v)
    {
        f.signOut();
        Toast.makeText(this,"Logged Out",Toast.LENGTH_LONG).show();
        startActivity(new Intent(StudentPage.this,Login.class));
        finish();
    }
}