package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StudentSignup extends AppCompatActivity {
    Spinner collge;
    EditText email,password1,password2;
    ArrayList<String> listName,listId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_activity_signup);
        collge=findViewById(R.id.collegeName);
        email=findViewById(R.id.email);
        password1=findViewById(R.id.password);
        password2=findViewById(R.id.password2);
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference allcllgName = f.collection("All Users On App").document("All Colleges");
        allcllgName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listName=(ArrayList<String>) documentSnapshot.get("Colleges");
                listId=(ArrayList<String>) documentSnapshot.get("IDs");
                ArrayAdapter spinnerList=new ArrayAdapter(StudentSignup.this,android.R.layout.simple_spinner_item,listName);
                collge.setAdapter(spinnerList);
            }
        });
    }

    public void next(View v)
    {
        //get the college selected
        int i=collge.getSelectedItemPosition();
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference allcllgName = f.collection("All Users On App").document("All Colleges");
        allcllgName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listId=(ArrayList<String>) documentSnapshot.get("IDs");
                String collegeId=listId.get(i);
                DocumentReference allowedOrNot=f.collection("All Colleges").document(collegeId).collection("All Users").document(email.getText().toString());
                allowedOrNot.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists())
                        {
                                boolean exists= (boolean) documentSnapshot.get("New");
                                if(exists==true)
                                {
                                    //This is a new user
                                }
                                else
                                {
                                    //Toast that this user has already signed up
                                    //Intent to login.class
                                }
                        }
                        else
                        {
                            email.setError("This email is not allowed by admin");
                        }
                    }
                });


            }
        });
    }
}