package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CompanySignUp extends AppCompatActivity {
    EditText name,email,phone,location,companyName;
    private Button signup;

    CompanyData companyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_sign_up);
        signup=findViewById(R.id.signup);
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
            companyData.setName(name.getText().toString().trim());
            companyData.setPhone(phone.getText().toString());
            companyData.setCompanyName(companyName.getText().toString().trim());
            companyData.setLocation(location.getText().toString().trim());
            companyData.setPersonalEmail(email.getText().toString().trim());
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
                dialog.setCancelable(false);
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
                    ImageView profileimg=(ImageView) findViewById(R.id.uplodingdone);
                    profileimg.setVisibility(View.VISIBLE);

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

        signup.setEnabled(false);
        ProgressBar pbar =findViewById(R.id.progressBar5);
        pbar.setVisibility(View.VISIBLE);
        //check name not empty and phone number not empty
        if(companyName.getText().toString().length()==0)
        {
            companyName.setError("ENTER COMPANY NAME");
            companyName.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }
        if(location.getText().toString().length()==0)
        {
            location.setError("ENTER LOCATION");
            location.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }
        if(name.getText().toString().length()==0)
        {
            name.setError("ENTER USERNAME");
            name.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }

        if(email.getText().toString().length()==0)
        {
            email.setError("ENTER EMAIL");
            email.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email1=email.getText().toString().trim();
        if(!email1.matches(emailPattern))
        {
            email.setError("ENTER VALID MAIL");
            email.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }
        if(phone.getText().toString().length()==0)
        {
            phone.setError("ENTER PHONE NUMBER");
            phone.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }
        if(phone.getText().toString().length()<10 || phone.getText().toString().trim().length()>10)
        {
            phone.setError("INVALID PHONE NUMBER");
            phone.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            signup.setEnabled(true);
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder saveDetails=new AlertDialog.Builder(this);
        saveDetails.setTitle("ARE YOU SURE?");
        saveDetails.setMessage("All unsaved data will be lost.");
        saveDetails.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CompanySignUp.super.onBackPressed();
            }
        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        saveDetails.create().show();
    }
}