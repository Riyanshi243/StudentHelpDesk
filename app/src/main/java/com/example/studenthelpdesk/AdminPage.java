package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminPage extends AppCompatActivity {
    static AdminData adminData;
     FirebaseAuth f;
     TextView greetings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        adminData=new AdminData();
        greetings=findViewById(R.id.head);
        f=FirebaseAuth.getInstance();
        adminData.setEmail(f.getCurrentUser().getEmail());
        FirebaseFirestore fs=FirebaseFirestore.getInstance();
        DocumentReference docUserInfo = fs.collection("All Users On App").document(adminData.getEmail());
        docUserInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String cId= (String) documentSnapshot.get("College");
                String dept=(String) documentSnapshot.get("Department");
                adminData.setCollegeId(cId);
                adminData.setDeptName(dept);
                DocumentReference docUserInfoAll = fs.collection("All Colleges").document(cId).collection("AllUsers").document("Admin").collection(dept).document(adminData.getEmail());
                docUserInfoAll.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String name= (String) documentSnapshot.get("Name");
                        String phone=(String) documentSnapshot.get("Phone Number");
                        adminData.setPhoneNumber(phone);
                        adminData.setAdminName(name);
                        greetings.setText(greetings.getText()+name);

                    }
                });
            }
        });


    }
    public void createNewUser(View v){
        startActivity(new Intent(AdminPage.this, AdminCreateNewAccount.class));
    }
    public void sendNotification(View v){
        startActivity(new Intent(AdminPage.this, AdminOrCompanySendNotification.class));
    }
    public void logout(View v)
    {
        f.signOut();
        Toast.makeText(this,"Logged Out",Toast.LENGTH_LONG).show();
        startActivity(new Intent(AdminPage.this,Login.class));
        finish();
    }
}