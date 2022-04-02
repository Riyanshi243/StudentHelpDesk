package com.example.studenthelpdesk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AdminOrCompanySendNotification extends AppCompatActivity {
    ArrayList<Uri> allAttach;
    TextView title,content,heading;
    AutoCompleteTextView audience;
    String token="",cId;
    AdminData adminData;
    CompanyData companyData;
    LinearLayout attach_ll;
    ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_or_company_send_notification);
        title=findViewById(R.id.notif_title);
        content=findViewById(R.id.notif_content);
        audience=findViewById(R.id.audience);
        heading=findViewById(R.id.heading);
        attach_ll=findViewById(R.id.linearlay);
        adminData=AdminPage.adminData;
        companyData=CompanyPage.companyData;
        progressbar=findViewById(R.id.progressBar5);
        allAttach=new ArrayList<>();
        cId="All";
        if (adminData!=null)
            cId=adminData.getCollegeId();
        else
            cId=companyData.getCollegeId();
             //dropdown menu
        FirebaseFirestore.getInstance().collection("All Colleges")
                .document(cId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String collgName= (String) documentSnapshot.get("Name");
                heading.setText("These notifications will be sent to the desired audience of college "+collgName);

            }
        });

        ArrayList<String> audienceChoice=new ArrayList<>();
        audienceChoice.add("All");
        audienceChoice.add("Student");
        audienceChoice.add("Admin");
        audienceChoice.add("Company");
        ArrayAdapter spinnerListAudience = new ArrayAdapter(this, android.R.layout.simple_spinner_item, audienceChoice);
        audience.setAdapter(spinnerListAudience);
        String finalCId = cId;
        audience.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    token=finalCId;
                    LinearLayout add1=findViewById(R.id.add);
                    add1.removeAllViews();
                }
                else if(i==1)
                {

                    LinearLayout add1=findViewById(R.id.add);
                    add1.removeAllViews();
                    Spinner studentAudience=new Spinner(AdminOrCompanySendNotification.this);
                    LinearLayout ll2=new LinearLayout(AdminOrCompanySendNotification.this);
                    ll2.setOrientation(LinearLayout.HORIZONTAL);
                    DocumentReference allBranches=FirebaseFirestore.getInstance().collection("All Colleges").document(cId);
                    allBranches.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String> courses = (ArrayList<String>) documentSnapshot.get("Courses");
                            courses.add(0,"All");
                            ArrayAdapter spinnerListCourse = new ArrayAdapter(AdminOrCompanySendNotification.this, android.R.layout.simple_spinner_item, courses);
                            studentAudience.setAdapter(spinnerListCourse);
                            studentAudience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    if(i==0)
                                    {
                                        token="Student"+cId;
                                        ll2.removeAllViews();
                                    }
                                    else {
                                        ll2.removeAllViews();
                                        String selectedCourse = courses.get(i);
                                        token=cId+"_"+selectedCourse;
                                        DocumentReference branch = FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("Branches").document(selectedCourse);
                                        branch.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                ArrayList<String> allbranch = (ArrayList<String>) documentSnapshot.get("Branches");
                                                allbranch.add(0, "All");
                                                ArrayAdapter spinnerListBranch = new ArrayAdapter(AdminOrCompanySendNotification.this, android.R.layout.simple_spinner_item, allbranch);
                                                Spinner branches = new Spinner(AdminOrCompanySendNotification.this);
                                                branches.setAdapter(spinnerListBranch);
                                                LinearLayout ll3=new LinearLayout(AdminOrCompanySendNotification.this);
                                                ll3.setOrientation(LinearLayout.HORIZONTAL);
                                                branches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                    @Override
                                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                        if(i==0)
                                                        {
                                                            token=cId+"_"+selectedCourse;
                                                            ll3.removeAllViews();
                                                        }
                                                        else
                                                        {
                                                            String selectedBranch=allbranch.get(i);
                                                            ll3.removeAllViews();
                                                            ArrayList<String> yr=new ArrayList<>();
                                                            yr.add("All");
                                                            yr.add("1");
                                                            yr.add("2");
                                                            yr.add("3");
                                                            yr.add("4");
                                                            yr.add("5");
                                                            yr.add("5+");
                                                            ArrayAdapter spinnerListYear = new ArrayAdapter(AdminOrCompanySendNotification.this, android.R.layout.simple_spinner_item,yr);
                                                            Spinner yrS=new Spinner(AdminOrCompanySendNotification.this);
                                                            yrS.setAdapter(spinnerListYear);
                                                            yrS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                @Override
                                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                                    if(i==0)
                                                                    {
                                                                     token=cId+"_"+selectedCourse+"_"+selectedBranch;
                                                                    }
                                                                    else
                                                                    {
                                                                        token=cId+"_"+selectedCourse+"_"+selectedBranch+"_"+yr.get(i);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onNothingSelected(AdapterView<?> adapterView) {
                                                                    token=cId+"_"+selectedCourse+"_"+selectedBranch;
                                                                }
                                                            });
                                                            ll3.addView(yrS);
                                                        }
                                                    }

                                                    @Override
                                                    public void onNothingSelected(AdapterView<?> adapterView) {
                                                        token=cId+"_"+selectedCourse;
                                                    }
                                                });
                                                ll2.addView(branches);
                                                ll2.addView(ll3);

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    token="Student"+cId;
                                }
                            });
                        }
                    });
                    add1.addView(studentAudience);
                    add1.addView(ll2);

                }
                else if(i==2)
                {
                    LinearLayout add1=findViewById(R.id.add);
                    add1.removeAllViews();
                    Spinner adminAudience=new Spinner(AdminOrCompanySendNotification.this);
                    DocumentReference allDepts=FirebaseFirestore.getInstance().collection("All Colleges").document(cId);
                    allDepts.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String> dept = (ArrayList<String>) documentSnapshot.get("Departments");
                            dept.add(0,"All");
                            ArrayAdapter spinnerListDept=new ArrayAdapter(AdminOrCompanySendNotification.this, android.R.layout.simple_spinner_item,dept);
                            adminAudience.setAdapter(spinnerListDept);
                            adminAudience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if(i==0)
                                        token="Admin_"+cId;
                                    else
                                        token=cId+"_"+dept.get(i).replaceAll("\\s", "");
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    token="Admin_"+cId;
                                }
                            });
                        }
                    });
                    add1.addView(adminAudience);
                }
                else if(i==3)
                {
                    token="Company_"+cId;
                }

            }
        });


    }
    //attach Notification
    public void attachNotif(View v)
    {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(intent,1);
    }

    ProgressDialog dialog;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            dialog = new ProgressDialog(this);
            dialog.setMessage("UPLOADING");
            dialog.setCancelable(false);
            dialog.show();
            getAttachment(data.getData());
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this,"ACTIVITY CANCELLED",Toast.LENGTH_SHORT).show();
            progressbar.setVisibility(View.INVISIBLE);
        }
    }
     // send Notification
    public void sendNotif(View v)
    {
        String t1=token;
        Date t=Calendar.getInstance().getTime();
        String dateToday=(String.valueOf(t.getDate()))+"-"+String.valueOf(t.getMonth()+1)+"-"+String.valueOf(t.getYear()).substring(1,3);
        String timeStamp=t.toString();
        if(title.getText().toString().length()==0)
        {
            title.setError("Give a title");
            return;
        }
        if(audience.getText().toString().equalsIgnoreCase("Choose desired audience of notification"))
        {
            audience.setError("Select the audience");
            return;
        }
        ArrayList<String> fileName=new ArrayList<>();
        String name;
        if (adminData!=null) {
            name = adminData.getAdminName();
        }
        else
            name=companyData.getCompanyName();

        if(allAttach.size()==0)
        {
            FirebaseFirestore fs=FirebaseFirestore.getInstance();
            DocumentReference docNotif = fs.collection("All Colleges").document(cId).collection("Notification").document(FirebaseAuth.getInstance().getUid().toString() + "_" + timeStamp);
            HashMap<String,Object> notifMap=new HashMap<>();
            notifMap.put("Title",title.getText().toString());
            notifMap.put("Content",content.getText().toString());
            notifMap.put("Sender",name);
            notifMap.put("Sender mail",FirebaseAuth.getInstance().getCurrentUser().getEmail());
            notifMap.put("Token",token);
            notifMap.put("Timestamp",t);
            notifMap.put("Attachment",null);

            docNotif.set(notifMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    allAttach.clear();
                    token="/topics/"+token;
                    FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,title.getText().toString(),content.getText().toString(),AdminOrCompanySendNotification.this,"Notification");
                    notificationsSender.SendNotifications();
                    token=t1;
                    title.setText("");
                    content.setText("");
                    attach_ll.removeAllViews();
                    LinearLayout add1=findViewById(R.id.add);
                    audience.setText("Choose desired audience of notification");
                    add1.removeAllViews();
                    Toast.makeText(AdminOrCompanySendNotification.this,"Notif sent",Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            for(int i=0;i<allAttach.size();i++)
            {
                fileName.add(getNameFromURI(allAttach.get(i)));
                StorageReference sr=FirebaseStorage.getInstance().getReference(cId).child("Notification_"+cId).child(FirebaseAuth.getInstance().getUid()+"_"+timeStamp).child(getNameFromURI(allAttach.get(i)));
                int finalI = i;
                sr.putFile(allAttach.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        if(finalI ==allAttach.size()-1)
                        {
                            FirebaseFirestore fs=FirebaseFirestore.getInstance();
                            DocumentReference docNotif = fs.collection("All Colleges").document(cId).collection("Notification").document(FirebaseAuth.getInstance().getUid().toString() + "_" + timeStamp);
                            HashMap<String,Object> notifMap=new HashMap<>();
                            notifMap.put("Title",title.getText().toString());
                            notifMap.put("Content",content.getText().toString());
                            notifMap.put("Sender",name);
                            notifMap.put("Sender mail",FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            notifMap.put("Token",token);
                            notifMap.put("Timestamp",t);
                            notifMap.put("Attachment",fileName);
                            notifMap.put("Notif Location",FirebaseAuth.getInstance().getUid()+"_"+timeStamp);
                            docNotif.set(notifMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    token="/topics/"+token;
                                    FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,title.getText().toString(),content.getText().toString(),AdminOrCompanySendNotification.this,"Notification");
                                    notificationsSender.SendNotifications();
                                    token=t1;
                                    title.setText("");
                                    content.setText("");
                                    attach_ll.removeAllViews();
                                    allAttach.clear();
                                    LinearLayout add1=findViewById(R.id.add);
                                    audience.setText("Choose desired audience of notification");
                                    add1.removeAllViews();
                                    Toast.makeText(AdminOrCompanySendNotification.this,"Notif sent",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        }
    }
    // function for getting the notification
    public void getAttachment(Uri imageuri2){
        if(imageuri2!=null)
        {
            TextView imageName=new TextView(this);
            imageName.setText(getNameFromURI(imageuri2));
            attach_ll.addView(imageName);
            allAttach.add(imageuri2);
            dialog.dismiss();
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

    public String getNameFromURI(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        @SuppressLint("Range") String string = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        return string;
    }

}