package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class AdminSignUp extends AppCompatActivity {

EditText name,phone;
AutoCompleteTextView dept;
int deptn=-1;
private ProgressBar progressBar5;
ArrayList<String> deptName;
AdminData adminData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);
        adminData=Signup.adminData;
        name=findViewById(R.id.adminname);
        phone=findViewById(R.id.contact);
        dept=findViewById(R.id.deptName);
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference collegeRef = f.collection("All Colleges").document(adminData.getCollegeId());
        collegeRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                deptName= (ArrayList<String>) documentSnapshot.get("Departments");
                ArrayAdapter spinnerList=new ArrayAdapter(AdminSignUp.this,android.R.layout.simple_spinner_item,deptName);
                dept.setAdapter(spinnerList);
                dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        deptn=arg2;
                    }
                });
            }
        });
    }
    public void saveAndNext(View v)
    {
        if(deptn!=-1)
        {
            if (checkFilled())
            {
                adminData.setAdminName(name.getText().toString());
                adminData.setPhoneNumber(phone.getText().toString());
                adminData.setDeptName(deptName.get(deptn));
                startActivity(new Intent(this,AdminSignUp2.class));
            }
        }
        else
        {
            Toast.makeText(this,"Select Department",Toast.LENGTH_LONG).show();
        }
        //j
    }
    public void uploadPic(View v)
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK) {
                dialog = new ProgressDialog(this);
                dialog.setMessage("Uploading");
                dialog.show();
                Uri imageuri = data.getData();
                uploadPic(imageuri);
                Toast.makeText(this,getNameFromURI(data.getData())+"Uploaded",Toast.LENGTH_LONG).show();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "ACTIVITY CANCELLED", Toast.LENGTH_SHORT).show();
            }
        }


    }
    public String getNameFromURI(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        @SuppressLint("Range") String string = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        return string;
    }

    ProgressDialog dialog;
    public void uploadPic(Uri imageuri) {

        if(imageuri!=null)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageRef=storage.getReference(adminData.getCollegeId()).child("Photograph");

            StorageReference fileReference =storageRef.child((adminData.getEmail()));
            fileReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if(dialog.isShowing())
                        dialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminSignUp.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(this,"No Image selected",Toast.LENGTH_LONG).show();
        }

    }
    boolean checkFilled()
    {
        //check name not empty and phone number not empty
        return true;
    }
}