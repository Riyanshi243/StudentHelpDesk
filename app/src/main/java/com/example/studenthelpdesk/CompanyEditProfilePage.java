package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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
        location.setText(companyData.getLocation());
        repre_name.setText(companyData.getName());
        repre_emailid.setText(companyData.getPersonalEmail());
        repre_number.setText(companyData.getPhone());

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
        AlertDialog.Builder change = new AlertDialog.Builder(this);
        change.setTitle("Edit Company Name");
        change.setMessage("Enter new Company Name");
        EditText et = new EditText(view.getContext());
        et.setText(companyData.getCompanyName());
        et.requestFocus();
        change.setView(et);

        change.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        }).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (et.getText().toString().length() == 0) {
                    et.setError("ENTER COMPANY NAME");
                }
                else {
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


        change.create().show();


}

    public void locationClick(View view) {
        AlertDialog.Builder change = new AlertDialog.Builder(this);
        change.setTitle("Edit Company Location");
        change.setMessage("Enter new Company Location");
        EditText et = new EditText(view.getContext());
        et.setText(companyData.getLocation());
        et.requestFocus();
        change.setView(et);
        change.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        }).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save in database
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
        });
        change.create().show();
    }

    public void repre_nameClick(View view) {
        AlertDialog.Builder change = new AlertDialog.Builder(this);
        change.setTitle("Edit Representative Name");
        change.setMessage("Enter new Representative Name");
        EditText et = new EditText(view.getContext());
        et.setText(companyData.getName());
        et.requestFocus();
        change.setView(et);
        change.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        }).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save in database
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
        });
        change.create().show();
    }

    public void repre_emailClick(View view) {
        AlertDialog.Builder change = new AlertDialog.Builder(this);
        change.setTitle("Edit Representative EmailId");
        change.setMessage("Enter new Representative EmailId");
        EditText et = new EditText(view.getContext());
        et.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        et.setText(companyData.getPersonalEmail());
        et.requestFocus();
        change.setView(et);
        change.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        }).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save in database
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
        });
        change.create().show();
    }

    public void repre_numberClick(View view) {
        AlertDialog.Builder change = new AlertDialog.Builder(this);
        change.setTitle("Edit Representative Contact Number");
        change.setMessage("Enter new Representative Contact Number");
        EditText et = new EditText(view.getContext());
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        et.setText(companyData.getPhone());
        et.requestFocus();
        change.setView(et);
        change.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        }).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //save in database
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
        });
        change.create().show();
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
    boolean checkFilled()
    {
        ProgressBar pbar =findViewById(R.id.progressBar5);
        pbar.setVisibility(View.VISIBLE);
        if(cname.getText().toString().length()==0)
        {
            cname.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            return false;
        }
        if(cemail.getText().toString().length()==0)
        {
            cemail.setError("ENTER COMPANY EMAIL");
            cemail.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            return false;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email1=cemail.getText().toString().trim();
        if(!email1.matches(emailPattern))
        {
            cemail.setError("ENTER VALID MAIL");
            cemail.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            return false;
        }
        if(location.getText().toString().length()==0)
        {
            location.setError("ENTER LOCATION");
            location.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            return false;
        }
        if(repre_name.getText().toString().length()==0)
        {
            repre_name.setError("ENTER REPRESENTATIVE NAME");
            repre_name.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            return false;
        }
        if(repre_emailid.getText().toString().length()==0)
        {
            repre_emailid.setError("ENTER REPRESENTATIVE EMAIL ID");
            repre_emailid.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            return false;
        }
        String emailPattern1 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String email2=repre_emailid.getText().toString().trim();
        if(!email2.matches(emailPattern1))
        {
            repre_emailid.setError("ENTER VALID MAIL");
            repre_emailid.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            return false;
        }
        if(repre_number.getText().toString().length()==0)
        {
            repre_number.setError("ENTER PHONE NUMBER");
            repre_number.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            return false;
        }
        if(repre_number.getText().toString().length()<10 || repre_number.getText().toString().trim().length()>10)
        {
            repre_number.setError("INVALID PHONE NUMBER");
            repre_number.requestFocus();
            pbar.setVisibility(View.INVISIBLE);
            return false;
        }

   return true;
    }
}

