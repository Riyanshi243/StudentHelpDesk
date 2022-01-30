package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminPage extends AppCompatActivity {
    static AdminData adminData;
     ImageView create_new_user,search_user,view_all_data,see_req,send_notif,faq,lockdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        adminData=new AdminData();
        FirebaseAuth f=FirebaseAuth.getInstance();
        adminData.setEmail(f.getCurrentUser().getEmail());
        FirebaseFirestore fs=FirebaseFirestore.getInstance();
        DocumentReference docUserInfo = fs.collection("All Users On App").document(adminData.getEmail());
        docUserInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String cId= (String) documentSnapshot.get("College");
                adminData.setCollegeId(cId);
            }
        });


    }
    public void createNewUser(View v){
        startActivity(new Intent(AdminPage.this, AdminCreateNewAccount.class));
    }
}