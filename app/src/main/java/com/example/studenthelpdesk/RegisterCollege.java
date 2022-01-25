package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterCollege extends AppCompatActivity {
    //this instance will be referred to untill registration is completed
    static CollegeRegistrationData allData;
    EditText uname,cname,password,password2,adminmail,location;
    FirebaseFirestore firebaseFirestores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college);
        allData=new CollegeRegistrationData();
        uname=findViewById(R.id.uname);
        cname=findViewById(R.id.cname);
        location=findViewById(R.id.clocation);
        adminmail=findViewById(R.id.adminemail);
        password=findViewById(R.id.password2);
        password2=findViewById(R.id.confirmpassword2);
        firebaseFirestores=FirebaseFirestore.getInstance();

    }
    //move to next step of registration
    public void next(View v)
    {
        if(checkConstraints())
        {
            DocumentReference documentReference = firebaseFirestores.collection("All Colleges").document(uname.getText().toString());
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //if username exist... a document will already be present in its name
                    if(documentSnapshot.exists())
                    {
                        uname.setError("This username already Exist");
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
                        finish();
                    }
                }
            });

        }
    }
    public void toLogin(View v)
    {
        //Intent to login
        startActivity(new Intent(RegisterCollege.this,Login.class));
    }

    public Boolean checkConstraints()
    {
        final Boolean[] flag = {true};
        //everything not empty
        if(uname.getText().toString().length()==0)
        {
            uname.setError("Enter Username");
             return false;
        }

        if(cname.getText().toString().length()==0)
        {
            cname.setError("Enter College Name");
             return false;
        }
        if(password.getText().toString().length()==0)
        {
            password.setError("Enter Password");
             return false;
        }
        if(password2.getText().toString().length()==0)
        {
            password2.setError("Enter Confirm Password");
             return false;
        }
        if(adminmail.getText().toString().length()==0)
        {
            adminmail.setError("Enter Mail");
             return false;
        }
         if(location.getText().toString().length()==0)
        {
            location.setError("Enter Location");
             return false;
        }
        //check if valid mail
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email=adminmail.getText().toString().trim();
        if(!email.matches(emailPattern))
        {
            adminmail.setError("Enter valid mail");
             return false;
        }
       
                    //check if passwords match
        if(password.getText().toString().equals(password2.getText().toString())==false)
        {
            password2.setError("Enter correct password");
             return false;
        }
        //check if password length greater than 8
        if(password.getText().toString().length()< 8)
        {
            password.setError("Enter atleast 8 characters");
             return false;
        }


        return flag[0];
    }
}