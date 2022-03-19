package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AdminEditProfilePage extends AppCompatActivity {
    TextView name,email,department,phone;
    AdminData adminData;
    ImageView profilepic;
    ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_profile_page);
        profilepic=findViewById(R.id.profilePic);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        department=findViewById(R.id.deptName);
        phone=findViewById(R.id.phone);
        progressbar=findViewById(R.id.progressBar5);

        adminData=AdminPage.adminData;
        name.setText(adminData.getAdminName());
        email.setText(adminData.getEmail());
        Linkify.addLinks(email, Linkify.ALL);
        email.setLinkTextColor(Color.parseColor("#034ABC"));
        department.setText(adminData.getDeptName());
        phone.setText(adminData.getPhoneNumber());

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(adminData.getCollegeId()).child("Photograph").child(adminData.getEmail());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(AdminEditProfilePage.this)
                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.admin_profile_img)
                        .placeholder(R.drawable.default_loading_img)
                        .into(profilepic);
            }
    });
    }
    public void nameClick(View view)
    {
        EditText et = new EditText(view.getContext());
        et.setText(adminData.getAdminName());
        et.requestFocus();

        AlertDialog builder=new AlertDialog.Builder(AdminEditProfilePage.this)
            .setTitle("Edit Name")
            .setMessage("Enter new Name")
            .setView(et)
            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        }).setPositiveButton("SAVE", null)
                .setCancelable(false)
                .show();
        Button posButton=builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
        posButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString().length()==0)
                {
                    et.setError("This is Compulsory field");
                    return;
                }
                else
                {
                    builder.dismiss();
                    name.setText(et.getText().toString().trim());
                    adminData.setAdminName(et.getText().toString().trim());
                    HashMap <String,Object> changeDetail = new HashMap<>();
                    changeDetail.put("Name",et.getText().toString().trim());
                    FirebaseFirestore.getInstance().collection("All Colleges")
                            .document(adminData.getCollegeId()).collection("UsersInfo")
                            .document(adminData.getEmail()).update(changeDetail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminEditProfilePage.this,"Data saved Successfully!!",Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
    public void phoneClick(View view)
    {
        EditText et = new EditText(view.getContext());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setText(adminData.getPhoneNumber());
        et.requestFocus();

        AlertDialog builder=new AlertDialog.Builder(AdminEditProfilePage.this)
                .setTitle("Edit Phone Number")
                .setMessage("Enter new Phone Number")
                .setView(et)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        }).setPositiveButton("SAVE", null)
                .setCancelable(false)
                .show();
        Button posButton=builder.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE);
        posButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et.getText().toString().length()==0)
                {
                    et.setError("This is Compulsory field");
                    return;
                }
                else if(et.getText().toString().length()<10 || et.getText().toString().trim().length()>10)
                {
                    et.setError("INVALID PHONE NUMBER");
                    return;
                }
                 else{
                    builder.dismiss();
                    phone.setText(et.getText().toString().trim());
                    adminData.setPhoneNumber(et.getText().toString().trim());
                    HashMap <String,Object> changeDetail = new HashMap<>();
                    changeDetail.put("Phone Number",et.getText().toString().trim());
                    FirebaseFirestore.getInstance().collection("All Colleges")
                            .document(adminData.getCollegeId()).collection("UsersInfo")
                            .document(adminData.getEmail()).update(changeDetail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminEditProfilePage.this,"Data saved Successfully!!",Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
    public void nonchangeable(View view)
    {
        //Toast for non-editable field
        Toast.makeText(this,"This field in non-Editable!!",Toast.LENGTH_LONG).show();
    }
    public void changeProfile(View view) {
        progressbar.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(adminData.getCollegeId()).child("Photograph").child(adminData.getEmail());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(AdminEditProfilePage.this)
                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.error_profile_picture)
                        .placeholder(R.drawable.default_loading_img)
                        .into(profilepic);

            }
        });
    }
    ProgressDialog dialog;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK) {
                dialog = new ProgressDialog(this);
                dialog.setMessage("UPLOADING");
                dialog.setCancelable(false);
                dialog.show();
                Uri imageuri = data.getData();
                uploadPic(imageuri, "Photograph");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "ACTIVITY CANCELLED", Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void uploadPic(Uri imageuri,String q) {

        if(imageuri!=null)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef=storage.getReference(adminData.getCollegeId()).child(q);
            StorageReference fileReference =storageRef.child((adminData.getEmail()));
            fileReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
                    String uploadid=databaseReference.push().getKey();
                    if(dialog.isShowing())
                        dialog.dismiss();

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(adminData.getCollegeId()).child("Photograph").child(adminData.getEmail());
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(AdminEditProfilePage.this)
                                    .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .error(R.drawable.error_profile_picture)
                                    .placeholder(R.drawable.default_loading_img)
                                    .into(profilepic);

                            progressbar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminEditProfilePage.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.INVISIBLE);

                }
            });
        }
        else
        {
            Toast.makeText(this,"NO IMAGE SELECTED",Toast.LENGTH_LONG).show();
            progressbar.setVisibility(View.INVISIBLE);
        }

    }

}