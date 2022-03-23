package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentPostFAQ extends AppCompatActivity {
    static StudentData studentData;
    EditText FAQ_content,hastag;
    CheckBox anonymous;
    AutoCompleteTextView concernedAdmin;
    Button postFAQ;
    String cId,selectedAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_post_faq);
        FAQ_content=findViewById(R.id.FAQ_content);
        concernedAdmin=findViewById(R.id.concerned_admin);
        hastag=findViewById(R.id.hashtags);
        anonymous=findViewById(R.id.anonymous);
        postFAQ=(Button) findViewById(R.id.post);
        studentData=StudentPage.studentData;

        cId=studentData.getCollegeid();
        ArrayList<String> concernedAdmins=new ArrayList<>();
        CollectionReference allAdmins = FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("UsersInfo");
        allAdmins.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> adminDetail = queryDocumentSnapshots.getDocuments();
                for (int i=0;i<adminDetail.size();i++) {
                    DocumentSnapshot d=adminDetail.get(i);
                    String category= (String) d.get("Category");
                    String name,dept;
                    if(category.equalsIgnoreCase("Admin"))
                    {
                        name=(String) d.get("Name");
                        dept=(String) d.get("Department");
                        concernedAdmins.add(name+", "+dept);
                        ArrayAdapter spinnerList = new ArrayAdapter(StudentPostFAQ.this, android.R.layout.simple_spinner_item, concernedAdmins);
                        if(i==adminDetail.size()-1)
                            concernedAdmin.setAdapter(spinnerList);
                    }
                }
                concernedAdmin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //selectedAdmin=
                    }
                });
            }
        });
    }
    public void postFAQ(View v)
    {
        if(nonEmpty())
        {

        }
    }
    public boolean nonEmpty()
    {
        if (FAQ_content.getText().toString().length() == 0) {
            FAQ_content.setError("Enter Content");
            FAQ_content.requestFocus();
            ProgressBar pbar =findViewById(R.id.progressBar5);
            pbar.setVisibility(View.INVISIBLE);
            postFAQ.setEnabled(true);
            return false;
        }
        if(concernedAdmin.getText().toString().equalsIgnoreCase("Choose concerned ADMIN"))
        {
            concernedAdmin.setError("Select the concerned admin");
            return false;
        }
        return true;
    }

}