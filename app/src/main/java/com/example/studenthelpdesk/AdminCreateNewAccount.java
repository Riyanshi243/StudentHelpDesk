package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AdminCreateNewAccount extends AppCompatActivity {
    TextView email;
    CheckBox student,admin,company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_new_account);
        email=findViewById(R.id.email);
        student=findViewById(R.id.checkBox_student);
        admin=findViewById(R.id.checkBox_admin);
        company=findViewById(R.id.checkBox_company);
        student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(student.isChecked())
                {
                    checkStudent();
                }
            }
        });
        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(admin.isChecked())
                {
                    checkAdmin();
                }
            }
        });
        company.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(company.isChecked())
                {
                    checkCompany();
                }
            }
        });
    }
    public void createUser(View v)
    {
        if(checkEmail())
        {
            AlertDialog.Builder areYouSure=new AlertDialog.Builder(this);
            areYouSure.setTitle("PLEASE CONFIRM THE INFORMATION");
            String cat="Student";
            if(admin.isChecked())
                cat="Admin";
            else if(company.isChecked())
                cat="Company";
            else if(student.isChecked())
                cat="Student";
            String s="EMAIL: "+email.getText().toString()+"\nCATEGORY: "+cat;
            areYouSure.setMessage(s);
            String finalCat = cat;
            areYouSure.setPositiveButton("YES,Create User", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseFirestore f=FirebaseFirestore.getInstance();
                    DocumentReference docuserInfo = f.collection("All Users On App").document(email.getText().toString());
                   //check if user exists
                    docuserInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                       @Override
                       public void onSuccess(DocumentSnapshot documentSnapshot) {
                           if (documentSnapshot.exists())
                           {
                               email.setError("USER EXISTS");
                           }
                           else
                           {
                               HashMap<String,Object> userInfo=new HashMap<>();
                               userInfo.put("Category", finalCat);
                               userInfo.put("New",true);
                               userInfo.put("College ",AdminPage.adminData.getCollegeId());
                               docuserInfo.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       email.setText("");
                                       student.setChecked(true);
                                       admin.setChecked(false);
                                       company.setChecked(false);
                                       Toast.makeText(AdminCreateNewAccount.this,"User Created",Toast.LENGTH_LONG).show();
                                   }
                               });
                           }
                       }
                   });
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //do nothing
                }
            });
            areYouSure.create().show();
        }

    }
    public void checkAdmin()
    {
        if(company.isChecked())
            company.setChecked(false);
        if(student.isChecked())
            student.setChecked(false);
    }
    public void checkCompany()
    {
        if(admin.isChecked())
            admin.setChecked(false);
        if(student.isChecked())
            student.setChecked(false);
    }
    public void checkStudent()
    {
        if(admin.isChecked())
            admin.setChecked(false);
        if(company.isChecked())
            company.setChecked(false);
    }
    public boolean checkEmail()
    {
        //check if not empty
        //check if valid
        return  true;
    }

}