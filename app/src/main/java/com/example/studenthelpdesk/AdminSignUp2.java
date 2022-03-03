package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AdminSignUp2 extends AppCompatActivity {
    AdminData adminData;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up2);
        adminData=Signup.adminData;
        ProgressBar progressBar=findViewById(R.id.progressBar2);
        ll=findViewById(R.id.ll);
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference docUserBasicInfo = f.collection("All Users On App").document(adminData.getEmail());
        HashMap<String,Object> userBasicInfo=new HashMap<>();
        userBasicInfo.put("New",false);
        userBasicInfo.put("Department",adminData.getDeptName());
        setText("Uploaded Basic Info");
        docUserBasicInfo.update(userBasicInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                setText("Uploaded Basic Info");
                DocumentReference docUserInfoAll=f.collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(adminData.getEmail());
                HashMap<String,Object> userInfoAll=new HashMap<>();
                userInfoAll.put("Phone Number",adminData.getPhoneNumber());
                userInfoAll.put("Name",adminData.getAdminName());
                userInfoAll.put("Category","Admin");
                userInfoAll.put("Department",adminData.getDeptName());
                userInfoAll.put("Email",adminData.getEmail());
                docUserInfoAll.set(userInfoAll).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        setText("Admin Data Uploaded\nAuthenticating now");
                        FirebaseAuth f1=FirebaseAuth.getInstance();
                        f1.createUserWithEmailAndPassword(adminData.getEmail(),adminData.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                setText("SignUp done");
                                Toast.makeText(AdminSignUp2.this,"Signup done \nNow you may LOGIN",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AdminSignUp2.this,Login.class));
                                finishAffinity();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                setText("FAILED");
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(AdminSignUp2.this,e.toString(),Toast.LENGTH_LONG).show();
                                startActivity(new Intent(AdminSignUp2.this,Signup.class));
                                finishAffinity();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setText("FAILED");
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(AdminSignUp2.this,e.toString(),Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AdminSignUp2.this,Signup.class));
                        finishAffinity();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setText("FAILED");
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminSignUp2.this,e.toString(),Toast.LENGTH_LONG).show();
                startActivity(new Intent(AdminSignUp2.this,Signup.class));
                finishAffinity();
            }
        });
    }
    void setText(String s)
    {
        TextView t=new TextView(this);
        t.setText(s);
        t.setTextSize(16);
        ll.addView(t);
    }
    @Override
    public void onBackPressed() {
        //do nothing
    }
}