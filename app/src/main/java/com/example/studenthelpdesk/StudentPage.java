package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class StudentPage extends AppCompatActivity {
    FirebaseAuth f;
    TextView heading,reqStatus,email;
    ImageView profilepic;
    static StudentData studentData;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);
        f=FirebaseAuth.getInstance();
        if(f==null)
        {
            startActivity(new Intent(this,Login.class));
            finish();
        }
        FirebaseMessaging.getInstance().subscribeToTopic("All");
        FirebaseMessaging.getInstance().subscribeToTopic(f.getCurrentUser().getEmail());
        HashSet<String> token=new HashSet<>();
        token.add("All");
        token.add(f.getCurrentUser().getEmail());
        studentData=new StudentData();
        heading=findViewById(R.id.name);
        progressBar=findViewById(R.id.progressBar4);
        email=findViewById(R.id.email);
        reqStatus=findViewById(R.id.requests_status);
        profilepic=findViewById(R.id.profile);
        studentData.setEmail(f.getCurrentUser().getEmail());
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        DocumentReference docUserInfo = ff.collection("All Users On App").document(studentData.getEmail());
        docUserInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String cId= (String) documentSnapshot.get("College");
                studentData.setCollegeid(cId);
                 {
                        {
                            FirebaseMessaging.getInstance().subscribeToTopic("Student" + studentData.getCollegeid());
                            FirebaseMessaging.getInstance().subscribeToTopic(studentData.getCollegeid());
                            FirebaseMessaging.getInstance().subscribeToTopic(studentData.getCollegeid() + "_" + studentData.getCourse());
                            FirebaseMessaging.getInstance().subscribeToTopic(studentData.getCollegeid() + "_" + studentData.getCourse() + "_" + studentData.getBranch());
                            FirebaseMessaging.getInstance().subscribeToTopic(studentData.getCollegeid() + "_" + studentData.getCourse() + "_" + studentData.getBranch() + "_" + studentData.getYr());
                            token.add("Student" + studentData.getCollegeid());
                            token.add(studentData.getCollegeid());
                            token.add(studentData.getCollegeid() + "_" + studentData.getCourse());
                            token.add(studentData.getCollegeid() + "_" + studentData.getCourse() + "_" + studentData.getBranch());
                            token.add(studentData.getCollegeid() + "_" + studentData.getCourse() + "_" + studentData.getBranch() + "_" + studentData.getYr());
                            studentData.setToken(token);
                        }
                        DocumentReference docUserInfo2 = ff.collection("All Colleges").document(studentData.getCollegeid()).collection("UsersInfo").document(studentData.getEmail());
                        docUserInfo2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                String branch= (String) documentSnapshot.get("Branch");
                                String yr= (String) documentSnapshot.get("Year");
                                String course= (String) documentSnapshot.get("Course");
                                String name = (String) documentSnapshot.get("Name");
                                long noOfRequest= (long) documentSnapshot.get("Number of Requests");
                                studentData.setName(name);
                                studentData.setNoOfReq(noOfRequest);
                                studentData.setBranch(branch);
                                studentData.setCourse(course);
                                studentData.setYr(yr);
                                heading.setText(name);
                                email.setText(studentData.getEmail());
                                reqStatus.setText(reqStatus.getText().toString().substring(0,25)+noOfRequest+" requests");
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference(cId).child("Photograph").child(studentData.getEmail());
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(StudentPage.this)
                                                .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .error(R.drawable.profile_pic)
                                                .placeholder(R.drawable.profile_pic)
                                                .into(profilepic);

                                    }
                                });

                                final ArrayList<CollegeRegisterQuestions> personalQ = new ArrayList<>();
                                final ArrayList<CollegeRegisterQuestions> academicQ = new ArrayList<>();
                                final ArrayList<CollegeRegisterQuestions> uploadQ = new ArrayList<>();
                                DocumentReference docPersQues = ff.collection("All Colleges").document(studentData.getCollegeid()).collection("Questions").document("Personal Question");
                                docPersQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        long total= (long) documentSnapshot.get("Total");
                                        studentData.setNoPersonalQ(total);
                                        for (int i=0;i<(int)total;i++)
                                        {
                                            DocumentReference docCurrQues = docPersQues.collection(i + "").document(i + "");
                                            int finalI = i;
                                            int finalI1 = i;
                                            int finalI2 = i;
                                            docCurrQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    CollegeRegisterQuestions currQ=new CollegeRegisterQuestions();
                                                    boolean editable= (boolean) documentSnapshot.get("Editable");
                                                    boolean compulsory = (boolean) documentSnapshot.get("Compulsory");
                                                    String ques= (String) documentSnapshot.get("Question");
                                                    long type=(long) documentSnapshot.get("Type");
                                                    currQ.setChangeable(editable);
                                                    currQ.setCompulsory(compulsory);
                                                    currQ.setType((int) type);
                                                    currQ.setQuestion(ques);
                                                    currQ.setId(finalI2);
                                                    DocumentReference ans= docUserInfo2.collection("Personal Question").document(finalI+"");
                                                    ans.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            String ans= (String) documentSnapshot.get("Answer");
                                                            currQ.setAnswer(ans);
                                                            personalQ.add(currQ);
                                                            if(finalI1 ==total-1)
                                                            {
                                                                Collections.sort(personalQ,new Comparator<CollegeRegisterQuestions>() {
                                                                    @Override
                                                                    public int compare(CollegeRegisterQuestions o1,CollegeRegisterQuestions o2) {
                                                                        int i1 = (o1.getId() - (o2.getId()));
                                                                        return i1;
                                                                    }
                                                                });
                                                                studentData.setPersonal_ques(personalQ);
                                                                DocumentReference docPersQues = ff.collection("All Colleges").document(studentData.getCollegeid()).collection("Questions").document("Academic Question");
                                                                docPersQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        long total= (long) documentSnapshot.get("Total");
                                                                        studentData.setNoPersonalQ(total);
                                                                        for (int i=0;i<(int)total;i++)
                                                                        {
                                                                            DocumentReference docCurrQues = docPersQues.collection(i + "").document(i + "");
                                                                            int finalI = i;
                                                                            int finalI1 = i;
                                                                            int finalI2 = i;
                                                                            docCurrQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                    CollegeRegisterQuestions currQ=new CollegeRegisterQuestions();
                                                                                    boolean editable= (boolean) documentSnapshot.get("Editable");
                                                                                    boolean compulsory = (boolean) documentSnapshot.get("Compulsory");
                                                                                    String ques= (String) documentSnapshot.get("Question");
                                                                                    long type=(long) documentSnapshot.get("Type");
                                                                                    currQ.setChangeable(editable);
                                                                                    currQ.setCompulsory(compulsory);
                                                                                    currQ.setType((int) type);
                                                                                    currQ.setQuestion(ques);
                                                                                    currQ.setId(finalI2);
                                                                                    DocumentReference ans= docUserInfo2.collection("Academic Question").document(finalI+"");
                                                                                    ans.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                            String ans= (String) documentSnapshot.get("Answer");
                                                                                            currQ.setAnswer(ans);
                                                                                            academicQ.add(currQ);
                                                                                            if(finalI1 ==total-1)
                                                                                            {
                                                                                                Collections.sort(academicQ,new Comparator<CollegeRegisterQuestions>() {
                                                                                                    @Override
                                                                                                    public int compare(CollegeRegisterQuestions o1,CollegeRegisterQuestions o2) {
                                                                                                        int i1 = (o1.getId() - (o2.getId()));
                                                                                                        return i1;
                                                                                                    }
                                                                                                });
                                                                                                studentData.setAcademic_ques(academicQ);
                                                                                                DocumentReference docPersQues = ff.collection("All Colleges").document(studentData.getCollegeid()).collection("Questions").document("Upload Question");
                                                                                                docPersQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                                        long total= (long) documentSnapshot.get("Total");
                                                                                                        studentData.setNoUploadQ(total);
                                                                                                        for (int i=0;i<(int)total;i++)
                                                                                                        {
                                                                                                            DocumentReference docCurrQues = docPersQues.collection(i + "").document(i + "");

                                                                                                            int finalI1 = i;
                                                                                                            int finalI2 = i;
                                                                                                            docCurrQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                                                    CollegeRegisterQuestions currQ=new CollegeRegisterQuestions();
                                                                                                                    boolean editable= (boolean) documentSnapshot.get("Editable");
                                                                                                                    boolean compulsory = (boolean) documentSnapshot.get("Compulsory");
                                                                                                                    String ques= (String) documentSnapshot.get("Question");
                                                                                                                    long type=(long) documentSnapshot.get("Type");
                                                                                                                    currQ.setChangeable(editable);
                                                                                                                    currQ.setCompulsory(compulsory);
                                                                                                                    currQ.setType((int) type);
                                                                                                                    currQ.setQuestion(ques);
                                                                                                                    currQ.setId(finalI2);
                                                                                                                    uploadQ.add(currQ);
                                                                                                                    if(finalI1 ==total-1)
                                                                                                                    {
                                                                                                                        Collections.sort(uploadQ,new Comparator<CollegeRegisterQuestions>() {
                                                                                                                            @Override
                                                                                                                            public int compare(CollegeRegisterQuestions o1,CollegeRegisterQuestions o2) {
                                                                                                                                int i1 = (o1.getId() - (o2.getId()));
                                                                                                                                return i1;
                                                                                                                            }
                                                                                                                        });
                                                                                                                        studentData.setUpload_ques(uploadQ);
                                                                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                                                                    }

                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    });

                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });

                    }

            }
        });
    }

    public void personalDetails(View v){
        if(studentData.getPersonal_ques()!=null)
            startActivity(new Intent(StudentPage.this, StudentPersonalDetails.class));
        else
            Toast.makeText(this,"Data Loading",Toast.LENGTH_LONG).show();
    }
    public void academicDetails(View v){
        if(studentData.getAcademic_ques()!=null)
            startActivity(new Intent(StudentPage.this, StudentAcademicDetails.class));
        else
            Toast.makeText(this,"Data Loading",Toast.LENGTH_LONG).show();
    }
    public void uploadedDetails(View v){
        if(studentData.getUpload_ques()!=null)
            startActivity(new Intent(StudentPage.this, StudentUploadDetails.class));
        else
            Toast.makeText(this,"Data Loading",Toast.LENGTH_LONG).show();
    }
    public void requestStatus(View v){
        startActivity(new Intent(StudentPage.this, StudentCheckRequestStatus.class));
    }
    public void viewNotifications(View v){
        startActivity(new Intent(StudentPage.this, ViewNotificationsByAll.class));
    }
    public void viewFaq(View v){
        startActivity(new Intent(StudentPage.this, StudentViewAllFAQ.class));
    }
    public void logout(View v)
    {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(studentData.getCollegeid());
        FirebaseMessaging.getInstance().unsubscribeFromTopic("Student"+studentData.getCollegeid());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(f.getCurrentUser().getEmail());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(studentData.getCollegeid()+"_"+studentData.getCourse());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(studentData.getCollegeid()+"_"+studentData.getCourse()+"_"+studentData.getBranch());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(studentData.getCollegeid()+"_"+studentData.getCourse()+"_"+studentData.getBranch()+"_"+studentData.getYr());
        f.signOut();
        Toast.makeText(this,"Logged Out",Toast.LENGTH_LONG).show();
        startActivity(new Intent(StudentPage.this,Login.class));
        finish();
    }

    @Override
    protected void onResume() {
        FirebaseFirestore ff=FirebaseFirestore.getInstance();
        DocumentReference docUserInfo = ff.collection("All Users On App").document(studentData.getEmail());
        docUserInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String cId= (String) documentSnapshot.get("College");
                studentData.setCollegeid(cId);
                DocumentReference docUserInfo1 = ff.collection("All Colleges").document(cId).collection("All Users").document(studentData.getEmail());
                docUserInfo1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String branch= (String) documentSnapshot.get("Branch");
                        String yr= (String) documentSnapshot.get("Year");
                        String course= (String) documentSnapshot.get("Course");
                        studentData.setBranch(branch);
                        studentData.setCourse(course);
                        studentData.setYr(yr);
                        DocumentReference docUserInfo2 = ff.collection("All Colleges").document(studentData.getCollegeid()).collection("UsersInfo").document(studentData.getEmail());
                        docUserInfo2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String name = (String) documentSnapshot.get("Name");
                                long noOfRequest= (long) documentSnapshot.get("Number of Requests");
                                String course= (String) documentSnapshot.get("Course");
                                String branch=(String)documentSnapshot.get("Branch");
                                String year=(String)documentSnapshot.get("Year");
                                studentData.setCourse(course);
                                studentData.setBranch(branch);
                                studentData.setYr(year);
                                studentData.setName(name);
                                studentData.setNoOfReq(noOfRequest);
                                heading.setText(name);
                                email.setText(studentData.getEmail());
                                reqStatus.setText(reqStatus.getText().toString().substring(0,25)+noOfRequest+" requests");
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference(cId).child("Photograph").child(studentData.getEmail());
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(StudentPage.this)
                                                .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .error(R.drawable.profile_pic)
                                                .placeholder(R.drawable.profile_pic)
                                                .into(profilepic);

                                    }
                                });

                                final ArrayList<CollegeRegisterQuestions> personalQ = new ArrayList<>();
                                final ArrayList<CollegeRegisterQuestions> academicQ = new ArrayList<>();
                                final ArrayList<CollegeRegisterQuestions> uploadQ = new ArrayList<>();
                                DocumentReference docPersQues = ff.collection("All Colleges").document(studentData.getCollegeid()).collection("Questions").document("Personal Question");
                                docPersQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        long total= (long) documentSnapshot.get("Total");
                                        studentData.setNoPersonalQ(total);
                                        for (int i=0;i<(int)total;i++)
                                        {
                                            DocumentReference docCurrQues = docPersQues.collection(i + "").document(i + "");
                                            int finalI = i;
                                            int finalI1 = i;
                                            int finalI2 = i;
                                            docCurrQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    CollegeRegisterQuestions currQ=new CollegeRegisterQuestions();
                                                    boolean editable= (boolean) documentSnapshot.get("Editable");
                                                    boolean compulsory = (boolean) documentSnapshot.get("Compulsory");
                                                    String ques= (String) documentSnapshot.get("Question");
                                                    long type=(long) documentSnapshot.get("Type");
                                                    currQ.setChangeable(editable);
                                                    currQ.setCompulsory(compulsory);
                                                    currQ.setType((int) type);
                                                    currQ.setQuestion(ques);
                                                    currQ.setId(finalI2);
                                                    currQ.setDomain(1);
                                                    DocumentReference ans= docUserInfo2.collection("Personal Question").document(finalI+"");
                                                    ans.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            String ans= (String) documentSnapshot.get("Answer");
                                                            currQ.setAnswer(ans);
                                                            personalQ.add(currQ);
                                                            if(personalQ.size() ==total)
                                                            {
                                                                Collections.sort(personalQ,new Comparator<CollegeRegisterQuestions>() {
                                                                    @Override
                                                                    public int compare(CollegeRegisterQuestions o1,CollegeRegisterQuestions o2) {
                                                                        int i1 = (o1.getId() - (o2.getId()));
                                                                        return i1;
                                                                    }
                                                                });
                                                                studentData.setPersonal_ques(personalQ);
                                                                DocumentReference docPersQues = ff.collection("All Colleges").document(studentData.getCollegeid()).collection("Questions").document("Academic Question");
                                                                docPersQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        long total= (long) documentSnapshot.get("Total");
                                                                        studentData.setNoPersonalQ(total);
                                                                        for (int i=0;i<(int)total;i++)
                                                                        {
                                                                            DocumentReference docCurrQues = docPersQues.collection(i + "").document(i + "");
                                                                            int finalI = i;
                                                                            int finalI1 = i;
                                                                            int finalI2 = i;
                                                                            docCurrQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                    CollegeRegisterQuestions currQ=new CollegeRegisterQuestions();
                                                                                    boolean editable= (boolean) documentSnapshot.get("Editable");
                                                                                    boolean compulsory = (boolean) documentSnapshot.get("Compulsory");
                                                                                    String ques= (String) documentSnapshot.get("Question");
                                                                                    long type=(long) documentSnapshot.get("Type");
                                                                                    currQ.setChangeable(editable);
                                                                                    currQ.setCompulsory(compulsory);
                                                                                    currQ.setType((int) type);
                                                                                    currQ.setQuestion(ques);
                                                                                    currQ.setId(finalI2);
                                                                                    currQ.setDomain(2);
                                                                                    DocumentReference ans= docUserInfo2.collection("Academic Question").document(finalI+"");
                                                                                    ans.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                        @Override
                                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                            String ans= (String) documentSnapshot.get("Answer");
                                                                                            currQ.setAnswer(ans);
                                                                                            academicQ.add(currQ);
                                                                                            if(academicQ.size() ==total)
                                                                                            {
                                                                                                Collections.sort(academicQ,new Comparator<CollegeRegisterQuestions>() {
                                                                                                    @Override
                                                                                                    public int compare(CollegeRegisterQuestions o1,CollegeRegisterQuestions o2) {
                                                                                                        int i1 = (o1.getId() - (o2.getId()));
                                                                                                        return i1;
                                                                                                    }
                                                                                                });
                                                                                                studentData.setAcademic_ques(academicQ);
                                                                                                DocumentReference docPersQues = ff.collection("All Colleges").document(studentData.getCollegeid()).collection("Questions").document("Upload Question");
                                                                                                docPersQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                                        long total= (long) documentSnapshot.get("Total");
                                                                                                        studentData.setNoUploadQ(total);
                                                                                                        for (int i=0;i<(int)total;i++)
                                                                                                        {
                                                                                                            DocumentReference docCurrQues = docPersQues.collection(i + "").document(i + "");

                                                                                                            int finalI1 = i;
                                                                                                            int finalI2 = i;
                                                                                                            docCurrQues.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                                                @Override
                                                                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                                                    CollegeRegisterQuestions currQ=new CollegeRegisterQuestions();
                                                                                                                    boolean editable= (boolean) documentSnapshot.get("Editable");
                                                                                                                    boolean compulsory = (boolean) documentSnapshot.get("Compulsory");
                                                                                                                    String ques= (String) documentSnapshot.get("Question");
                                                                                                                    long type=(long) documentSnapshot.get("Type");
                                                                                                                    currQ.setChangeable(editable);
                                                                                                                    currQ.setCompulsory(compulsory);
                                                                                                                    currQ.setType((int) type);
                                                                                                                    currQ.setQuestion(ques);
                                                                                                                    currQ.setId(finalI2);
                                                                                                                    currQ.setDomain(3);
                                                                                                                    uploadQ.add(currQ);
                                                                                                                    if(uploadQ.size() ==total)
                                                                                                                    {
                                                                                                                        Collections.sort(uploadQ,new Comparator<CollegeRegisterQuestions>() {
                                                                                                                            @Override
                                                                                                                            public int compare(CollegeRegisterQuestions o1,CollegeRegisterQuestions o2) {
                                                                                                                                int i1 = (o1.getId() - (o2.getId()));
                                                                                                                                return i1;
                                                                                                                            }
                                                                                                                        });
                                                                                                                        studentData.setUpload_ques(uploadQ);
                                                                                                                        progressBar.setVisibility(View.GONE);
                                                                                                                    }

                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                        }
                                                                                    });

                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        });

                    }
                });
            }
        });

        super.onResume();
    }
}