package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;

public class AdminLockDatabaseFilters extends AppCompatActivity {
    LinearLayout ll;
    ProgressBar progressBar;
    AdminData adminData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lock_database_filters);
        ll=findViewById(R.id.linearlay);
        adminData=AdminPage.adminData;
        progressBar=findViewById(R.id.progressBar5);
        DocumentReference courseDoc = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId());
                courseDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> courses = (ArrayList<String>) documentSnapshot.get("Courses");
                        int k=0;
                        for (String c : courses) {
                                DocumentReference thisCourse=courseDoc.collection("Lock").document(c);
                                thisCourse.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        HashMap<String,Object> lockData= (HashMap<String, Object>) documentSnapshot.getData();
                                        View courseView=getLayoutInflater().inflate(R.layout.repeatable_database_filters,null);
                                        TextView courseName=courseView.findViewById(R.id.CourseName);
                                        courseName.setText(c);
                                        Button downArrow=courseView.findViewById(R.id.downArrow);
                                        LinearLayout linearLayout=courseView.findViewById(R.id.linearlay);
                                        Switch lockcourse=courseView.findViewById(R.id.lock);
                                        linearLayout.setVisibility(View.GONE);
                                        downArrow.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(linearLayout.getVisibility()==View.VISIBLE)
                                                    linearLayout.setVisibility(View.GONE);
                                                else
                                                    linearLayout.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        final int[] totalLocks = {0};
                                        for(String i:lockData.keySet())
                                        {
                                            Boolean lock= (Boolean) lockData.get(i);
                                            View branchView=getLayoutInflater().inflate(R.layout.repeatable_database_filters2,null);
                                            TextView branchName=branchView.findViewById(R.id.branchName);
                                            branchName.setText(i);
                                            Switch lockbranch=branchView.findViewById(R.id.switch1);
                                            lockbranch.setChecked(lock);
                                            lockbranch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                    if(b==true)
                                                        totalLocks[0]++;
                                                    else
                                                        totalLocks[0]--;
                                                    if(totalLocks[0]==lockData.size())
                                                        lockcourse.setChecked(b);
                                                    if(totalLocks[0]==0)
                                                        lockcourse.setChecked(b);
                                                }
                                            });
                                            linearLayout.addView(branchView);
                                            if(lock==true)
                                                totalLocks[0]++;
                                        }
                                        if(totalLocks[0] ==lockData.size())
                                        {
                                            lockcourse.setChecked(true);
                                        }
                                        lockcourse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                                int t=linearLayout.getChildCount();
                                                    for(int i=0;i<t;i++)
                                                    {
                                                        View v=linearLayout.getChildAt(i);
                                                        Switch lockbranch=v.findViewById(R.id.switch1);

                                                        lockbranch.setChecked(b);
                                                    }

                                            }
                                        });
                                        ll.addView(courseView);

                                    }
                                });
                                k++;
                                if(k>=courses.size())
                                {
                                    progressBar.setVisibility(View.GONE);
                                }
                             }
                    }
                });
    }
    public void saveLock(View v)
    {
        int totalCourse=ll.getChildCount();
        for(int i=0;i<totalCourse;i++)
        {
            View courseView = ll.getChildAt(i);
            TextView courseName=courseView.findViewById(R.id.CourseName);
            String course=courseName.getText().toString();
            LinearLayout linearLayout=courseView.findViewById(R.id.linearlay);
            int totalBranch=linearLayout.getChildCount();
            for(int j=0;j<totalBranch;j++)
            {
                View branchView=linearLayout.getChildAt(j);
                TextView branchName=branchView.findViewById(R.id.branchName);
                String branch=branchName.getText().toString();
                Switch lockbranch=branchView.findViewById(R.id.switch1);
                boolean lock=lockbranch.isChecked();
                HashMap<String,Object> branchInfo=new HashMap<>();
                branchInfo.put(branch,lock);
                DocumentReference thisBranch=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Lock").document(course);
                thisBranch.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       boolean thislock= (boolean) documentSnapshot.get(branch);
                       if(lock==thislock)
                           return;
                        thisBranch.update(branchInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(AdminLockDatabaseFilters.this,course+" "+branch+" "+lock,Toast.LENGTH_LONG).show();


                                if(lock==true)
                                {
                                    Toast.makeText(AdminLockDatabaseFilters.this,course+" "+branch+" Locked",Toast.LENGTH_LONG).show();
                                    Log.e("hi",course+" "+branch);
                                    String token="/topics/"+adminData.getCollegeId()+"_"+course+"_"+branch;
                                    FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Your database has been locked","You cannot edit now",AdminLockDatabaseFilters.this,"DatabaseLock");
                                    notificationsSender.SendNotifications();

                                }
                                else
                                {
                                    Toast.makeText(AdminLockDatabaseFilters.this,course+" "+branch+" Unlocked",Toast.LENGTH_LONG).show();

                                    String token="/topics/"+adminData.getCollegeId()+"_"+course+"_"+branch;
                                    FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Your database has been unlocked","You can edit now",AdminLockDatabaseFilters.this,"DatabaseLock");
                                    notificationsSender.SendNotifications();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminLockDatabaseFilters.this,"ERROR:"+e.getMessage(),Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });

            }
        }
    }
}
