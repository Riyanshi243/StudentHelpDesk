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
    CheckBox admin,company,student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_create_new_account);
        email=findViewById(R.id.email);
        admin=findViewById(R.id.checkBox_admin);
        company=findViewById(R.id.checkBox_company);
        student=findViewById(R.id.checkBox_student);
        student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //check if student
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(student.isChecked())
                {
                    checkStudent();
                }

            }
        });
        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //check if admin
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(admin.isChecked())
                {
                    checkAdmin();
                }

            }
        });
        company.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //check if company
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
            else
                cat="Student";
            String s="EMAIL: "+email.getText().toString().trim()+"\nCATEGORY: "+cat;
            areYouSure.setMessage(s);
            String finalCat = cat;
            areYouSure.setPositiveButton("YES,Create User", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseFirestore f=FirebaseFirestore.getInstance();
                    DocumentReference docuserInfo = f.collection("All Users On App").document(email.getText().toString().trim());
                    //check if user exists
                    docuserInfo.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                email.setError("User Exists");
                                email.requestFocus();
                            }
                            else
                            {
                                HashMap<String,Object> userInfo=new HashMap<>();
                                userInfo.put("Category", finalCat);
                                userInfo.put("New",true);
                                userInfo.put("College",AdminPage.adminData.getCollegeId());
                                docuserInfo.set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        email.setText("");
                                        admin.setChecked(false);
                                        company.setChecked(false);
                                        student.setChecked(true);
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
        admin.setChecked(true);
    }
    public void checkCompany()
    {
        if(student.isChecked())
            student.setChecked(false);
        if(admin.isChecked())
            admin.setChecked(false);
        company.setChecked(true);
    }
    public void checkStudent()
    {
        if(admin.isChecked())
            admin.setChecked(false);
        if(company.isChecked())
            company.setChecked(false);
        student.setChecked(true);
    }
    public boolean checkEmail()
    {
        //check if email is not empty
        if(email.getText().toString().length()==0)
        {
            email.setError("ENTER EMAIL");
            email.requestFocus();
            return false;
        }
        //check if email is valid
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email1=email.getText().toString().trim();
        if(!email1.matches(emailPattern))
        {
            email.setError("ENTER VALID MAIL");
            email.requestFocus();
            return false;
        }
        return  true;
    }

}