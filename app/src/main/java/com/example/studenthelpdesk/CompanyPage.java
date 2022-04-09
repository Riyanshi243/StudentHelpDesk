package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.HashSet;

public class CompanyPage extends AppCompatActivity {
    static CompanyData companyData;
    FirebaseAuth f;
    ImageView profilepic;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_page);
        f=FirebaseAuth.getInstance();
        companyData=new CompanyData();
        name=findViewById(R.id.name);
        profilepic=findViewById(R.id.profilepic);
        HashSet<String > token=new HashSet<>();
        FirebaseMessaging.getInstance().subscribeToTopic("All");
        FirebaseMessaging.getInstance().subscribeToTopic(f.getCurrentUser().getEmail());
        companyData.setEmail(f.getCurrentUser().getEmail());
        token.add("All");
        token.add(f.getCurrentUser().getEmail());
        DocumentReference userInfo = FirebaseFirestore.getInstance().collection("All Users On App").document(f.getCurrentUser().getEmail());
        userInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String cId= (String) documentSnapshot.get("College");
                companyData.setCollegeId(cId);
                DocumentReference companyInfo=FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("UsersInfo").document(f.getCurrentUser().getEmail());
                companyInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        FirebaseMessaging.getInstance().subscribeToTopic(cId);
                        FirebaseMessaging.getInstance().subscribeToTopic("Company_"+cId);
                        token.add(cId);
                        token.add("Company_"+cId);
                        companyData.setToken(token);

                        companyData.setPersonalEmail((String) documentSnapshot.get("Personal Email"));
                        companyData.setLocation((String) documentSnapshot.get("Company Location"));
                        companyData.setCompanyName((String) documentSnapshot.get("Company Name"));
                        companyData.setName((String) documentSnapshot.get("Name"));
                        companyData.setPhone((String) documentSnapshot.get("Phone Number"));
                        name.setText(companyData.getCompanyName());
                        StorageReference storageRef= FirebaseStorage.getInstance().getReference(companyData.getCollegeId()).child("Photograph").child(companyData.getEmail());
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(CompanyPage.this)
                                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.drawable.company_profile_img)
                                        .placeholder(R.drawable.default_loading_img)
                                        .into(profilepic);
                            }
                        });


                    }
                });
            }
        });

    }


    public void sendNotification(View v){
        startActivity(new Intent(CompanyPage.this, AdminOrCompanySendNotification.class));
    }
    public void viewNotifications(View v){
        startActivity(new Intent(CompanyPage.this, ViewNotificationsByAll.class));
    }
    public void editProfile(View v){
        startActivity(new Intent(CompanyPage.this, CompanyEditProfilePage.class));
    }
    public void logout(View v)
    {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(companyData.getCollegeId());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(f.getCurrentUser().getEmail());
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Company_"+companyData.getCollegeId());
        f.signOut();
        Toast.makeText(this,"Logged Out",Toast.LENGTH_LONG).show();
       // companyData=null;
        startActivity(new Intent(CompanyPage.this,Login.class));
        finish();
    }
    public void help(View v)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("Developer Folder").child("Company Help.pdf");
        Task<Uri> helpDoc = storageRef.getDownloadUrl();
        helpDoc.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Intent intent = new Intent(CompanyPage.this, ViewPDFActivity.class);
                intent.putExtra("url", uri.toString());
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CompanyPage.this,e.getMessage().toString(),Toast.LENGTH_LONG);

            }
        });

    }
    public void requestData(View v)
    {
        startActivity(new Intent(CompanyPage.this,CompanyRequestDataFromAdmin.class));
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(CompanyPage.this,EndScreen.class));
    }
}