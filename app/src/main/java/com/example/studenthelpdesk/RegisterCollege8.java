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
import java.util.Calendar;
import java.util.Date;
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
        t.setText("Data Uploading... \nPLEASE DO NOT PRESS BACK OR EXIT");
        t.setTextSize(22);
        LinearLayout cl=findViewById(R.id.cl);
        cl.addView(t);
        FirebaseFirestore f1=FirebaseFirestore.getInstance();
        //storing basic information about every single user on the app
        DocumentReference forGeneralInformation = f1.collection("All Users On App").document(allData.getSAdminemail());
        HashMap<String,Object> generaInfoAdmin=new HashMap<>();
        generaInfoAdmin.put("College",allData.getUname());
        generaInfoAdmin.put("Category","Admin");
        generaInfoAdmin.put("Department",allData.getAdminDept());
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
                basicInfoOfCollege.put("Department for Data",allData.getDataDept());
                basicInfoOfCollege.put("Main Admin",allData.getSAdminemail());
                collegeInformation.set(basicInfoOfCollege).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        ArrayList<ArrayList<String>> allbranch = allData.getBranchForEachCourse();
                        ArrayList<ArrayList<String>> alldept = allData.getDepForEachCourse();

                        ArrayList<String> allcourseName = allData.getCourseName();
                        for(int i=0;i<allbranch.size();i++)
                        {
                            DocumentReference branchInfo = collegeInformation.collection("Branches").document(allcourseName.get(i));
                            ArrayList<String> itsBranch = allbranch.get(i);
                            HashMap<String,Object> branches=new HashMap<>();
                            branches.put("Branches",itsBranch);
                            branches.put("Department",alldept.get(i));
                            int finalI = i;
                            branchInfo.set(branches).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    TextView t=new TextView(RegisterCollege8.this);
                                    t.setText("COURSE AND BRANCHES UPLOADING ..."+ finalI);
                                    t.setTextSize(18);
                                    cl.addView(t);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    branchInfo.delete();
                                    TextView t=new TextView(RegisterCollege8.this);
                                    t.setText("Failure...");
                                    t.setTextSize(16);
                                    cl.addView(t);
                                    startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                                    return;
                                }
                            });

                        }//all branches uploaded

                        for(int i=0;i<allbranch.size();i++)
                        {
                            DocumentReference branchInfoLock = collegeInformation.collection("Lock").document(allcourseName.get(i));
                            ArrayList<String> itsBranch = allbranch.get(i);
                            HashMap<String,Object> brancheslock=new HashMap<>();
                            for(int j=0;j<itsBranch.size();j++)
                                brancheslock.put(itsBranch.get(j),false);
                            int finalI = i;
                            branchInfoLock.set(brancheslock).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    TextView t=new TextView(RegisterCollege8.this);
                                    t.setText("COURSE AND BRANCHES LOCKS UPLOADING ..."+ finalI);
                                    t.setTextSize(18);
                                    cl.addView(t);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    branchInfoLock.delete();
                                    TextView t=new TextView(RegisterCollege8.this);
                                    t.setText("Failure...");
                                    t.setTextSize(16);
                                    cl.addView(t);
                                    startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                                    finish();
                                    return;
                                }
                            });

                        }
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
                        int personalTotal=allData.getTotalPersonal();
                        HashMap<String,Object> tot=new HashMap<>();
                        tot.put("Total",personalTotal);
                        personalQuestion.set(tot).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                ArrayList<HashMap<String, Object>> questionDetails = new ArrayList<>();
                                int c1=0;
                                for (int i=0;i<personalQuestionArray.length;i++) {
                                    CollegeRegisterQuestions currentQuestion = personalQuestionArray[i];
                                    if(currentQuestion==null)
                                        continue;
                                    HashMap<String,Object> questionDetailsp=new HashMap<>();
                                    questionDetailsp.put("Question", currentQuestion.getQuestion());
                                    questionDetailsp.put("Type", currentQuestion.getType());
                                    questionDetailsp.put("Compulsory", currentQuestion.isCumplolsory());
                                    questionDetailsp.put("Editable", currentQuestion.isChangeable());
                                    questionDetails.add(questionDetailsp);
                                }
                                for (int i=0;i<questionDetails.size();i++) {
                                    DocumentReference currentQuestionDoc = personalQuestion.collection(i + "").document(i + "");
                                    int finalI = i;
                                    currentQuestionDoc.set(questionDetails.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                tot.put("Total",allData.getTotalAcademic());
                                academicQuestion.set(tot).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        for (int i=0;i<academicQuestionArray.length;i++)
                                        {
                                            DocumentReference currentQuestionDoc = academicQuestion.collection(i + "").document(i + "");
                                            HashMap<String,Object> questionDetails=new HashMap<>();
                                            CollegeRegisterQuestions currentQuestion=academicQuestionArray[i];
                                            if(currentQuestion==null)
                                                continue;
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
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    academicQuestion.delete();
                                                    TextView t=new TextView(RegisterCollege8.this);
                                                    t.setText("Failure occurred");
                                                    t.setTextSize(16);
                                                    cl.addView(t);
                                                    startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                                                    finish();
                                                    return;
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
                                        tot.put("Total",allData.getTotalUpload());
                                        uploadQuestion.set(tot).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                for (int i=0;i<uploadQuestionArray.length;i++)
                                                {
                                                    DocumentReference currentQuestionDoc = uploadQuestion.collection(i + "").document(i + "");
                                                    HashMap<String,Object> questionDetails=new HashMap<>();
                                                    CollegeRegisterQuestions currentQuestion=uploadQuestionArray[i];
                                                    if(currentQuestion==null)
                                                        continue;
                                                    questionDetails.put("Question",currentQuestion.getQuestion());
                                                    questionDetails.put("Type",currentQuestion.getType());
                                                    questionDetails.put("Compulsory",currentQuestion.isCumplolsory());
                                                    questionDetails.put("Editable",currentQuestion.isChangeable());
                                                    int finalI = i;
                                                    currentQuestionDoc.set(questionDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            TextView t=new TextView(RegisterCollege8.this);
                                                            t.setText("UPLOAD QUESTION UPLOADING..."+ finalI);
                                                            t.setTextSize(16);
                                                            cl.addView(t);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            uploadQuestion.delete();
                                                            TextView t=new TextView(RegisterCollege8.this);
                                                            t.setText("Uploading Failed");
                                                            t.setTextSize(16);
                                                            cl.addView(t);
                                                            startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                                                            finish();
                                                            return;
                                                        }
                                                    });
                                                }//Upload questions added
                                                DocumentReference userInfo = collegeInformation.collection("UsersInfo").document(allData.getSAdminemail());
                                                HashMap<String , Object> user=new HashMap<>();
                                                user.put("Name",allData.getAdminName());
                                                user.put("Phone Number",allData.getAdminNo());
                                                user.put("Department",allData.getAdminDept());
                                                user.put("Category","Admin");
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
                                                                            collegeNames.add(allData.getCName()+", "+allData.getLocation());
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
                                                                                    startActivity(new Intent(RegisterCollege8.this,AdminPage.class));
                                                                                    finishAffinity();
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
                                                                            Date t1= Calendar.getInstance().getTime();

                                                                            collegeTillNow.set(c).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    TextView t=new TextView(RegisterCollege8.this);
                                                                                    t.setText("PROCESS COMPLETE");
                                                                                    t.setTextSize(40);
                                                                                    cl.addView(t);

                                                                                    Toast.makeText(RegisterCollege8.this,"College Registered",Toast.LENGTH_SHORT).show();
                                                                                    startActivity(new Intent(RegisterCollege8.this,AdminPage.class));
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
                                                                DocumentReference collegeInformation = f1.collection("All Colleges").document(allData.getUname());
                                                                collegeInformation.delete();
                                                                startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                                                                finish();
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

                                                        DocumentReference collegeInformation = f1.collection("All Colleges").document(allData.getUname());
                                                        collegeInformation.delete();
                                                        startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                                                        finish();
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
                                                DocumentReference collegeInformation = f1.collection("All Colleges").document(allData.getUname());
                                                collegeInformation.delete();
                                                startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                                                finish();
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

                                        DocumentReference collegeInformation = f1.collection("All Colleges").document(allData.getUname());
                                        collegeInformation.delete();
                                        startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                                        finish();
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
                                personalQuestion.delete();
                                startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                                finish();
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
                        DocumentReference collegeInformation = f1.collection("All Colleges").document(allData.getUname());
                        collegeInformation.delete();
                        startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                        finish();
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
                DocumentReference forGeneralInformation = f1.collection("All Users On App").document(allData.getSAdminemail());
                forGeneralInformation.delete();
                startActivity(new Intent(RegisterCollege8.this,RegisterCollege7_SuperAdminSignup.class));
                finish();
            }
        });;
    }
    @Override
    public void onBackPressed() {
        //do nothing
    }
}