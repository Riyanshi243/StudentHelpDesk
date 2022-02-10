package com.example.studenthelpdesk;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Signup extends AppCompatActivity {
    AutoCompleteTextView collge;
    EditText email, password1, password2;
    ArrayList<String> listName, listId;
    static AdminData adminData;
    static StudentData studentData;
    static CompanyData companyData;
    int cnumber;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminData = new AdminData();
        studentData = new StudentData();
        companyData = new CompanyData();
        setContentView(R.layout.activity_signup);
        collge = findViewById(R.id.collegeName);
        email = findViewById(R.id.email);
        password1 = findViewById(R.id.password);
        password2 = findViewById(R.id.confirmpassword);
        signup = (Button) findViewById(R.id.signup);

        FirebaseFirestore f = FirebaseFirestore.getInstance();
        DocumentReference allcllgName = f.collection("All Users On App").document("All Colleges");
        allcllgName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listName = (ArrayList<String>) documentSnapshot.get("Colleges");
                listId = (ArrayList<String>) documentSnapshot.get("IDs");
                ArrayAdapter spinnerList = new ArrayAdapter(Signup.this, android.R.layout.simple_spinner_item, listName);
                collge.setAdapter(spinnerList);
                collge.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        cnumber = arg2;
                    }
                });
            }
        });
    }


    