package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CompanyPage extends AppCompatActivity {

    FirebaseAuth f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_page);
        f=FirebaseAuth.getInstance();

    }
    public void logout(View v)
    {
        f.signOut();
        Toast.makeText(this,"Logged Out",Toast.LENGTH_LONG).show();
        startActivity(new Intent(CompanyPage.this,Login.class));
        finish();
    }
}