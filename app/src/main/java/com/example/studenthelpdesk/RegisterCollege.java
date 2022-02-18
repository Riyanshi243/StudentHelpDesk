package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterCollege extends AppCompatActivity {
    //this instance will be referred to untill registration is completed
    static CollegeRegistrationData allData;
    EditText uname,cname,password,password2,adminmail,location;
    private Button register;
    FirebaseFirestore firebaseFirestores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college);
        allData=new CollegeRegistrationData();
        uname=findViewById(R.id.uname);
        cname=findViewById(R.id.cname);
        cname.requestFocus();
        location=findViewById(R.id.clocation);
        adminmail=findViewById(R.id.adminemail);
        password=findViewById(R.id.password2);
        password2=findViewById(R.id.confirmpassword2);
         register= (Button) findViewById(R.id.register);
        firebaseFirestores=FirebaseFirestore.getInstance();

    }
    //move to next step of registration
    public void next(View v)
    {
        register.setEnabled(false);
        ProgressBar pbar = findViewById(R.id.progressBar_college);
        pbar.setVisibility(View.VISIBLE);
        if(checkConstraints())
        {
            DocumentReference documentReference = firebaseFirestores.collection("All Colleges").document(uname.getText().toString());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //if username exist... a document will already be present in its name
                    if(documentSnapshot.exists())
                    {
                        uname.setError("THIS USERNAME ALREADY EXISTS");
                    }
                    else
                    {
                        allData.setSAdminemail(adminmail.getText().toString());
                        allData.setPassword(password.getText().toString());
                        allData.setUname(uname.getText().toString());
                        allData.setCName(cname.getText().toString());
                        allData.setLocation(location.getText().toString());
                        //intent to college registration step 2
                        startActivity(new Intent(RegisterCollege.this, RegisterCollege2.class));

                    }
                }
            });

        }
    }
    public void toLogin(View v)
    {
        //Intent to login
        startActivity(new Intent(RegisterCollege.this,Login.class));
        finish();
    }

    public Boolean checkConstraints()
    {
        register.setEnabled(false);
        ProgressBar pbar =findViewById(R.id.progressBar_college);
        pbar.setVisibility(View.VISIBLE);
       // final Boolean[] flag = {true};
        //everything not empty


        if(cname.getText().toString().length()==0)
        {
            cname.setError("ENTER COLLEGE NAME");
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
             return false;
        }

        if(uname.getText().toString().length()==0)
        {
            uname.setError("ENTER USERNAME");
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }
        if(location.getText().toString().length()==0)
        {
            location.setError("ENTER LOCATION");
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }
        if(adminmail.getText().toString().length()==0)
        {
            adminmail.setError("ENTER EMAIL");
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }
        if(password.getText().toString().length()==0)
        {
            password.setError("ENTER PASSWORD");
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
             return false;
        }
        if(password2.getText().toString().length()==0)
        {
            password2.setError("ENTER CONFIRM PASSWORD");
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
             return false;
        }


        //check if valid mail
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email=adminmail.getText().toString().trim();
        if(!email.matches(emailPattern))
        {
            adminmail.setError("ENTER VALID MAIL");
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
             return false;
        }
       
                    //check if passwords match
        if(password.getText().toString().equals(password2.getText().toString())==false)
        {
            password2.setError("PASSWORDS DO NOT MATCH");
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
             return false;
        }
        //check if password length greater than 8
        if(password.getText().toString().length()< 8)
        {
            password.setError("ENTER ATLEAST 8 CHARACTERS");
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
             return false;
        }
     //   return flag[0];
        return true;
    }
}