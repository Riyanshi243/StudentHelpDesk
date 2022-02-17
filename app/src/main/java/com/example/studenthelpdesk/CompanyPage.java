package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

public class CompanyPage extends AppCompatActivity {
    static CompanyData companyData;
    FirebaseAuth f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_page);
        f=FirebaseAuth.getInstance();
        companyData=new CompanyData();
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        FirebaseMessaging.getInstance().subscribeToTopic(f.getCurrentUser().getEmail());
        //FirebaseMessaging.getInstance().subscribeToTopic(cId);
        //FirebaseMessaging.getInstance().subscribeToTopic("company_"+cId);

    }


    public void sendNotification(View v){
        startActivity(new Intent(CompanyPage.this, AdminOrCompanySendNotification.class));
    }
    public void editProfile(View v){
        startActivity(new Intent(CompanyPage.this, CompanyEditProfilePage.class));
    }
    public void logout(View v)
    {
        f.signOut();
        Toast.makeText(this,"Logged Out",Toast.LENGTH_LONG).show();
        startActivity(new Intent(CompanyPage.this,Login.class));
        finish();
    }
}