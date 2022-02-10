package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    }
    public void Login(View view)
    {

        FirebaseFirestore f=FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                DocumentReference userDetails = f.collection("All Users On App").document(email.getText().toString());
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

                DocumentReference userDetails = f.collection("All Users On App").document(email.getText().toString());
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
                        }
                        else
                        {
                            Toast.makeText(Login.this,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
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

}