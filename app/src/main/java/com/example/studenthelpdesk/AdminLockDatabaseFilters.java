package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminLockDatabaseFilters extends AppCompatActivity {
    LinearLayout ll;
    AdminData adminData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_lock_database_filters);
        ll=findViewById(R.id.linearlay);
        adminData=AdminPage.adminData;
        DocumentReference courseDoc = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId());
                courseDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> courses = (ArrayList<String>) documentSnapshot.get("Courses");
                        for (String c : courses) {
                            DocumentReference branchDoc = courseDoc.collection("Branches").document(c);
                            branchDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    ArrayList<String>  branch= (ArrayList<String>) documentSnapshot.get("Branches");
                                    View courseView=getLayoutInflater().inflate(R.layout.repeatable_database_filters,null);
                                    TextView courseName=courseView.findViewById(R.id.CourseName);
                                    courseName.setText(c);
                                    Button downArrow=courseView.findViewById(R.id.downArrow);
                                    LinearLayout linearLayout=courseView.findViewById(R.id.linearlay);
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
                                    for(String b:branch)
                                    {
                                        View branchView=getLayoutInflater().inflate(R.layout.repeatable_database_filters2,null);
                                        TextView branchName=branchView.findViewById(R.id.branchName);
                                        branchName.setText(b);
                                        linearLayout.addView(branchView);

                                    }
                                    ll.addView(courseView);
                                }
                            });
                        }
                    }
                });
    }
}