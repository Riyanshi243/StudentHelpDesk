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
                Log.e("id", collegeId);
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

                                    } else if (category.equalsIgnoreCase("Student")) {
                                        studentData.setEmail(email.getText().toString());
                                        studentData.setPassword(password1.getText().toString());
                                        studentData.setCollegeid(collegeId);
                                        //intent to student signup step 1 page
                                        startActivity(new Intent(Signup.this, StudentSignup1_PersonalData.class));
                                    } else if (category.equalsIgnoreCase("Company")) {
                                        companyData.setEmail(email.getText().toString());
                                        companyData.setPassword(password1.getText().toString());
                                        companyData.setCollegeId(collegeId);
                                        //intent to company signup page
                                        startActivity(new Intent(Signup.this, CompanySignUp.class));

                                    }

                                } else {
                                    //Toast that this user has already signed up
                                    Toast.makeText(Signup.this, "USER HAS ALREADY SIGNED UP", Toast.LENGTH_LONG).show();

                                    //Intent to login.class
                                    startActivity(new Intent(Signup.this, Login.class));
                                }
                            } else {
                                email.setError("This user does not belong to selected college");
                                pbar.setVisibility(View.INVISIBLE);
                                signup.setEnabled(true);
                                return;
                            }

                        } else {
                            email.setError("This email is not allowed by admin");
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
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }

        if (password1.getText().toString().length() == 0) {
            password1.setError("PASSWORD IS REQUIRED");
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }

        if (!password2.getText().toString().equals(password1.getText().toString())) {
            password2.setError("PASSWORDS DO NOT MATCH");
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }


//check if valid mail
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email1 = email.getText().toString().trim();
        if (!email1.matches(emailPattern)) {
            email.setError("ENTER VALID MAIL");
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;

        }

//check if password length greater than 8
        if (password1.getText().toString().length() < 8) {
            password1.setError("ENTER ATLEAST 8 CHARACTERS");
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;

        }

        //check if both passwords match
        if (password1.getText().toString().equals(password2.getText().toString()) == false) {
            password2.setError("PASSWORDS DO NOT MATCH");
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }
        return true;
    }
}