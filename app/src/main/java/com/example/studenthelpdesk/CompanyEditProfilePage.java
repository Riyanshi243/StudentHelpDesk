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

public class CompanyEditProfilePage extends AppCompatActivity {
    TextView cname, cemail, location, repre_name, repre_emailid, repre_number;
    CompanyData companyData;
    ImageView profilepic;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile_page);
        companyData = CompanyPage.companyData;
        profilepic = findViewById(R.id.profilepic);
        cname = findViewById(R.id.cname);
        cemail = findViewById(R.id.cemail);
        location = findViewById(R.id.location);
        repre_name = findViewById(R.id.repre_name);
        repre_emailid = findViewById(R.id.repre_emailid);
        repre_number = findViewById(R.id.repre_number);
        progressbar = findViewById(R.id.progressBar5);

        cname.setText(companyData.getCompanyName());
        cemail.setText(companyData.getEmail());
        Linkify.addLinks(cemail, Linkify.ALL);
        cemail.setLinkTextColor(Color.parseColor("#034ABC"));
        location.setText(companyData.getLocation());
        repre_name.setText(companyData.getName());
        repre_emailid.setText(companyData.getPersonalEmail());
        Linkify.addLinks(repre_emailid, Linkify.ALL);
        repre_emailid.setLinkTextColor(Color.parseColor("#034ABC"));
        repre_number.setText(companyData.getPhone());
        Linkify.addLinks(repre_number, Linkify.ALL);
        repre_number.setLinkTextColor(Color.parseColor("#034ABC"));

        StorageReference storageRef = FirebaseStorage.getInstance().getReference(companyData.getCollegeId()).child("Photograph").child(companyData.getEmail());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(CompanyEditProfilePage.this)
                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.company_profile_img)
                        .placeholder(R.drawable.default_loading_img)
                        .into(profilepic);
            }
        });
    }

    public void cnameClick(View view) {
        EditText et = new EditText(view.getContext());
        et.setText(companyData.getCompanyName());
        et.requestFocus();

        AlertDialog builder = new AlertDialog.Builder(CompanyEditProfilePage.this)
                .setTitle("Edit Company Name")
                .setMessage("Enter new Company Name")
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
                    cname.setText(et.getText().toString().trim());
                    companyData.setCompanyName(et.getText().toString().trim());
                    HashMap<String, Object> changeDetail = new HashMap<>();
                    changeDetail.put("Company Name", et.getText().toString().trim());
                    FirebaseFirestore.getInstance().collection("All Colleges")
                            .document(companyData.getCollegeId()).collection("UsersInfo")
                            .document(companyData.getEmail()).update(changeDetail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CompanyEditProfilePage.this, "Data saved Successfully!!", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    public void locationClick(View view) {
        EditText et = new EditText(view.getContext());
        et.setText(companyData.getLocation());
        et.requestFocus();

        AlertDialog builder = new AlertDialog.Builder(CompanyEditProfilePage.this)
                .setTitle("Edit Company Location")
                .setMessage("Enter new Company Location")
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
                    location.setText(et.getText().toString().trim());
                    companyData.setLocation(et.getText().toString().trim());
                    HashMap<String, Object> changeDetail = new HashMap<>();
                    changeDetail.put("Company Location", et.getText().toString().trim());
                    FirebaseFirestore.getInstance().collection("All Colleges")
                            .document(companyData.getCollegeId()).collection("UsersInfo")
                            .document(companyData.getEmail()).update(changeDetail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CompanyEditProfilePage.this, "Data saved Successfully!!", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    public void repre_nameClick(View view) {
        EditText et = new EditText(view.getContext());
        et.setText(companyData.getName());
        et.requestFocus();

        AlertDialog builder = new AlertDialog.Builder(CompanyEditProfilePage.this)
            .setTitle("Edit Representative Name")
            .setMessage("Enter new Representative Name")
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
                if (et.getText().toString().length() == 0) {
                    et.setError("This is Compulsory field");
                    return;
                }
                else {
                    builder.dismiss();
                    repre_name.setText(et.getText().toString().trim());
                    companyData.setName(et.getText().toString().trim());
                    HashMap<String, Object> changeDetail = new HashMap<>();
                    changeDetail.put("Name", et.getText().toString().trim());
                    FirebaseFirestore.getInstance().collection("All Colleges")
                            .document(companyData.getCollegeId()).collection("UsersInfo")
                            .document(companyData.getEmail()).update(changeDetail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CompanyEditProfilePage.this, "Data saved Successfully!!", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    public void repre_emailClick(View view) {
        EditText et = new EditText(view.getContext());
        et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        et.setText(companyData.getPersonalEmail());
        et.requestFocus();

        AlertDialog builder= new AlertDialog.Builder(CompanyEditProfilePage.this)
            .setTitle("Edit Representative EmailId")
            .setMessage("Enter new Representative EmailId")
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
                String emailPattern1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email2=et.getText().toString().trim();
                if(et.getText().toString().length()==0)
                {
                    et.setError("This is Compulsory field");
                    return;
                }

                else if(!email2.matches(emailPattern1))
                {
                    et.setError("ENTER VALID MAIL");
                    return;
                }
                else
                {
                    builder.dismiss();
                    repre_emailid.setText(et.getText().toString().trim());
                    companyData.setPersonalEmail(et.getText().toString().trim());
                    HashMap<String, Object> changeDetail = new HashMap<>();
                    changeDetail.put("Personal Email", et.getText().toString().trim());
                    FirebaseFirestore.getInstance().collection("All Colleges")
                            .document(companyData.getCollegeId()).collection("UsersInfo")
                            .document(companyData.getEmail()).update(changeDetail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CompanyEditProfilePage.this, "Data saved Successfully!!", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    public void repre_numberClick(View view) {
        EditText et = new EditText(view.getContext());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setText(companyData.getPhone());
        et.requestFocus();

        AlertDialog builder = new AlertDialog.Builder(CompanyEditProfilePage.this)
            .setTitle("Edit Representative Contact Number")
            .setMessage("Enter new Representative Contact Number")
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
                else
                {
                    builder.dismiss();
                    repre_number.setText(et.getText().toString().trim());
                    companyData.setPhone(et.getText().toString().trim());
                    HashMap<String, Object> changeDetail = new HashMap<>();
                    changeDetail.put("Phone Number", et.getText().toString().trim());
                    FirebaseFirestore.getInstance().collection("All Colleges")
                            .document(companyData.getCollegeId()).collection("UsersInfo")
                            .document(companyData.getEmail()).update(changeDetail)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(CompanyEditProfilePage.this, "Data saved Successfully!!", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
    public void nonchangeable(View view) {
        Toast.makeText(this, "This field in non-Editable!!", Toast.LENGTH_LONG).show();
    }

    public void changeProfile(View view) {
        progressbar.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(companyData.getCollegeId()).child("Photograph").child(companyData.getEmail());
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(CompanyEditProfilePage.this)
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
            StorageReference storageRef=storage.getReference(companyData.getCollegeId()).child(q);
            StorageReference fileReference =storageRef.child((companyData.getEmail()));
            fileReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
                    String uploadid=databaseReference.push().getKey();
                    if(dialog.isShowing())
                        dialog.dismiss();

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(companyData.getCollegeId()).child("Photograph").child(companyData.getEmail());
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(CompanyEditProfilePage.this)
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
                    Toast.makeText(CompanyEditProfilePage.this,e.getMessage(),Toast.LENGTH_LONG).show();
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

