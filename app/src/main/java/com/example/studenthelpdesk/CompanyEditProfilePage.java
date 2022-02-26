package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class CompanyEditProfilePage extends AppCompatActivity {
    TextView cname,cemail,location,repre_name,repre_emailid,repre_number;
    CompanyData companyData;
    ImageView profilepic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_edit_profile_page);
        companyData=CompanyPage.companyData;
        profilepic=findViewById(R.id.profilepic);
        cname=findViewById(R.id.cname);
        cemail=findViewById(R.id.cemail);
        location=findViewById(R.id.location);
        repre_name=findViewById(R.id.repre_name);
        repre_emailid=findViewById(R.id.repre_emailid);
        repre_number=findViewById(R.id.repre_number);
        cname.setText(companyData.getCompanyName());
        cemail.setText(companyData.getEmail());
        location.setText(companyData.getLocation());
        repre_name.setText(companyData.getName());
        repre_emailid.setText(companyData.getPersonalEmail());
        repre_number.setText(companyData.getPhone());

        StorageReference storageRef= FirebaseStorage.getInstance().getReference(companyData.getCollegeId()).child("Photograph").child(companyData.getEmail());
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

    public void cnameClick(View view)
    {
        AlertDialog.Builder change=new AlertDialog.Builder(this);
        change.setTitle("Edit Company Name");
        change.setMessage("Enter new Company Name");
        EditText et=new EditText(view.getContext());
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
                cname.setText(et.getText().toString().trim());
                companyData.setCompanyName(et.getText().toString().trim());
                HashMap<String,Object> changeDetail = new HashMap<>();
                changeDetail.put("Company Name",et.getText().toString().trim());
                FirebaseFirestore.getInstance().collection("All Colleges")
                        .document(companyData.getCollegeId()).collection("UsersInfo")
                        .document(companyData.getEmail()).update(changeDetail)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(CompanyEditProfilePage.this,"Data saved Successfully!!",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        change.create().show();
    }
    public void locationClick(View view)
    {
        AlertDialog.Builder change=new AlertDialog.Builder(this);
        change.setTitle("Edit Company Location");
        change.setMessage("Enter new Company Location");
        EditText et=new EditText(view.getContext());
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
            }
        });
        change.create().show();
    }
    public void repre_nameClick(View view)
    {
        AlertDialog.Builder change=new AlertDialog.Builder(this);
        change.setTitle("Edit Representative Name");
        change.setMessage("Enter new Representative Name");
        EditText et=new EditText(view.getContext());
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
            }
        });
        change.create().show();
    }
    public void repre_emailClick(View view)
    {
        AlertDialog.Builder change=new AlertDialog.Builder(this);
        change.setTitle("Edit Representative EmailId");
        change.setMessage("Enter new Representative EmailId");
        EditText et=new EditText(view.getContext());
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
            }
        });
        change.create().show();
    }
    public void repre_numberClick(View view)
    {
        AlertDialog.Builder change=new AlertDialog.Builder(this);
        change.setTitle("Edit Representative Contact Number");
        change.setMessage("Enter new Representative Contact Number");
        EditText et=new EditText(view.getContext());
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
            }
        });
        change.create().show();
    }
    public void nonchangeable(View view)
    {
        Toast.makeText(this,"This field in non-Editable!!",Toast.LENGTH_LONG).show();
    }
}