package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class StudentSignupDone extends AppCompatActivity {
    StudentData studentData;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_signup_done);
        studentData = Signup.studentData;
        ll = findViewById(R.id.ll);
        ProgressBar progressBar=findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore f = FirebaseFirestore.getInstance();
        setText("Started Uploading data");
         {

                DocumentReference docUserAllInfo = f.collection("All Colleges").document(studentData.getCollegeid()).collection("UsersInfo").document(studentData.getEmail());
                setText("Saving basic user Info");
                HashMap<String,Object> basicInfo=new HashMap<>();
                basicInfo.put("Name",studentData.getName());
                basicInfo.put("Category","Student");
                basicInfo.put("Course",studentData.getCourse());
                basicInfo.put("Branch",studentData.getBranch());
                basicInfo.put("Year",studentData.getYr());
                basicInfo.put("Number of Requests",0);
                docUserAllInfo.set(basicInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for(int i=0;i<studentData.getA_id().length;i++)
                        {
                            DocumentReference docAcadQues = docUserAllInfo.collection("Academic Question").document(studentData.getA_id()[i]);
                            HashMap<String,Object> currQuestion=new HashMap<>();
                            currQuestion.put("Question",studentData.getA_ques()[i]);
                            currQuestion.put("Answer",studentData.getA_ans()[i]);
                            int finalI = i;
                            docAcadQues.set(currQuestion).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    setText("Academic questions uploading.."+ finalI);
                                }
                            });
                            if(i==studentData.getA_id().length-1)
                            {
                                for(int i1=0;i1<studentData.getP_id().length;i1++)
                                {
                                    DocumentReference docPerQues = docUserAllInfo.collection("Personal Question").document(studentData.getP_id()[i1]);
                                    HashMap<String,Object> currQuestion1=new HashMap<>();
                                    currQuestion1.put("Question",studentData.getP_ques()[i1]);
                                    currQuestion1.put("Answer",studentData.getP_ans()[i1]);
                                    int finalI1 = i1;
                                    docPerQues.set(currQuestion1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            setText("Personal questions uploading.."+ finalI1);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(StudentSignupDone.this,e.toString(),Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            setText("FAILED");
                                        }
                                    });;
                                    if(i1==studentData.getP_ans().length-1)
                                    {
                                        FirebaseAuth f1=FirebaseAuth.getInstance();
                                        f1.createUserWithEmailAndPassword(studentData.getEmail(),studentData.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                setText("Authenticating user");
                                                DocumentReference docChangeState = f.collection("All Users On App").document(studentData.getEmail());
                                                HashMap<String,Object> changeState=new HashMap<>();
                                                changeState.put("New",false);
                                                docChangeState.update(changeState).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        setText("Process Complete");
                                                        Toast.makeText(StudentSignupDone.this,"Signup Done",Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(StudentSignupDone.this,StudentPage.class));
                                                        finishAffinity();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(StudentSignupDone.this,e.toString(),Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        setText("FAILED");
                                                        startActivity(new Intent(StudentSignupDone.this,Signup.class));
                                                        finishAffinity();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(StudentSignupDone.this,e.toString(),Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                                setText("FAILED");
                                                startActivity(new Intent(StudentSignupDone.this,Signup.class));
                                                finishAffinity();
                                            }
                                        });;
                                    }
                                }
                            }
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentSignupDone.this,e.toString(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        setText("FAILED");
                        startActivity(new Intent(StudentSignupDone.this,Signup.class));
                        finishAffinity();
                    }
                });;
            }

    }
    void setText(String s)
    {
        TextView t=new TextView(StudentSignupDone.this);
        t.setText(s);
        t.setTextSize(16);
        ll.addView(t);
    }
    @Override
    public void onBackPressed() {
        //do nothing
    }
}