package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CompanySignUp2 extends AppCompatActivity {
    CompanyData companyData;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_sign_up2);
        companyData=Signup.companyData;
        ll=findViewById(R.id.ll);
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference docUserBasicInfo = f.collection("All Users On App").document(companyData.getEmail());
        HashMap<String,Object> userBasicInfo=new HashMap<>();
        userBasicInfo.put("New",false);
        setText("Uploaded Basic Info");
        docUserBasicInfo.update(userBasicInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                setText("Uploaded Basic Info");
                DocumentReference docUserInfoAll=f.collection("All Colleges").document(companyData.getCollegeId()).collection("UsersInfo").document("Company").collection("Details").document(companyData.getEmail());
                HashMap<String,Object> userInfoAll=new HashMap<>();
                userInfoAll.put("Phone Number",companyData.getPhone());
                userInfoAll.put("Name",companyData.getName());
                userInfoAll.put("Company Name",companyData.getCompanyName());
                userInfoAll.put("Company Location",companyData.getLocation());
                userInfoAll.put("Company Email",companyData.getPersonalEmail());
                docUserInfoAll.set(userInfoAll).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        setText("Admin Data Uploaded\nAuthenticating now");
                        FirebaseAuth f1=FirebaseAuth.getInstance();
                        f1.createUserWithEmailAndPassword(companyData.getEmail(),companyData.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                setText("SignUp done");
                                Toast.makeText(CompanySignUp2.this,"Signup done \nNow you may LOGIN",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(CompanySignUp2.this,Login.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                setText("FAILED");
                                Toast.makeText(CompanySignUp2.this,e.toString(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setText("FAILED");
                        Toast.makeText(CompanySignUp2.this,e.toString(),Toast.LENGTH_LONG).show();
                    }
                });;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                setText("FAILED");
                Toast.makeText(CompanySignUp2.this,e.toString(),Toast.LENGTH_LONG).show();
            }
        });;
    }
    void setText(String s)
    {
        TextView t=new TextView(this);
        t.setText(s);
        t.setTextSize(16);
        ll.addView(t);
    }
}