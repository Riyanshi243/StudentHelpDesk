package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

//Token from messanging: cf7h47URQrWogQlT9q_lqY:APA91bGOGUYefMU612AvORbOc0g1CmsPDbf2P_DgzcAEowdZBVkCspTQj1qO0BvkJ1kfPyzsCqWkYDBbqvBIxk9-yrWDaYSpEhb45GMZkH1yqQx9fLdH-q8JoPQ32MUUyQT8FcIFNffq
//tokWN here: eyJhbGciOiJSUzI1NiIsImtpZCI6ImYyNGYzMTQ4MTk3ZWNlYTUyOTE3YzNmMTgzOGFiNWQ0ODg3ZWEwNzYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vc3R1ZGVudGhlbHBkZXNrLWZlYzM4IiwiYXVkIjoic3R1ZGVudGhlbHBkZXNrLWZlYzM4IiwiYXV0aF90aW1lIjoxNjQ0OTQ2ODY3LCJ1c2VyX2lkIjoiMExraGROMThGUGdya0ZWQk92QnQ2S3JiTTYwMiIsInN1YiI6IjBMa2hkTjE4RlBncmtGVkJPdkJ0NktyYk02MDIiLCJpYXQiOjE2NDQ5NDY4NjksImV4cCI6MTY0NDk1MDQ2OSwiZW1haWwiOiJzdHVkZW50MkBiYW5hc3RoYWxpLmluIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7ImVtYWlsIjpbInN0dWRlbnQyQGJhbmFzdGhhbGkuaW4iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.IBES8yNB6YQ-cDBfUNppRImpgQc_soKN1_QWRyiduxmJney-0SYGxp1ZhfjuKyUgRrAHqM3k1TxMio1fesdQkj-ZH3QUSu9y0l4Z9JHy48Za8hOHcbKZdi_AWwHEYd67xlTCqKcrXxKF91lfugsiKdaU5xGHDe6GYmWgcRzyBxAXqT7JX0N4_ai9PfkEL2KBW7UQSUgKpnJ6b82_lb3j5V_PSs98CaZgLyvfkWdg12tvSorGeJ2V8ga8VHBbSpBoqrh46aSl4WIrELOyCGZ3qWPHK9PT3w1fqf0yTXi6Z6XIen20MqdkTS5HzgnC_uNss0-CuLXk2QHWnpGnFtW49A
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
        collge = findViewById(R.id.audience);
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
                //Collections.sort(listName,(o1, o2)->o1.compareTo(o2));
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


    public void next(View v) {
        signup.setEnabled(false);
        ProgressBar pbar = findViewById(R.id.progressBar5);
        pbar.setVisibility(View.VISIBLE);
        //get the college selected
        int i = cnumber;
        //i=collge.getListSelection();
        if (allDataFilled() == false) {
            return;
        }
        FirebaseFirestore f = FirebaseFirestore.getInstance();
        DocumentReference allcllgName = f.collection("All Users On App").document("All Colleges");
        allcllgName.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listId = (ArrayList<String>) documentSnapshot.get("IDs");
                String collegeId = listId.get(i);
                DocumentReference userDetails = f.collection("All Users On App").document(email.getText().toString());
                userDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String collgeid = (String) documentSnapshot.get("College");
                            if (collgeid.equalsIgnoreCase(collegeId)) {
                                boolean newUser = (boolean) documentSnapshot.get("New");
                                if (newUser == true) {
                                    //This is a new user
                                    String category = (String) documentSnapshot.get("Category");
                                    if (category.equalsIgnoreCase("Admin")) {
                                        adminData.setEmail(email.getText().toString());
                                        adminData.setPassword(password1.getText().toString());
                                        adminData.setCollegeId(collegeId);
                                        //intent to admin signup page
                                        startActivity(new Intent(Signup.this, AdminSignUp.class));
                                        signup.setEnabled(true);
                                        ProgressBar pbar = findViewById(R.id.progressBar5);
                                        pbar.setVisibility(View.INVISIBLE);

                                    } else if (category.equalsIgnoreCase("Student")) {
                                        studentData.setEmail(email.getText().toString());
                                        studentData.setPassword(password1.getText().toString());
                                        studentData.setCollegeid(collegeId);
                                        //intent to student signup step 1 page
                                        startActivity(new Intent(Signup.this, StudentSignup1_PersonalData.class));
                                        signup.setEnabled(true);
                                        ProgressBar pbar = findViewById(R.id.progressBar5);
                                        pbar.setVisibility(View.INVISIBLE);
                                    } else if (category.equalsIgnoreCase("Company")) {
                                        companyData.setEmail(email.getText().toString());
                                        companyData.setPassword(password1.getText().toString());
                                        companyData.setCollegeId(collegeId);
                                        //intent to company signup page
                                        startActivity(new Intent(Signup.this, CompanySignUp.class));
                                        signup.setEnabled(true);
                                        ProgressBar pbar = findViewById(R.id.progressBar5);
                                        pbar.setVisibility(View.INVISIBLE);
                                    }

                                } else {
                                    //Toast that this user has already signed up
                                    Toast.makeText(Signup.this, "You have already Signed Up, You may Login", Toast.LENGTH_LONG).show();

                                    //Intent to login.class
                                    startActivity(new Intent(Signup.this, Login.class));
                                }
                            } else {
                                email.setError("This user does not belong to selected college");
                                email.requestFocus();
                                pbar.setVisibility(View.INVISIBLE);
                                signup.setEnabled(true);
                                return;
                            }

                        } else {
                            email.setError("This email is not allowed by admin");
                            email.requestFocus();
                            pbar.setVisibility(View.INVISIBLE);
                            signup.setEnabled(true);
                            return;
                        }
                    }
                });


            }
        });
    }


    boolean allDataFilled() {
        signup.setEnabled(false);
        ProgressBar pbar = findViewById(R.id.progressBar5);
        pbar.setVisibility(View.VISIBLE);
        adminData = new AdminData();
        //check all constraints here
        if (email.getText().toString().length() == 0 || !email.getText().toString().contains("@")) {
            email.setError("EMAIL IS REQUIRED");
            email.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }

       //check if valid mail
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email1 = email.getText().toString().trim();
        if (!email1.matches(emailPattern)) {
            email.setError("ENTER VALID EMAIL");
            email.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;

        }

        if (password1.getText().toString().length() == 0) {
            password1.setError("PASSWORD IS REQUIRED");
            password1.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }

        //check if password length greater than 8
        if (password1.getText().toString().length() < 8) {
            password1.setError("ENTER ATLEAST 8 CHARACTERS");
            password1.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;

        }
        if (password2.getText().toString().length() == 0) {
            password2.setError("CONFIRM PASSWORD IS REQUIRED");
            password2.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }
        if (!password2.getText().toString().equals(password1.getText().toString())) {
            password2.setError("PASSWORDS DO NOT MATCH");
            password2.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder saveDetails=new AlertDialog.Builder(this);
        saveDetails.setTitle("ARE YOU SURE?");
        saveDetails.setMessage("All unsaved data will be lost.");
        saveDetails.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Signup.this,Login.class));
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        saveDetails.create().show();
    }
}