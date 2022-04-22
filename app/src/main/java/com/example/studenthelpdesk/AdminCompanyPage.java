package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdminCompanyPage extends AppCompatActivity {
    static AdminData adminData;
    TextView name;
    ImageView profilepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_company_page);
        name=findViewById(R.id.name);
        profilepic=findViewById(R.id.profilePic);

    }
    public void viewListOfCompanies(View v){
        startActivity(new Intent(AdminCompanyPage.this, AdminViewListOfCompanies.class));
    }
    public void viewDataRequestsFromCompany(View v){
        startActivity(new Intent(AdminCompanyPage.this, AdminViewDataRequestsFromCompany.class));
    }
}