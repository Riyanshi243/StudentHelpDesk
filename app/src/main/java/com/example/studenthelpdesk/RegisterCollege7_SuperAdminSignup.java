package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.InputType;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterCollege7_SuperAdminSignup extends AppCompatActivity {
    EditText name,phone;
    AutoCompleteTextView dept;
    CollegeRegistrationData alldata;
    int deptn=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college7_super_admin_signup);
        name=findViewById(R.id.adminname);
        phone=findViewById(R.id.contact);
        phone.setInputType(InputType.TYPE_CLASS_NUMBER);
        dept=findViewById(R.id.deptName);
        alldata=RegisterCollege.allData;
        ArrayAdapter spinnerList=new ArrayAdapter(RegisterCollege7_SuperAdminSignup.this,android.R.layout.simple_spinner_item,alldata.getDeptName());
        dept.setAdapter(spinnerList);
        dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                deptn=arg2;
            }
        });
    }
    public void saveAndNext(View v)
    {
        if(deptn!=-1)
        {
            if (checkFilled())
            {
                alldata.setAdminName(name.getText().toString());
                alldata.setAdminNo(phone.getText().toString());
                alldata.setAdminDept(alldata.getDeptName().get(deptn));
                startActivity(new Intent(RegisterCollege7_SuperAdminSignup.this,RegisterCollege8.class));
            }
        }
        else
        {
            Toast.makeText(this,"Select Department",Toast.LENGTH_LONG).show();
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
                //ImageView profileimg=(ImageView) findViewById(R.id.uplodingdone);
                //profileimg.setVisibility(View.VISIBLE);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(RegisterCollege7_SuperAdminSignup.this, "ACTIVITY CANCELLED", Toast.LENGTH_SHORT).show();
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

            StorageReference storageRef=storage.getReference(alldata.getUname()).child("Photograph");

            StorageReference fileReference =storageRef.child((alldata.getSAdminemail()));
            fileReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
                    if(dialog.isShowing())
                        dialog.dismiss();
                    ImageView profileimg=(ImageView) findViewById(R.id.uplodingdone);
                    profileimg.setVisibility(View.VISIBLE);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(RegisterCollege7_SuperAdminSignup.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            dialog.dismiss();
            Toast.makeText(this,"No Image selected",Toast.LENGTH_LONG).show();
        }

    }
    boolean checkFilled()
    {
        //check name not empty and phone number not empty
        if(name.getText().toString().length()==0)
        {
            name.setError("ENTER USERNAME");
            return false;
        }
        if(phone.getText().toString().length()<10|| phone.getText().toString().trim().length()>10)
        {
            phone.setError("INVALID PHONE NUMBER");
            return false;
        }
       else if(phone.getText().toString().length()==0)
        {
            phone.setError("ENTER PHONE NUMBER");
            return false;
        }
        return true;
    }
}