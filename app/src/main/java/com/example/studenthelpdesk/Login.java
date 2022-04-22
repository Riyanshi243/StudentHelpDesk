package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText email,password;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login= (Button) findViewById(R.id.login);
        StudentPage.studentData=null;
        CompanyPage.companyData=null;
        AdminPage.adminData=null;
    }
    public void Login(View view)
    {
        if(checkConstraints()==false)
        {
            return;
        }
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                DocumentReference userDetails = f.collection("All Users On App").document(email.getText().toString().trim());
                userDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String category= (String) documentSnapshot.get("Category");
                        if(category.equalsIgnoreCase("Admin"))
                        {
                            //intent to admin page
                            startActivity(new Intent(Login.this,AdminPage.class));
                            finish();
                        }
                        else if(category.equalsIgnoreCase("Student"))
                        {
                            //intent to student page
                            startActivity(new Intent(Login.this,StudentPage.class));
                            finish();
                        }
                        else if(category.equalsIgnoreCase("Company"))
                        {
                            //intent to company page
                            startActivity(new Intent(Login.this,CompanyPage.class));
                            finish();
                        }
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                DocumentReference userDetails = f.collection("All Users On App").document(email.getText().toString().trim());
                userDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            boolean sign= (boolean) documentSnapshot.get("New");
                            if(sign==true)
                            {
                                Toast.makeText(Login.this,"You must Signin First",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Login.this,Signup.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Login.this,"Error:"+ e.getMessage(),Toast.LENGTH_LONG).show();
                                login.setEnabled(true);
                                ProgressBar pbar = findViewById(R.id.progressBar4);
                                pbar.setVisibility(View.INVISIBLE);
                            }
                        }
                        else
                        {
                            Toast.makeText(Login.this,"Error:"+ e.getMessage(),Toast.LENGTH_LONG).show();
                            login.setEnabled(true);
                            ProgressBar pbar = findViewById(R.id.progressBar4);
                            pbar.setVisibility(View.INVISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        login.setEnabled(true);
                        ProgressBar pbar = findViewById(R.id.progressBar4);
                        pbar.setVisibility(View.INVISIBLE);
                    }
                });

            }
        });
    }
    //function for checking constraints
    public boolean checkConstraints()
    {
        login.setEnabled(false);
        ProgressBar pbar = findViewById(R.id.progressBar4);
        pbar.setVisibility(View.VISIBLE);

        if (email.getText().toString().length() == 0 || !email.getText().toString().contains("@")) {
            email.setError("EMAIL IS REQUIRED");
            email.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            login.setEnabled(true);
            return false;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email1 = email.getText().toString().trim();
        if (!email1.matches(emailPattern)) {
            email.setError("ENTER VALID MAIL");
            email.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            login.setEnabled(true);
            return false;

        }
        if (password.getText().toString().length() == 0) {
            password.setError("PASSWORD IS REQUIRED");
            password.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            login.setEnabled(true);
            return false;
        }
        if (password.getText().toString().length() < 8) {
            password.setError("ENTER ATLEAST 8 CHARACTERS");
            password.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            login.setEnabled(true);
            return false;
        }

     return true;

    }
    public void signUp(View v)
    {
      startActivity(new Intent(Login.this,Signup.class));
        finish();
    }
    public void register(View v)
    {
        startActivity(new Intent(Login.this,RegisterCollege.class));
        finish();
    }
    public void forgetPassword(View v)
    {
        EditText resetMail=new EditText(v.getContext());
        resetMail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        resetMail.requestFocus();
        AlertDialog.Builder passres=new AlertDialog.Builder(v.getContext());
        passres.setTitle("Reset Password?");
        passres.setMessage("Enter your E-mail to reset password.");
        passres.setView(resetMail);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        passres.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mail=resetMail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Login.this,"Reset link sent to your E-mail",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this,"Error! Reset Link not sent\n Try again"+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        passres.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close dialog
            }
        });
        passres.create().show();
    }
}