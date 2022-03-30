package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdminSearchUser extends AppCompatActivity{
    static StudentData studentData;
    static CompanyData companyData;
    static AdminData adminData,adminDataSearched;
    static EditText email;
    Button search;
    static String category;
    TextView msg;
    LinearLayout ll;
    String cId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_user);
        email=findViewById(R.id.emailsearch);
        search=findViewById(R.id.search);
        msg=findViewById(R.id.msg);
        ll=findViewById(R.id.linearlay);
        adminData=AdminPage.adminData;
        studentData=StudentPage.studentData;
        companyData=CompanyPage.companyData;
        if(adminData!=null)
        {
            cId=adminData.getCollegeId();
        }
        else if (studentData!=null)
            cId=studentData.getCollegeid();
        else
            companyData.getCollegeId();
        if (StudentPage.studentData!=null) {
            search.setVisibility(View.INVISIBLE);
            email.setClickable(false);
            email.setEnabled(false);
        }
        if(getIntent().hasExtra("Email"))
        {
            email.setText(getIntent().getStringExtra("Email"));
            searchUser(new View(this));
        }
    }
    public void searchUser(View v) {

        ProgressBar pbar =findViewById(R.id.progressBar2);
        pbar.setVisibility(View.VISIBLE);
        search.setEnabled(false);
        msg.setVisibility(View.INVISIBLE);
        ll.removeAllViews();
        if(checkEmail())
        {
            checkUserType();
        }
    }

    public void checkUserType()  {
        String eMail=email.getText().toString().trim();
        final String[] category = new String[1];
        category[0]=null;

        DocumentReference userDetail = FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("UsersInfo").document(eMail);

        userDetail.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    category[0] = (String) documentSnapshot.get("Category");
                    AdminSearchUser.category=category[0];
                    if(category[0].equalsIgnoreCase("Admin"))
                    {
                        adminDataSearched=new AdminData();
                        String name= (String) documentSnapshot.get("Name");
                        String phone=(String) documentSnapshot.get("Phone Number");
                        adminDataSearched.setPhoneNumber(phone);
                        adminDataSearched.setAdminName(name);
                        adminDataSearched.setDeptName((String) documentSnapshot.get("Department"));
                        adminDataSearched.setEmail(eMail);
                        View photo=getLayoutInflater().inflate(R.layout.repeatable_photograph,null);
                        TextView ques=photo.findViewById(R.id.Ques);
                        ques.setVisibility(View.GONE);
                        Button edit=photo.findViewById(R.id.button5);
                        edit.setVisibility(View.GONE);
                        ImageView profilepic=photo.findViewById(R.id.imageView6);
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(cId).child("Photograph").child(eMail);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(AdminSearchUser.this)
                                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.drawable.admin_profile_img)
                                        .placeholder(R.drawable.default_loading_img)
                                        .into(profilepic);
                                ProgressBar pbar =findViewById(R.id.progressBar2);
                                pbar.setVisibility(View.GONE);
                                search.setEnabled(true);
                                msg.setVisibility(View.VISIBLE);
                            }
                        });
                        ll.addView(photo);
                        if(category[0].equalsIgnoreCase("Admin"))
                        {
                            String email=adminDataSearched.getEmail();
                            View email_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_email=email_.findViewById(R.id.Ques);
                            TextView ans_email=email_.findViewById(R.id.ans);
                            ques_email.setText("EMAIL");
                            ans_email.setText(email);
                            Linkify.addLinks(ans_email, Linkify.ALL);
                            ans_email.setLinkTextColor(Color.parseColor("#034ABC"));
                            ll.addView(email_);

                            View name_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_name=name_.findViewById(R.id.Ques);
                            TextView ans_name=name_.findViewById(R.id.ans);
                            ques_name.setText("NAME");
                            ans_name.setText(name);
                            ll.addView(name_);

                            String department=adminDataSearched.getDeptName();
                            View dept_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_dept=dept_.findViewById(R.id.Ques);
                            TextView ans_dept=dept_.findViewById(R.id.ans);
                            ques_dept.setText("DEPARTMENT");
                            ans_dept.setText(department);
                            ll.addView(dept_);

                            View phone_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_phone=phone_.findViewById(R.id.Ques);
                            TextView ans_phone=phone_.findViewById(R.id.ans);
                            ques_phone.setText("PHONE NUMBER");
                            ans_phone.setText(phone);
                            Linkify.addLinks(ans_phone, Linkify.ALL);
                            ans_phone.setLinkTextColor(Color.parseColor("#034ABC"));
                            ll.addView(phone_);
                        }

                    }
                    else if(category[0].equalsIgnoreCase("Student"))
                    {
                        studentData =new StudentData();
                        final ArrayList<CollegeRegisterQuestions> personalQ = new ArrayList<>();
                        final ArrayList<CollegeRegisterQuestions> academicQ = new ArrayList<>();
                        final ArrayList<CollegeRegisterQuestions> uploadQ = new ArrayList<>();
                        DocumentReference docPersQues = FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("Questions").document("Personal Question");
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
                                            DocumentReference ans= userDetail.collection("Personal Question").document(finalI+"");
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
                                                        DocumentReference docPersQues = FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("Questions").document("Academic Question");
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
                                                                            DocumentReference ans= userDetail.collection("Academic Question").document(finalI+"");
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
                                                                                        DocumentReference docPersQues = FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("Questions").document("Upload Question");
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
                                                                                                                if(category[0].equalsIgnoreCase("Student"))
                                                                                                                {
                                                                                                                    View photo=getLayoutInflater().inflate(R.layout.repeatable_photograph,null);
                                                                                                                    TextView ques1=photo.findViewById(R.id.Ques);
                                                                                                                    ques1.setVisibility(View.GONE);
                                                                                                                    ImageView profilepic=photo.findViewById(R.id.imageView6);
                                                                                                                    Button edit=photo.findViewById(R.id.button5);
                                                                                                                    edit.setVisibility(View.GONE);
                                                                                                                    if(eMail==null)
                                                                                                                    {
                                                                                                                        profilepic.setImageResource(R.drawable.profile_pic);
                                                                                                                    }
                                                                                                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(cId).child("Photograph").child(eMail);
                                                                                                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                                        @Override
                                                                                                                        public void onSuccess(Uri uri) {
                                                                                                                            Glide.with(getApplicationContext())
                                                                                                                                    .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                                                                                                                    .error(R.drawable.profile_pic)
                                                                                                                                    .placeholder(R.drawable.default_loading_img)
                                                                                                                                    .into(profilepic);
                                                                                                                            ProgressBar pbar =findViewById(R.id.progressBar2);
                                                                                                                            pbar.setVisibility(View.GONE);
                                                                                                                            search.setEnabled(true);
                                                                                                                            msg.setVisibility(View.VISIBLE);
                                                                                                                        }
                                                                                                                    });
                                                                                                                    ll.addView(photo);

                                                                                                                    View repeatAnswer = getLayoutInflater().inflate(R.layout.repeatable_student_details, null);
                                                                                                                    TextView ques00 = repeatAnswer.findViewById(R.id.Ques);
                                                                                                                    TextView ans00= repeatAnswer.findViewById(R.id.ans);
                                                                                                                    ques00.setText("Registered Email");
                                                                                                                    ans00.setText(email.getText().toString());
                                                                                                                    Linkify.addLinks(ans00, Linkify.ALL);
                                                                                                                    ans00.setLinkTextColor(Color.parseColor("#034ABC"));

                                                                                                                    ll.addView(repeatAnswer);

                                                                                                                    ArrayList<CollegeRegisterQuestions> quesAns_personal = studentData.getPersonal_ques();
                                                                                                                    for(CollegeRegisterQuestions a:quesAns_personal) {
                                                                                                                        View repeatAnswers = getLayoutInflater().inflate(R.layout.repeatable_student_details, null);
                                                                                                                        TextView ques11 = repeatAnswers.findViewById(R.id.Ques);
                                                                                                                        TextView ans = repeatAnswers.findViewById(R.id.ans);
                                                                                                                        ques11.setText(a.getQuestion());
                                                                                                                        ans.setText(a.getAnswer());
                                                                                                                        ll.addView(repeatAnswers);
                                                                                                                    }
                                                                                                                    ArrayList<CollegeRegisterQuestions> quesAns_academic = studentData.getAcademic_ques();
                                                                                                                    for(CollegeRegisterQuestions a:quesAns_academic) {
                                                                                                                        View repeatAnswers = getLayoutInflater().inflate(R.layout.repeatable_student_details, null);
                                                                                                                        TextView ques11 = repeatAnswers.findViewById(R.id.Ques);
                                                                                                                        TextView ans = repeatAnswers.findViewById(R.id.ans);
                                                                                                                        ques11.setText(a.getQuestion());
                                                                                                                        ans.setText(a.getAnswer());
                                                                                                                        ll.addView(repeatAnswers);
                                                                                                                    }
                                                                                                                    ArrayList<CollegeRegisterQuestions> quesAns = studentData.getUpload_ques();
                                                                                                                    for(CollegeRegisterQuestions a:quesAns)
                                                                                                                    {
                                                                                                                        if(a.getQuestion().equalsIgnoreCase("Photograph"))
                                                                                                                        {
                                                                                                                            continue;
                                                                                                                        }
                                                                                                                        else
                                                                                                                        {
                                                                                                                            View repeatAnswers=getLayoutInflater().inflate(R.layout.repeatable_student_uploaded_documents,null);
                                                                                                                            TextView ques11=repeatAnswers.findViewById(R.id.Ques);
                                                                                                                            Button view =repeatAnswers.findViewById(R.id.view);
                                                                                                                            Button update= repeatAnswers.findViewById(R.id.update);
                                                                                                                            Button download=repeatAnswers.findViewById(R.id.download);
                                                                                                                            update.setVisibility(View.GONE);
                                                                                                                            ques11.setText(a.getQuestion());
                                                                                                                            view.setOnClickListener(new View.OnClickListener() {
                                                                                                                                @Override
                                                                                                                                public void onClick(View view) {
                                                                                                                                   FirebaseStorage storage = FirebaseStorage.getInstance();
                                                                                                                                    StorageReference storageRef = storage.getReference(cId).child(a.getQuestion()).child(eMail);
                                                                                                                                    Task<Uri> message = storageRef.getDownloadUrl();
                                                                                                                                    message.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                                                        @Override
                                                                                                                                        public void onSuccess(Uri uri) {
                                                                                                                                            // Toast.makeText(getActivity(),uri.toString(),Toast.LENGTH_SHORT).show();
                                                                                                                                            Intent intent = new Intent(view.getContext(), ViewPDFActivity.class);
                                                                                                                                            intent.putExtra("url", uri.toString());
                                                                                                                                            startActivity(intent);
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                            });
                                                                                                                           
                                                                                                                            download.setOnClickListener(new View.OnClickListener() {
                                                                                                                                @Override
                                                                                                                                public void onClick(View view) {
                                                                                                                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                                                                                                                    StorageReference storageRef = storage.getReference(studentData.getCollegeid()).child(a.getQuestion()).child(studentData.getEmail());
                                                                                                                                    Task<Uri> message = storageRef.getDownloadUrl();
                                                                                                                                    message.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                                                                                        @Override
                                                                                                                                        public void onSuccess(Uri uri) {
                                                                                                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                                                                                                            startActivity(intent);
                                                                                                                                        }
                                                                                                                                    });
                                                                                                                                }
                                                                                                                            });

                                                                                                                            ll.addView(repeatAnswers);
                                                                                                                        }


                                                                                                                    }

                                                                                                                }
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
                    else if (category[0].equalsIgnoreCase("Company"))
                    {
                        View photo=getLayoutInflater().inflate(R.layout.repeatable_photograph,null);
                        TextView ques=photo.findViewById(R.id.Ques);
                        ques.setVisibility(View.GONE);
                        Button edit=photo.findViewById(R.id.button5);
                        edit.setVisibility(View.GONE);
                        ImageView profilepic=photo.findViewById(R.id.imageView6);
                        StorageReference storageRef= FirebaseStorage.getInstance().getReference(cId).child("Photograph").child(eMail);
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(AdminSearchUser.this)
                                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.drawable.company_profile_img)
                                        .placeholder(R.drawable.default_loading_img)
                                        .into(profilepic);

                                ProgressBar pbar =findViewById(R.id.progressBar2);
                                pbar.setVisibility(View.GONE);
                                search.setEnabled(true);
                                msg.setVisibility(View.VISIBLE);
                            }
                        });
                        ll.addView(photo);
                        companyData=new CompanyData();
                        companyData.setEmail(email.getText().toString());
                        companyData.setPersonalEmail((String) documentSnapshot.get("Personal Email"));
                        companyData.setLocation((String) documentSnapshot.get("Company Location"));
                        companyData.setCompanyName((String) documentSnapshot.get("Company Name"));
                        companyData.setName((String) documentSnapshot.get("Name"));
                        companyData.setPhone((String) documentSnapshot.get("Phone Number"));
                        if(category[0].equalsIgnoreCase("Company"))
                        {
                            String companyEmail=companyData.getEmail();
                            View companyEmail_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_email=companyEmail_.findViewById(R.id.Ques);
                            TextView ans_email=companyEmail_.findViewById(R.id.ans);
                            ques_email.setText("COMPANY EMAIL");
                            ans_email.setText(companyEmail);
                            Linkify.addLinks(ans_email, Linkify.ALL);
                            ans_email.setLinkTextColor(Color.parseColor("#034ABC"));
                            ll.addView(companyEmail_);

                            String name=companyData.getCompanyName();
                            View name_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_name=name_.findViewById(R.id.Ques);
                            TextView ans_name=name_.findViewById(R.id.ans);
                            ques_name.setText("COMPANY NAME");
                            ans_name.setText(name);
                            ll.addView(name_);

                            String location=companyData.getLocation();
                            View location_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_location=location_.findViewById(R.id.Ques);
                            TextView ans_location=location_.findViewById(R.id.ans);
                            ques_location.setText("COMPANY LOCATION");
                            ans_location.setText(location);
                            ll.addView(location_);

                            String rname=companyData.getName();
                            View rname_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_rname=rname_.findViewById(R.id.Ques);
                            TextView ans_rname=rname_.findViewById(R.id.ans);
                            ques_rname.setText("REPRESENTATIVE NAME");
                            ans_rname.setText(rname);
                            ll.addView(rname_);

                            String remail=companyData.getPersonalEmail();
                            View remail_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_remail=remail_.findViewById(R.id.Ques);
                            TextView ans_remail=remail_.findViewById(R.id.ans);
                            ques_remail.setText("REPRESENTATIVE EMAIL");
                            ans_remail.setText(remail);
                            Linkify.addLinks(ans_remail, Linkify.ALL);
                            ans_remail.setLinkTextColor(Color.parseColor("#034ABC"));
                            ll.addView(remail_);

                            String rphone=companyData.getPhone();
                            View rphone_=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
                            TextView ques_rphone=rphone_.findViewById(R.id.Ques);
                            TextView ans_rphone=rphone_.findViewById(R.id.ans);
                            ques_rphone.setText("REPRESENTATIVE PHONE NUMBER");
                            ans_rphone.setText(rphone);
                            Linkify.addLinks(ans_rphone, Linkify.ALL);
                            ans_rphone.setLinkTextColor(Color.parseColor("#034ABC"));
                            ll.addView(rphone_);

                        }
                    }
                    else
                    {
                        category[0]="None";

                    }
                }
                else
                {
                    email.setError("This user does not exists");
                    email.requestFocus();
                    ProgressBar pbar =findViewById(R.id.progressBar2);
                    pbar.setVisibility(View.INVISIBLE);
                    search.setEnabled(true);
                    AdminSearchUser.category="None";
                    category[0]="None";
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AdminSearchUser.category="None";
                Toast.makeText(AdminSearchUser.this,e.toString(),Toast.LENGTH_LONG).show();
                category[0]="None";
                ProgressBar pbar =findViewById(R.id.progressBar2);
                pbar.setVisibility(View.INVISIBLE);
                search.setEnabled(true);
            }
        });
    }

    public boolean checkEmail() {
        //check if not empty
        if (email.getText().toString().length() == 0) {
            email.setError("ENTER EMAIL");
            email.requestFocus();
            ProgressBar pbar =findViewById(R.id.progressBar2);
            pbar.setVisibility(View.INVISIBLE);
            search.setEnabled(true);
            return false;
        }
        //check if valid
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email1 = email.getText().toString().trim();
        if (!email1.matches(emailPattern)) {
            email.setError("ENTER VALID EMAIL");
            email.requestFocus();
            ProgressBar pbar =findViewById(R.id.progressBar2);
            pbar.setVisibility(View.INVISIBLE);
            search.setEnabled(true);
            return false;
        }
        return true;
    }
}