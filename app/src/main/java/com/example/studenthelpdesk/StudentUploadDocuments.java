package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class StudentUploadDocuments extends AppCompatActivity
{
    StudentData studentData;
    LinearLayout ll;
    View currentQView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_upload_documents);
        studentData=Signup.studentData;
        ll=findViewById(R.id.ll);
        Log.e("All p q","Hi"+Arrays.toString(studentData.getP_ans())+" "+Arrays.toString(studentData.getP_ques())+Arrays.toString(studentData.getP_id()));
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference persQuestions = f.collection("All Colleges").document(studentData.getCollegeid()).collection("Questions").document("Upload Question");
        persQuestions.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                long noOfQuestions= (long) documentSnapshot.get("Total");
                for(int i=0;i<noOfQuestions;i++)
                {
                    DocumentReference quesDetails = persQuestions.collection(i + "").document(i + "");
                    int finalI = i;
                    quesDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            boolean compulsory= (boolean) documentSnapshot.get("Compulsory");
                            String question= (String) documentSnapshot.get("Question");
                            long type=(long)documentSnapshot.get("Type");
                            View addQues=getType(type,question);
                            if(compulsory==true)
                            {
                                if(type==0)
                                    addQues.setId((int)8);
                                else
                                    addQues.setId((int) type);
                            }
                            else
                            {
                                if(type==0)
                                    addQues.setId((int)-8);
                                else
                                    addQues.setId((int) -type);
                            }
                            ll.addView(addQues);
                        }
                    });
                }
            }
        });
    }
    public void saveAndNext(View view)
    {
        int quesNumber=ll.getChildCount();
        int i;
        for(i=0;i<quesNumber;i++)
        {
            View v=ll.getChildAt(i);
            int id=v.getId();
            if(id>0)
            {
                TextView t=v.findViewById(R.id.Ques);
                String s = t.getText().toString();
                if(empty(v,id))
                {
                    return;
                }
            }
            if(i==quesNumber-1)
                startActivity(new Intent(StudentUploadDocuments.this,StudentSignupDone.class));

        }

    }
    boolean empty(View v,int i)
    {
        if(i==6)
        {
            //upload
            View nView=v;
            TextView ans=nView.findViewById(R.id.document_name);
            if(ans.length()==0)
            {
                ans.setError("This is compulsory");
                return true;
            }

        }

        return false;
    }
    View getType(long i,String q)
    {
        if(i==6)
        {
            //upload
            View nView=getLayoutInflater().inflate(R.layout.repeatable_upload_document,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setText(q);
            Button upload=nView.findViewById(R.id.upload);
            Button preview=nView.findViewById(R.id.preview);
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentQView=nView;
                    if(q.equalsIgnoreCase("Photograph"))
                        uploadPic();
                    else
                        uploadResume();
                }
            });
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            return nView;
        }

        return null;
    }
    public void uploadPic()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    public void uploadResume()
    {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,2);
    }
    ProgressDialog dialog;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
           if(resultCode==RESULT_OK) {
               dialog = new ProgressDialog(this);
               dialog.setMessage("Uploading");
               dialog.show();
               Uri imageuri = data.getData();
               TextView q = currentQView.findViewById(R.id.Ques);
               uploadPic(imageuri, q.getText().toString());
               TextView ans = currentQView.findViewById(R.id.document_name);
               ans.setText(getNameFromURI(data.getData()));
           }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(StudentUploadDocuments.this, "ACTIVITY CANCELLED", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                dialog = new ProgressDialog(this);
                dialog.setMessage("Uploading");
                dialog.show();
               // String q= data.getExtras().getBundle("Question").toString();
                TextView q=currentQView.findViewById(R.id.Ques);
                uploadResume(data.getData(),q.getText().toString());
                TextView ans=currentQView.findViewById(R.id.document_name);
                ans.setText(getNameFromURI(data.getData()));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(StudentUploadDocuments.this,"ACTIVITY CANCELLED",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("Range")
    public String getNameFromURI(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        String string = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        return string;
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
                        dialog.setMessage("Uploaded");
                        Toast.makeText(StudentUploadDocuments.this, filepath.getName()+".pdf SAVED", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this,"No PDF selected",Toast.LENGTH_LONG).show();
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

                    Upload upload=new Upload(imageuri.toString()+"."+getFileExtension((imageuri)));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
                    String uploadid=databaseReference.push().getKey();
                    if(dialog.isShowing())
                        dialog.dismiss();
                    //showPic();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StudentUploadDocuments.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(this,"No Image selected",Toast.LENGTH_LONG).show();
        }

    }
    private  String getFileExtension(Uri uri)
    {
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}