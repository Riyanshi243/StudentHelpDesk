package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Signup extends AppCompatActivity {
    Spinner collge;
    EditText email,password1,password2;
    ArrayList<String> listName,listId;
    static AdminData adminData;
    static StudentData studentData;
    static CompanyData companyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminData=new AdminData();
        studentData=new StudentData();
        companyData=new CompanyData();
        setContentView(R.layout.activity_signup);
        collge=findViewById(R.id.collegeName);
        email=findViewById(R.id.email);
        password1=findViewById(R.id.password);
        password2=findViewById(R.id.confirmpassword);
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference allcllgName = f.collection("All Users On App").document("All Colleges");
        allcllgName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listName=(ArrayList<String>) documentSnapshot.get("Colleges");
                listId=(ArrayList<String>) documentSnapshot.get("IDs");
                ArrayAdapter spinnerList=new ArrayAdapter(Signup.this,android.R.layout.simple_spinner_item,listName);
                collge.setAdapter(spinnerList);
            }
        });
    }

    public void next(View v)
    {
        //get the college selected
        int i=collge.getSelectedItemPosition();
        if(allDataFilled()==false)
        {
            return;
        }
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference allcllgName = f.collection("All Users On App").document("All Colleges");
        allcllgName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listId=(ArrayList<String>) documentSnapshot.get("IDs");
                String collegeId=listId.get(i);
                Log.e("id",collegeId);
                DocumentReference userDetails = f.collection("All Users On App").document(email.getText().toString());
                userDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists())
                        {
                                String collgeid=(String)documentSnapshot.get("College");
                                if(collgeid.equalsIgnoreCase(collegeId))
                                {
                                    boolean newUser= (boolean) documentSnapshot.get("New");
                                    if(newUser==true)
                                    {
                                        //This is a new user
                                        String category=(String) documentSnapshot.get("Category");
                                        if(category.equalsIgnoreCase("Admin"))
                                        {
                                            adminData.setEmail(email.getText().toString());
                                            adminData.setPassword(email.getText().toString());
                                            //intent to admin signup page
                                            startActivity(new Intent(Signup.this,AdminSignUp.class));
                                            
                                        }
                                        else if(category.equalsIgnoreCase("Student"))
                                        {
                                            studentData.setEmail(email.getText().toString());
                                            studentData.setPassword(email.getText().toString());
                                            studentData.setCollegeid(collegeId);
                                            //intent to student signup step 1 page
                                            startActivity(new Intent(Signup.this,StudentSignup1_PersonalData.class));
                                        }
                                        else if(category.equalsIgnoreCase("Company"))
                                        {
                                            companyData.setEmail(email.getText().toString());
                                            companyData.setPassword(email.getText().toString());
                                            //intent to company signup page
                                            //Company page abhi bana nhi hai

                                        }
                                        
                                    }
                                    else
                                    {
                                        //Toast that this user has already signed up

                                        //Intent to login.class
                                        startActivity(new Intent(Signup.this,Login.class));
                                    }
                                }
                                else
                                {
                                    email.setError("This user does not belong to selected college");
                                    return;
                                }

                        }
                        else
                        {
                            email.setError("This email is not allowed by admin");
                            return;
                        }
                    }
                });


            }
        });
    }



    boolean allDataFilled()
    {
        //check all constraints here
         if(email.getText().toString().length()==0)
        {
            email.setError("Enter Mail");
             return false;

        }
        if(password1.getText().toString().length()==0)
        {
            password1.setError("Enter Mail");
             return false;
        }
        if(password2.getText().toString().length()==0)
        {
            password2.setError("Enter Mail");
             return false;
        }


//check if valid mail
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email1=email.getText().toString().trim();
        if(!email1.matches(emailPattern))
        {
            email.setError("Enter valid mail");
             return false;
        }
//check if password length greater than 8
        if(password1.getText().toString().length()< 8)
        {
            password1.setError("Enter atleast 8 characters");
             return false;
        }
        //check if both passwords match
        if(password1.getText().toString().equals(password2.getText().toString())==false)
        {
            password2.setError("Enter correct password");
             return false;
        }
        return true;
    }
}