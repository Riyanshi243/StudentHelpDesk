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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CompanySignUp extends AppCompatActivity {
    private ProgressBar progressBar5;
    EditText name,email,phone,location,companyName;

    CompanyData companyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_sign_up);
        companyData=Signup.companyData;
        name=findViewById(R.id.repre);
        companyName=findViewById(R.id.companyName);
        email=findViewById(R.id.emailrepre);
        phone=findViewById(R.id.phonerepre);
        location=findViewById(R.id.location);
    }
    public void saveAndNext(View v)
    {
        if (checkFilled())
        {
            companyData.setName(name.getText().toString());
            companyData.setPhone(phone.getText().toString());
            companyData.setCompanyName(companyName.getText().toString());
            companyData.setLocation(location.getText().toString());
            companyData.setPersonalEmail(email.getText().toString());
            startActivity(new Intent(this,CompanySignUp2.class));
        }
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

            StorageReference storageRef=storage.getReference(companyData.getCollegeId()).child("Photograph");

            StorageReference fileReference =storageRef.child((companyData.getEmail()));
            fileReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    if(dialog.isShowing())
                        dialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CompanySignUp.this,e.getMessage(),Toast.LENGTH_LONG).show();
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