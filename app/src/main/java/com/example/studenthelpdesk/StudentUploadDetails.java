package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class StudentUploadDetails extends AppCompatActivity {
    StudentData studentData;
    LinearLayout ll;
    View currentQView;
    ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_upload_details);
        ll=findViewById(R.id.ll);
        studentData=StudentPage.studentData;
        progressbar=findViewById(R.id.progressBar5);

        ArrayList<CollegeRegisterQuestions> quesAns = studentData.getUpload_ques();
        for(CollegeRegisterQuestions a:quesAns)
        {
            if(a.getQuestion().equalsIgnoreCase("Photograph"))
            {
                View repeatAnswers=getLayoutInflater().inflate(R.layout.repeatable_photograph,null);
                TextView ques=repeatAnswers.findViewById(R.id.Ques);
                ques.setText(a.getQuestion());
                ImageView profilepic=repeatAnswers.findViewById(R.id.imageView6);
                Button edit=repeatAnswers.findViewById(R.id.button5);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(studentData.getCollegeid()).child("Photograph").child(studentData.getEmail());
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(StudentUploadDetails.this)
                                .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.drawable.error_profile_picture)
                                .placeholder(R.drawable.default_loading_img)
                                .into(profilepic);

                    }
                });
                profilepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressbar.setVisibility(View.VISIBLE);
                        currentQView=repeatAnswers;
                        Intent intent=new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,1);
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(studentData.getCollegeid()).child("Photograph").child(studentData.getEmail());
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(StudentUploadDetails.this)
                                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.drawable.error_profile_picture)
                                        .placeholder(R.drawable.default_loading_img)
                                        .into(profilepic);

                            }
                        });
                    }
                });
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressbar.setVisibility(View.VISIBLE);
                        currentQView=repeatAnswers;
                        Intent intent=new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,1);
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(studentData.getCollegeid()).child("Photograph").child(studentData.getEmail());
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(StudentUploadDetails.this)
                                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.drawable.error_profile_picture)
                                        .placeholder(R.drawable.default_loading_img)
                                        .into(profilepic);

                            }
                        });
                    }
                });
                ll.addView(repeatAnswers);

            }
            else
            {
                View repeatAnswers=getLayoutInflater().inflate(R.layout.repeatable_student_uploaded_documents,null);
                TextView ques=repeatAnswers.findViewById(R.id.Ques);
                Button view =repeatAnswers.findViewById(R.id.view);
                Button update= repeatAnswers.findViewById(R.id.update);
                Button download=repeatAnswers.findViewById(R.id.download);
                ques.setText(a.getQuestion());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressbar.setVisibility(View.VISIBLE);
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference(studentData.getCollegeid()).child(a.getQuestion()).child(studentData.getEmail());
                        Task<Uri> message = storageRef.getDownloadUrl();
                        message.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Toast.makeText(getActivity(),uri.toString(),Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(view.getContext(), ViewPDFActivity.class);
                                intent.putExtra("url", uri.toString());
                                startActivity(intent);
                                progressbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(a.isChangeable()==true)
                        {
                            progressbar.setVisibility(View.VISIBLE);
                            currentQView=repeatAnswers;
                            Intent intent=new Intent();
                            intent.setType("application/pdf");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent,2);
                        }
                        else
                        {
                            AlertDialog.Builder ab=new AlertDialog.Builder(StudentUploadDetails.this);
                            //Send Request for Data Change
                            ab.setTitle("This field is not editable");
                            ab.setMessage("If you want to change the data in the field, please send request to admin");
                            ab.setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent=new Intent(StudentUploadDetails.this,StudentSendRequestToChangeData.class);
                                    intent.putExtra("Details",a);
                                    startActivity(intent);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //do nothing
                                }
                            })
                                    .setCancelable(false);
                            ab.create().show();
                        }
                    }
                });
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressbar.setVisibility(View.VISIBLE);
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference(studentData.getCollegeid()).child(a.getQuestion()).child(studentData.getEmail());
                        Task<Uri> message = storageRef.getDownloadUrl();
                        message.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                progressbar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });

                ll.addView(repeatAnswers);
            }


        }
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
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                dialog = new ProgressDialog(this);

                dialog.setMessage("UPLOADING");
                dialog.setCancelable(false);
                dialog.show();
                TextView q=currentQView.findViewById(R.id.Ques);
                uploadResume(data.getData(),q.getText().toString());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"ACTIVITY CANCELLED",Toast.LENGTH_SHORT).show();
                progressbar.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void uploadPic(Uri imageuri,String q) {

        if(imageuri!=null)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef=storage.getReference(studentData.getCollegeid()).child(q);
            StorageReference fileReference =storageRef.child((studentData.getEmail()));
            fileReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
                    String uploadid=databaseReference.push().getKey();
                    if(dialog.isShowing())
                        dialog.dismiss();
                    ImageView profilepic=currentQView.findViewById(R.id.imageView6);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(studentData.getCollegeid()).child("Photograph").child(studentData.getEmail());
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(StudentUploadDetails.this)
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
                    Toast.makeText(StudentUploadDetails.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
    public void uploadResume(Uri imageuri2,String q){
        if(imageuri2!=null)
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(studentData.getCollegeid()).child(q);
            final StorageReference filepath = storageReference.child(studentData.getEmail());
            filepath.putFile(imageuri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Upload upload=new Upload(imageuri2.toString()+"."+getFileExtension((imageuri2)));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
                    String uploadid=databaseReference.push().getKey();
                    //SignIn.data.setResume(uploadid);
                    if(dialog.isShowing()) {
                        dialog.setCancelable(false);
                        dialog.setMessage("UPLOADED");

                        Toast.makeText(StudentUploadDetails.this, filepath.getName()+".pdf SAVED", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1000);
                            progressbar.setVisibility(View.INVISIBLE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            progressbar.setVisibility(View.INVISIBLE);
                        }
                        dialog.dismiss();

                    }
                }
            });
        }
        else
        {
            Toast.makeText(this,"NO pdf SELECTED",Toast.LENGTH_LONG).show();
            progressbar.setVisibility(View.INVISIBLE);
        }
    }
    private  String getFileExtension(Uri uri)
    {
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    @SuppressLint("Range")
    public String getNameFromURI(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        String string = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        return string;
    }

}