package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

import java.util.ArrayList;

public class RegisterCollege extends AppCompatActivity {
    //this instance will be referred to untill registration is completed
    static CollegeRegistrationData allData;
    EditText uname, cname, password, password2, adminmail, location;
    private Button register;
    FirebaseFirestore firebaseFirestores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college);

        uname = findViewById(R.id.uname);
        cname = findViewById(R.id.cname);
        location = findViewById(R.id.clocation);
        adminmail = findViewById(R.id.adminemail);
        password = findViewById(R.id.password2);
        password2 = findViewById(R.id.confirmpassword2);
        register = (Button) findViewById(R.id.register);
        firebaseFirestores = FirebaseFirestore.getInstance();
        allData = new CollegeRegistrationData();

    }

    //move to next step of registration
    public void next(View v) {
        register.setEnabled(false);
        ProgressBar pbar = findViewById(R.id.progressBar_college);
        pbar.setVisibility(View.VISIBLE);
        if (checkConstraints()) {

            DocumentReference documentReference1 = firebaseFirestores.collection("All Users On App").document(adminmail.getText().toString());
            documentReference1.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        adminmail.setError("This mail is already registered");
                        register.setEnabled(true);
                        ProgressBar pbar = findViewById(R.id.progressBar_college);
                        pbar.setVisibility(View.INVISIBLE);

                    } else {
                        DocumentReference documentReference = firebaseFirestores.collection("All Users On App").document("All Colleges");
                        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                //if username exist... a document will already be present in its name
                                if (documentSnapshot.exists()) {
                                    ArrayList<String> id = (ArrayList<String>) documentSnapshot.get("IDs");
                                    if (id.size() == 0 || id.contains(uname.getText().toString())) {
                                        uname.setError("THIS USERNAME ALREADY EXISTS");
                                        register.setEnabled(true);
                                        ProgressBar pbar = findViewById(R.id.progressBar_college);
                                        pbar.setVisibility(View.INVISIBLE);
                                    } else {
                                        allData.setSAdminemail(adminmail.getText().toString().trim());
                                        allData.setPassword(password.getText().toString());
                                        allData.setUname(uname.getText().toString().trim());
                                        allData.setCName(cname.getText().toString().trim());
                                        allData.setLocation(location.getText().toString().trim());
                                        //intent to college registration step 2
                                        startActivity(new Intent(RegisterCollege.this, RegisterCollege2.class));
                                        register.setEnabled(true);
                                        ProgressBar pbar = findViewById(R.id.progressBar_college);
                                        pbar.setVisibility(View.INVISIBLE);
                                    }

                                } else {
                                    allData.setSAdminemail(adminmail.getText().toString().trim());
                                    allData.setPassword(password.getText().toString());
                                    allData.setUname(uname.getText().toString().trim());
                                    allData.setCName(cname.getText().toString().trim());
                                    allData.setLocation(location.getText().toString().trim());
                                    //intent to college registration step 2
                                    startActivity(new Intent(RegisterCollege.this, RegisterCollege2.class));
                                    register.setEnabled(true);
                                    ProgressBar pbar = findViewById(R.id.progressBar_college);
                                    pbar.setVisibility(View.INVISIBLE);

                                }
                            }
                        });
                    }
                }
            });


        }
    }

    public void toLogin(View v) {
        //Intent to login
        startActivity(new Intent(RegisterCollege.this, Login.class));
        finish();
    }

    public Boolean checkConstraints() {
        register.setEnabled(false);
        ProgressBar pbar = findViewById(R.id.progressBar_college);
        pbar.setVisibility(View.VISIBLE);

        //everything not empty

        if (cname.getText().toString().length() == 0) {
            cname.setError("ENTER COLLEGE NAME");
            cname.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }

        if (uname.getText().toString().length() == 0) {
            uname.setError("ENTER USERNAME");
            uname.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }
        if (location.getText().toString().length() == 0) {
            location.setError("ENTER LOCATION");
            location.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }
        if (adminmail.getText().toString().length() == 0) {
            adminmail.setError("ENTER EMAIL");
            adminmail.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }
        //check if valid mail
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email = adminmail.getText().toString().trim();
        if (!email.matches(emailPattern)) {
            adminmail.setError("ENTER VALID EMAIL");
            adminmail.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }
        if (password.getText().toString().length() == 0) {
            password.setError("ENTER PASSWORD");
            password.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }
        //check if password length greater than 8
        if (password.getText().toString().length() < 8) {
            password.setError("ENTER ATLEAST 8 CHARACTERS");
            password.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }
        if (password2.getText().toString().length() == 0) {
            password2.setError("ENTER CONFIRM PASSWORD");
            password2.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
            return false;
        }

        //check if passwords match
        if (password.getText().toString().equals(password2.getText().toString()) == false) {
            password2.setError("PASSWORDS DO NOT MATCH");
            password2.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            register.setEnabled(true);
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
                startActivity(new Intent(RegisterCollege.this, Login.class));
                finishAffinity();
            }
        }).setNegativeButton("Save & Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                next(new View(RegisterCollege.this));
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