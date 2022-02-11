package com.example.studenthelpdesk;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_upload_details);
        ll=findViewById(R.id.ll);
        studentData=StudentPage.studentData;
        String s="";
        ArrayList<CollegeRegisterQuestions> quesAns = studentData.getUpload_ques();
        for(CollegeRegisterQuestions a:quesAns)
        {
            View repeatAnswers=getLayoutInflater().inflate(R.layout.repeatable_student_uploaded_documents,null);
            TextView ques=repeatAnswers.findViewById(R.id.Ques);
            Button view =repeatAnswers.findViewById(R.id.view);
            Button update= repeatAnswers.findViewById(R.id.update);
            Button download=repeatAnswers.findViewById(R.id.download);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                        }
                    });
                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentQView=repeatAnswers;
                    Intent intent=new Intent();
                    intent.setType("application/pdf");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,2);
                }
            });
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference(studentData.getCollegeid()).child(a.getQuestion()).child(studentData.getEmail());
                    Task<Uri> message = storageRef.getDownloadUrl();
                    message.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
                }
            });
            ques.setText(a.getQuestion());
            ll.addView(repeatAnswers);

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
                dialog.show();
                Uri imageuri = data.getData();
               // TextView q = currentQView.findViewById(R.id.Ques);
                //uploadPic(imageuri, q.getText().toString());
                //TextView ans = currentQView.findViewById(R.id.document_name);
               // ans.setText(getNameFromURI(data.getData()));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "ACTIVITY CANCELLED", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                dialog = new ProgressDialog(this);
                dialog.setMessage("UPLOADING");
                dialog.show();
                TextView q=currentQView.findViewById(R.id.Ques);
                uploadResume(data.getData(),q.getText().toString());
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"ACTIVITY CANCELLED",Toast.LENGTH_SHORT).show();
            }
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
                        dialog.setMessage("UPLOADED");

                        Toast.makeText(StudentUploadDetails.this, filepath.getName()+".pdf SAVED", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        Button pdf= findViewById(R.id.preview);
                        pdf.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this,"NO pdf SELECTED",Toast.LENGTH_LONG).show();
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