package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class AdminPage extends AppCompatActivity {
    static AdminData adminData;
     FirebaseAuth f;
     TextView greetings;
     ImageView profilepic;
     LinearLayout editReq, lockDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        String s="/topics/student1@banasthali.in";
        s.replaceAll(".","_");
        FcmNotificationsSender notificationsSender=new FcmNotificationsSender(s,"Request Rejected","currReq.getQuestion()"+" remains ",AdminPage.this,"Request");
        notificationsSender.SendNotifications();
        adminData=new AdminData();
        greetings=findViewById(R.id.name);
        profilepic=findViewById(R.id.profilePic);
        editReq=findViewById(R.id.editReq);
        lockDatabase=findViewById(R.id.lockDatabase);
        f=FirebaseAuth.getInstance();
        if(f==null)
        {
            startActivity(new Intent(AdminPage.this,Login.class));
            finishAffinity();
        }
        adminData.setEmail(f.getCurrentUser().getEmail());
        FirebaseFirestore fs=FirebaseFirestore.getInstance();
        HashSet<String> token=new HashSet<>();
        token.add("All");
        token.add(f.getCurrentUser().getEmail());
        DocumentReference docUserInfo = fs.collection("All Users On App").document(adminData.getEmail());
        docUserInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String cId= (String) documentSnapshot.get("College");
                String dept=(String) documentSnapshot.get("Department");
                adminData.setCollegeId(cId);
                adminData.setDeptName(dept);
                fs.collection("All Colleges").document(cId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                ArrayList<String> deptData= (ArrayList<String>) documentSnapshot.get("Department for Data");
                                if (deptData.contains(dept))
                                {
                                    editReq.setVisibility(View.VISIBLE);
                                    lockDatabase.setVisibility(View.VISIBLE);
                                }
                                FirebaseMessaging.getInstance().subscribeToTopic("All");
                                FirebaseMessaging.getInstance().subscribeToTopic(f.getCurrentUser().getEmail());
                                FirebaseMessaging.getInstance().subscribeToTopic(cId);
                                FirebaseMessaging.getInstance().subscribeToTopic("Admin_"+cId);
                                FirebaseMessaging.getInstance().subscribeToTopic(cId+"_"+dept.replaceAll("\\s", ""));
                                token.add(cId);
                                token.add("Admin_"+cId);
                                token.add(cId+"_"+dept.replaceAll("\\s", ""));
                                adminData.setToken(token);
                                DocumentReference docUserInfoAll = fs.collection("All Colleges").document(cId).collection("UsersInfo").document(adminData.getEmail());
                                docUserInfoAll.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        String name= (String) documentSnapshot.get("Name");
                                        String phone=(String) documentSnapshot.get("Phone Number");
                                        adminData.setPhoneNumber(phone);
                                        adminData.setAdminName(name);
                                        greetings.setText(name);
                                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(cId).child("Photograph").child(adminData.getEmail());
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(AdminPage.this)
                                                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                                        .error(R.drawable.admin_profile_img)
                                                        .placeholder(R.drawable.default_loading_img)
                                                        .into(profilepic);
                                            }
                                        });

                                    }
                                });
                            }
                        });
            }
        });
    }
    public void createNewUser(View v){
        startActivity(new Intent(AdminPage.this, AdminCreateNewAccount.class));
    }
    public void sendNotification(View v){
        startActivity(new Intent(AdminPage.this, AdminOrCompanySendNotification.class));
    }
    public void viewNotifications(View v){
        startActivity(new Intent(AdminPage.this, ViewNotificationsByAll.class));
    }
    public void searchUser(View v){
        startActivity(new Intent(AdminPage.this, AdminSearchUser.class));
    }
    public void viewAllEditRequests(View v){
        startActivity(new Intent(AdminPage.this, AdminViewAllRequests.class));
    }
    public void answerFAQ(View v){
        startActivity(new Intent(AdminPage.this, AdminAnswerFAQ.class));
    }
    public void editProfile(View v){
        startActivity(new Intent(AdminPage.this, AdminEditProfilePage.class));
    }
    public void lockDatabase(View v){
        startActivity(new Intent(AdminPage.this, AdminLockDatabaseFilters.class));
    }
    public void logout(View v)
    {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(adminData.getCollegeId());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(f.getCurrentUser().getEmail());
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Admin_"+adminData.getCollegeId());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(adminData.getCollegeId()+"_"+adminData.getDeptName().replaceAll("\\s", ""));
        f.signOut();
        Toast.makeText(this,"Logged Out",Toast.LENGTH_LONG).show();
        //adminData=null;
        startActivity(new Intent(AdminPage.this,Login.class));
        finish();
    }
    public void help(View v)
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> deptData= (ArrayList<String>) documentSnapshot.get("Department for Data");
                        if (deptData.contains(adminData.getDeptName()))
                        {
                            StorageReference storageRef = storage.getReference("Developer Folder").child("Admin Help.pdf");
                            Task<Uri> helpDoc = storageRef.getDownloadUrl();
                            helpDoc.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Intent intent = new Intent(AdminPage.this, ViewPDFActivity.class);
                                    intent.putExtra("url", uri.toString());
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminPage.this,e.getMessage().toString(),Toast.LENGTH_LONG);

                                }
                            });
                        }
                        else
                        {
                            StorageReference storageRef = storage.getReference("Developer Folder").child("Admin Help 2.pdf");
                            Task<Uri> helpDoc = storageRef.getDownloadUrl();
                            helpDoc.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Intent intent = new Intent(AdminPage.this, ViewPDFActivity.class);
                                    intent.putExtra("url", uri.toString());
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminPage.this,e.getMessage().toString(),Toast.LENGTH_LONG);

                                }
                            });
                        }
                    }
                });
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminPage.this,EndScreen.class));
    }
}