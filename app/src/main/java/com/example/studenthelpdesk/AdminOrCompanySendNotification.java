package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminOrCompanySendNotification extends AppCompatActivity {

    TextView title,content;
    AutoCompleteTextView audience;
    String token="",cId;
    AdminData adminData;
    CompanyData companyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_or_company_send_notification);
        title=findViewById(R.id.notif_title);
        content=findViewById(R.id.notif_content);
        audience=findViewById(R.id.audience);
        adminData=AdminPage.adminData;
        companyData=CompanyPage.companyData;
        cId="all";
        if (adminData!=null)
            cId=adminData.getCollegeId();
        else
            cId=companyData.getCollegeId();

        ArrayList<String> audienceChoice=new ArrayList<>();
        audienceChoice.add("all");
        audienceChoice.add("student");
        audienceChoice.add("admin");
        audienceChoice.add("company");
        ArrayAdapter spinnerListAudience = new ArrayAdapter(this, android.R.layout.simple_spinner_item, audienceChoice);
        audience.setAdapter(spinnerListAudience);
        String finalCId = cId;
        audience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    token=finalCId;

                    LinearLayout add1=findViewById(R.id.add);
                    add1.removeAllViews();
                }
                else if(i==1)
                {

                    LinearLayout add1=findViewById(R.id.add);
                    add1.removeAllViews();
                    Spinner studentAudience=new Spinner(AdminOrCompanySendNotification.this);
                    LinearLayout ll2=new LinearLayout(AdminOrCompanySendNotification.this);
                    ll2.setOrientation(LinearLayout.HORIZONTAL);
                    DocumentReference allBranches=FirebaseFirestore.getInstance().collection("All Colleges").document(cId);
                    allBranches.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String> courses = (ArrayList<String>) documentSnapshot.get("Courses");
                            courses.add(0,"all");
                            ArrayAdapter spinnerListCourse = new ArrayAdapter(AdminOrCompanySendNotification.this, android.R.layout.simple_spinner_item, courses);
                            studentAudience.setAdapter(spinnerListCourse);
                            studentAudience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    if(i==0)
                                    {
                                        token="student"+cId;
                                        ll2.removeAllViews();
                                    }
                                    else {
                                        ll2.removeAllViews();
                                        String selectedCourse = courses.get(i);
                                        token=cId+"_"+selectedCourse;
                                        Log.e("Course", selectedCourse);
                                        DocumentReference branch = FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("Branches").document(selectedCourse);
                                        branch.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                ArrayList<String> allbranch = (ArrayList<String>) documentSnapshot.get("Branches");
                                                allbranch.add(0, "all");
                                                ArrayAdapter spinnerListBranch = new ArrayAdapter(AdminOrCompanySendNotification.this, android.R.layout.simple_spinner_item, allbranch);
                                                Spinner branches = new Spinner(AdminOrCompanySendNotification.this);
                                                branches.setAdapter(spinnerListBranch);
                                                LinearLayout ll3=new LinearLayout(AdminOrCompanySendNotification.this);
                                                ll3.setOrientation(LinearLayout.HORIZONTAL);
                                                branches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        if(i==0)
                                                        {
                                                            token=cId+"_"+selectedCourse;
                                                            ll3.removeAllViews();
                                                        }
                                                        else
                                                        {
                                                            String selectedBranch=allbranch.get(i);
                                                            ll3.removeAllViews();
                                                            ArrayList<String> yr=new ArrayList<>();
                                                            yr.add("all");
                                                            yr.add("1");
                                                            yr.add("2");
                                                            yr.add("3");
                                                            yr.add("4");
                                                            yr.add("5");
                                                            yr.add("5+");
                                                            ArrayAdapter spinnerListYear = new ArrayAdapter(AdminOrCompanySendNotification.this, android.R.layout.simple_spinner_item,yr);
                                                            Spinner yrS=new Spinner(AdminOrCompanySendNotification.this);
                                                            yrS.setAdapter(spinnerListYear);
                                                            yrS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                @Override
                                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                    if(i==0)
                                                                    {
                                                                     token=cId+"_"+selectedCourse+"_"+selectedBranch;
                                                                    }
                                                                    else
                                                                    {
                                                                        Log.e("Year","hi:"+yr.get(i));
                                                                        token=cId+"_"+selectedCourse+"_"+selectedBranch+"_"+yr.get(i);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onNothingSelected(AdapterView<?> adapterView) {
                                                                    token=cId+"_"+selectedCourse+"_"+selectedBranch;
                                                                }
                                                            });
                                                            ll3.addView(yrS);
                                                        }
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                        token=cId+"_"+selectedCourse;
                                                    }
                                                });
                                                ll2.addView(branches);
                                                ll2.addView(ll3);

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    token="student"+cId;
                                }
                            });
                        }
                    });
                    add1.addView(studentAudience);
                    add1.addView(ll2);

                }
                else if(i==2)
                {
                    LinearLayout add1=findViewById(R.id.add);
                    add1.removeAllViews();
                    Spinner adminAudience=new Spinner(AdminOrCompanySendNotification.this);
                    DocumentReference allDepts=FirebaseFirestore.getInstance().collection("All Colleges").document(cId);
                    allDepts.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String> dept = (ArrayList<String>) documentSnapshot.get("Departments");
                            dept.add(0,"all");
                            ArrayAdapter spinnerListDept=new ArrayAdapter(AdminOrCompanySendNotification.this, android.R.layout.simple_spinner_item,dept);
                            adminAudience.setAdapter(spinnerListDept);
                            adminAudience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if(i==0)
                                        token="admin_"+cId;
                                    else
                                        token=cId+"_"+dept.get(i).replaceAll("\\s", "");
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    token="admin_"+cId;
                                }
                            });
                        }
                    });
                    add1.addView(adminAudience);
                }
                else if(i==3)
                {
                    token="company"+cId;
                }

            }
        });


    }
    public void sendNotif(View v)
    {
        String t1=token;
        token="/topics/"+token;
        FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,title.getText().toString(),content.getText().toString(),getApplicationContext(),this);
        notificationsSender.SendNotifications();
        token=t1;
    }
}