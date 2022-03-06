package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashSet;

public class AdminPage extends AppCompatActivity {
    static AdminData adminData;
     FirebaseAuth f;
     TextView greetings;
     ImageView profilepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        adminData=new AdminData();
        greetings=findViewById(R.id.name);
        profilepic=findViewById(R.id.profilePic);
        f=FirebaseAuth.getInstance();
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
    public void logout(View v)
    {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(adminData.getCollegeId());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(f.getCurrentUser().getEmail());
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Admin_"+adminData.getCollegeId());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(adminData.getCollegeId()+"_"+adminData.getDeptName().replaceAll("\\s", ""));
        f.signOut();
        Toast.makeText(this,"Logged Out",Toast.LENGTH_LONG).show();
        startActivity(new Intent(AdminPage.this,Login.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdminPage.this,EndScreen.class));
    }
}