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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterCollege8 extends AppCompatActivity {
    CollegeRegistrationData allData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college8);
        ProgressBar progressBar=findViewById(R.id.progressBar2);
        allData=RegisterCollege.allData;
        TextView t=new TextView(this);
        t.setText("Data Uploading... PLEASE DO NOT PRESS BACK OR EXIT");
        t.setTextSize(22);
        LinearLayout cl=findViewById(R.id.cl);
        cl.addView(t);
        FirebaseFirestore f1=FirebaseFirestore.getInstance();
        //storing basic information about every single user on the app
        DocumentReference forGeneralInformation = f1.collection("All Users On App").document(allData.getSAdminemail());
        HashMap<String,Object> generaInfoAdmin=new HashMap<>();
        generaInfoAdmin.put("College",allData.getUname());
        generaInfoAdmin.put("Category","Admin");
        generaInfoAdmin.put("Department","Main Admin");
        generaInfoAdmin.put("New",false);

        forGeneralInformation.set(generaInfoAdmin).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //storing basic information about college
                DocumentReference collegeInformation = f1.collection("All Colleges").document(allData.getUname());
                HashMap<String,Object> basicInfoOfCollege=new HashMap<>();

                TextView t=new TextView(RegisterCollege8.this);
                t.setText("Uploading college data...");
                t.setTextSize(16);
                cl.addView(t);
                basicInfoOfCollege.put("Location",allData.getLocation());
                basicInfoOfCollege.put("Name",allData.getCName());
                basicInfoOfCollege.put("Departments",allData.getDeptName());
                basicInfoOfCollege.put("Courses",allData.getCourseName());
                basicInfoOfCollege.put("Main Admin",allData.getSAdminemail());
                collegeInformation.set(basicInfoOfCollege).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        ArrayList<ArrayList<String>> allbranch = allData.getBranchForEachCourse();
                        ArrayList<String> allcourseName = allData.getCourseName();
                        for(int i=0;i<allbranch.size();i++)
                        {
                            DocumentReference branchInfo = collegeInformation.collection("Branches").document(allcourseName.get(i));
                            ArrayList<String> itsBranch = allbranch.get(i);
                            HashMap<String,Object> branches=new HashMap<>();
                            branches.put("Branches",itsBranch);
                            int finalI = i;
                            branchInfo.set(branches).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    TextView t=new TextView(RegisterCollege8.this);
                                    t.setText("COURSE AND BRANCHES UPLOADING ..."+ finalI);
                                    t.setTextSize(16);
                                    cl.addView(t);
                                }
                            });

                        }//all branches uploaded
                        TextView t=new TextView(RegisterCollege8.this);
                        t.setText("COURSE AND BRANCHES UPLOADED...");
                        t.setTextSize(16);
                        cl.addView(t);
                        DocumentReference personalQuestion = collegeInformation.collection("Questions").document("Personal Question");
                        TextView t1=new TextView(RegisterCollege8.this);
                        t1.setText("PERSONAL QUESTIONS UPLOADING");
                        t1.setTextSize(16);
                        cl.addView(t1);
                        CollegeRegisterQuestions[] personalQuestionArray = allData.getQuestions_personal();
                        HashMap<String,Object> tot=new HashMap<>();
                        tot.put("Total",personalQuestionArray.length);
                        personalQuestion.set(tot).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                HashMap<String, Object> questionDetails[] = new HashMap[personalQuestionArray.length];

                                for (int i=0;i<personalQuestionArray.length;i++) {
                                    CollegeRegisterQuestions currentQuestion = personalQuestionArray[i];
                                    Log.e("Personal question" + i, currentQuestion.getQuestion());
                                    HashMap<String,Object> questionDetailsp=new HashMap<>();
                                    questionDetailsp.put("Question", currentQuestion.getQuestion());
                                    questionDetailsp.put("Type", currentQuestion.getType());
                                    questionDetailsp.put("Compulsory", currentQuestion.isCumplolsory());
                                    questionDetailsp.put("Editable", currentQuestion.isChangeable());
                                    questionDetails[i]=questionDetailsp;
                                }
                                for (int i=0;i<personalQuestionArray.length;i++) {
                                    DocumentReference currentQuestionDoc = personalQuestion.collection(i + "").document(i + "");
                                    int finalI = i;
                                    currentQuestionDoc.set(questionDetails[i]).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            TextView t=new TextView(RegisterCollege8.this);
                                            t.setText("PERSONAL QUESTION UPLOADING..."+ finalI);
                                            t.setTextSize(16);
                                            cl.addView(t);
                                        }
                                    });
                                }//Personal questions added
                                DocumentReference academicQuestion = collegeInformation.collection("Questions").document("Academic Question");
                                TextView t1=new TextView(RegisterCollege8.this);
                                t1.setText("ACADEMIC QUESTIONS UPLOADING");
                                t1.setTextSize(16);
                                cl.addView(t1);
                                CollegeRegisterQuestions[] academicQuestionArray = allData.getQuestions_academic();
                                HashMap<String,Object> tot=new HashMap<>();
                                tot.put("Total",academicQuestionArray.length);
                                academicQuestion.set(tot).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        for (int i=0;i<academicQuestionArray.length;i++)
                                        {
                                            DocumentReference currentQuestionDoc = academicQuestion.collection(i + "").document(i + "");
                                            HashMap<String,Object> questionDetails=new HashMap<>();
                                            CollegeRegisterQuestions currentQuestion=academicQuestionArray[i];
                                            questionDetails.put("Question",currentQuestion.getQuestion());
                                            questionDetails.put("Type",currentQuestion.getType());
                                            questionDetails.put("Compulsory",currentQuestion.isCumplolsory());
                                            questionDetails.put("Editable",currentQuestion.isChangeable());
                                            int finalI = i;
                                            currentQuestionDoc.set(questionDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    TextView t=new TextView(RegisterCollege8.this);
                                                    t.setText("ACADEMIC QUESTION UPLOADING..."+ finalI);
                                                    t.setTextSize(16);
                                                    cl.addView(t);
                                                }
                                            });
                                        }//Academic questions added
                                        DocumentReference uploadQuestion = collegeInformation.collection("Questions").document("Upload Question");
                                        TextView t1=new TextView(RegisterCollege8.this);
                                        t1.setText("UPLOAD QUESTIONS UPLOADING");
                                        t1.setTextSize(16);
                                        cl.addView(t1);
                                        CollegeRegisterQuestions[] uploadQuestionArray = allData.getQuestions_upload();
                                        HashMap<String,Object> tot=new HashMap<>();
                                        tot.put("Total",uploadQuestionArray.length);
                                        uploadQuestion.set(tot).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                for (int i=0;i<uploadQuestionArray.length;i++)
                                                {
                                                    DocumentReference currentQuestionDoc = uploadQuestion.collection(i + "").document(i + "");
                                                    HashMap<String,Object> questionDetails=new HashMap<>();
                                                    CollegeRegisterQuestions currentQuestion=uploadQuestionArray[i];
                                                    questionDetails.put("Question",currentQuestion.getQuestion());
                                                    questionDetails.put("Type",currentQuestion.getType());
                                                    questionDetails.put("Compulsory",currentQuestion.isCumplolsory());
                                                    questionDetails.put("Editable",currentQuestion.isChangeable());
                                                    int finalI = i;
                                                    currentQuestionDoc.set(questionDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            TextView t=new TextView(RegisterCollege8.this);
                                                            t.setText("ACADEMIC QUESTION UPLOADING..."+ finalI);
                                                            t.setTextSize(16);
                                                            cl.addView(t);
                                                        }
                                                    });
                                                }//Upload questions added
                                                DocumentReference userInfo = collegeInformation.collection("UsersInfo").document("Admin").collection("Main Admin").document(allData.getSAdminemail());
                                                HashMap<String , Object> user=new HashMap<>();
                                                user.put("Name",allData.getCName()+" Main Admin");
                                                userInfo.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        FirebaseAuth fauth = FirebaseAuth.getInstance();
                                                        TextView t=new TextView(RegisterCollege8.this);
                                                        t.setText("ADMIN INFORMATION UPLOADING...\n SIGNING IN");
                                                        t.setTextSize(16);
                                                        cl.addView(t);
                                                        fauth.createUserWithEmailAndPassword(allData.getSAdminemail(),allData.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                            @Override
                                                            public void onSuccess(AuthResult authResult) {
                                                                DocumentReference collegeTillNow = f1.collection("All Users On App").document("All Colleges");
                                                                collegeTillNow.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        if(documentSnapshot.exists())
                                                                        {
                                                                            ArrayList<String> collegeNames= (ArrayList<String>) documentSnapshot.get("Colleges");
                                                                            ArrayList<String> collegeIds= (ArrayList<String>) documentSnapshot.get("IDs");
                                                                            collegeNames.add(allData.getCName()+","+allData.getLocation());
                                                                            collegeIds.add(allData.getUname());
                                                                            HashMap<String,Object> c=new HashMap<>();
                                                                            c.put("Colleges",collegeNames);
                                                                            c.put("IDs",collegeIds);
                                                                            collegeTillNow.update(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    TextView t=new TextView(RegisterCollege8.this);
                                                                                    t.setText("PROCESS COMPLETE");
                                                                                    t.setTextSize(40);
                                                                                    cl.addView(t);
                                                                                    Toast.makeText(RegisterCollege8.this,"College Registered",Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(RegisterCollege8.this,Login.class));
                                                                                }
                                                                            });
                                                                        }
                                                                        else
                                                                        {
                                                                            ArrayList<String> collegeNames= new ArrayList<>();
                                                                            ArrayList<String> collegeIds= new ArrayList<>();
                                                                            collegeNames.add(allData.getCName().trim()+", "+allData.getLocation());
                                                                            collegeIds.add(allData.getUname());
                                                                            HashMap<String,Object> c=new HashMap<>();
                                                                            c.put("Colleges",collegeNames);
                                                                            c.put("IDs",collegeIds);

                                                                            collegeTillNow.set(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    TextView t=new TextView(RegisterCollege8.this);
                                                                                    t.setText("PROCESS COMPLETE");
                                                                                    t.setTextSize(40);
                                                                                    cl.addView(t);
                                                                                    Toast.makeText(RegisterCollege8.this,"College Registered",Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(RegisterCollege8.this,Login.class));
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(RegisterCollege8.this,e.toString(),Toast.LENGTH_SHORT).show();
                                                                progressBar.setVisibility(View.GONE);
                                                                TextView t=new TextView(RegisterCollege8.this);
                                                                t.setText("FAILED");
                                                                t.setTextSize(40);
                                                                cl.addView(t);
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(RegisterCollege8.this,e.toString(),Toast.LENGTH_SHORT).show();
                                                        progressBar.setVisibility(View.GONE);
                                                        TextView t=new TextView(RegisterCollege8.this);
                                                        t.setText("FAILED");
                                                        t.setTextSize(40);
                                                        cl.addView(t);
                                                    }
                                                });
                                            }
                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterCollege8.this,e.toString(),Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                                TextView t=new TextView(RegisterCollege8.this);
                                                t.setText("FAILED");
                                                t.setTextSize(40);
                                                cl.addView(t);
                                            }
                                        });;

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterCollege8.this,e.toString(),Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        TextView t=new TextView(RegisterCollege8.this);
                                        t.setText("FAILED");
                                        t.setTextSize(40);
                                        cl.addView(t);
                                    }
                                });;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterCollege8.this,e.toString(),Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                TextView t=new TextView(RegisterCollege8.this);
                                t.setText("FAILED");
                                t.setTextSize(40);
                                cl.addView(t);
                            }
                        });;
            }//basic admin info uploaded
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterCollege8.this,e.toString(),Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        TextView t=new TextView(RegisterCollege8.this);
                        t.setText("FAILED");
                        t.setTextSize(40);
                        cl.addView(t);
                    }
                });;
    }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterCollege8.this,e.toString(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                TextView t=new TextView(RegisterCollege8.this);
                t.setText("FAILED");
                t.setTextSize(40);
                cl.addView(t);
            }
        });;
    }
}