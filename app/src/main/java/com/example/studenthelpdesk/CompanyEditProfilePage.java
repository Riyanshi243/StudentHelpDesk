package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CompanyEditProfilePage extends AppCompatActivity {
    CompanyData companyData;
    ImageView profilepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile_page);
        companyData=CompanyPage.companyData;
        profilepic=findViewById(R.id.profilepic);
        StorageReference storageRef= FirebaseStorage.getInstance().getReference(companyData.getCollegeId()).child("Photograph").child(companyData.getEmail());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(CompanyEditProfilePage.this)
                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.profile_pic)
                        .placeholder(R.drawable.profile_pic)
                        .into(profilepic);
            }
        });
    }
}