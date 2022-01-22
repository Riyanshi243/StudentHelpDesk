package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseFirestore f=FirebaseFirestore.getInstance();
                DocumentReference userDetails = f.collection("All Users On App").document(email.getText().toString());
                userDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String category= (String) documentSnapshot.get("Category");
                        if(category.equalsIgnoreCase("Admin"))
                        {
                            //intent to admin page
                            startActivity(new Intent(Login.this,AdminPage.class));
                        }
                        else if(category.equalsIgnoreCase("Student"))
                        {
                            //intent to student page
                        }
                        else if(category.equalsIgnoreCase("Company"))
                        {
                            //intent to student page
                        }
                    }
                });
            }
        });
    }
}