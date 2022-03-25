package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AdminViewAllStudentData extends AppCompatActivity {
Timer t;
    static ArrayList<CollegeRegisterQuestions> personalQ,academicQ,uploadQ;
    AutoCompleteTextView sortin;
    static AdminData adminData;
    TableLayout tl;
    static int domain=0;
    static int k=0;
    static int s=0;
    static ArrayList<StudentData> allStudentData=new ArrayList<>();
    static ArrayList<StudentData> allStudentDatatemp=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_student_data);
        sortin=findViewById(R.id.sortin);
        adminData=AdminPage.adminData;
        tl=findViewById(R.id.TableLayout);
        personalQ=new ArrayList<>();
        academicQ=new ArrayList<>();
        uploadQ=new ArrayList<>();
        String[] sortinList={"Ascending Order","Descending Order"}; //Ascending = 0 and descending =1
        ArrayAdapter spinnerList=new ArrayAdapter(this,android.R.layout.simple_spinner_item,sortinList);
        sortin.setAdapter(spinnerList);
        sortin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String what="";
                if(domain==0)
                {
                    what=personalQ.get(k).getQuestion();
                }
                else
                    what=academicQ.get(k).getQuestion();
                if(i==0) {
                    s = 0;
                    Toast.makeText(AdminViewAllStudentData.this, "Sorting in ascending order of "+what, Toast.LENGTH_SHORT).show();
                }
                else {
                    s = 2;
                    Toast.makeText(AdminViewAllStudentData.this, "Sorting in descending  order of "+what, Toast.LENGTH_SHORT).show();
                }

                tl.removeAllViews();
                show();
                sort();
            }
        });
        DocumentReference personalDetails= FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Questions").document("Personal Question");
        personalDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                long total=(long)documentSnapshot.get("Total");
                for (int i=0;i<total;i++)
                {
                    int finalI = i;
                    int finalI1 = i;
                    personalDetails.collection(i+"").document(i+"")
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            CollegeRegisterQuestions crq=new CollegeRegisterQuestions();
                            crq.setQuestion((String) documentSnapshot.get("Question"));
                            long x= (long) documentSnapshot.get("Type");
                            crq.setType((int) x);
                            crq.setId(finalI);
                            personalQ.add(crq);
                            if(finalI1 ==total-1)
                            {
                                DocumentReference academicDetails=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Questions").document("Academic Question");
                                academicDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        long total= (long) documentSnapshot.get("Total");
                                        for (int i=0;i<total;i++)
                                        {
                                            int finalI2 = i;
                                            academicDetails.collection(i+"").document(i+"")
                                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    CollegeRegisterQuestions crq=new CollegeRegisterQuestions();
                                                    crq.setQuestion((String) documentSnapshot.get("Question"));
                                                    long x= (long) documentSnapshot.get("Type");
                                                    crq.setType((int) x);
                                                    crq.setId(finalI2);
                                                    academicQ.add(crq);
                                                    if(finalI2 ==total-1)
                                                    {
                                                        DocumentReference uploadDetails=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Questions").document("Upload Question");
                                                        uploadDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                long total=(long)documentSnapshot.get("Total");
                                                                for(int i=0;i<total;i++)
                                                                {
                                                                    int finalI3 = i;
                                                                    int finalI4 = i;
                                                                    uploadDetails.collection(i+"").document(i+"")
                                                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            CollegeRegisterQuestions crq=new CollegeRegisterQuestions();
                                                                            crq.setQuestion((String) documentSnapshot.get("Question"));
                                                                            long x= (long) documentSnapshot.get("Type");
                                                                            crq.setType((int) x);
                                                                            crq.setId(finalI3);
                                                                            uploadQ.add(crq);
                                                                            if(finalI4 ==total-1) {
                                                                                show();
                                                                                //get(adminData);
                                                                            }
                                                                        }
                                                                    });

                                                                }

                                                            }
                                                        });
                                                    }

                                                }
                                            });

                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        t=new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Log.e("Student",allStudentData.size()+"");
                if(0<allStudentDatatemp.size())
                {
                    StudentData thisStudent=allStudentDatatemp.get(0);
                    allStudentDatatemp.remove(thisStudent);
                    ArrayList<CollegeRegisterQuestions> personalAnswers = thisStudent.getPersonal_ques();
                    ArrayList<CollegeRegisterQuestions> academicAnswers = thisStudent.getAcademic_ques();
                    TableRow tr=new TableRow(AdminViewAllStudentData.this);
                    for (CollegeRegisterQuestions p:personalAnswers)
                    {
                        View v=getLayoutInflater().inflate(R.layout.repeatable_table_content,null);
                        String ans=p.getAnswer();
                        TextView ansT=v.findViewById(R.id.table_content);
                        ansT.setMovementMethod(new ScrollingMovementMethod());
                        ansT.setText(ans);
                        tr.addView(v);

                    }
                    for (CollegeRegisterQuestions a:academicAnswers)
                    {
                        View v=getLayoutInflater().inflate(R.layout.repeatable_table_content,null);
                        String ans=a.getAnswer();
                        TextView ansT=v.findViewById(R.id.table_content);
                        ansT.setMovementMethod(new ScrollingMovementMethod());
                        ansT.setText(ans);
                        tr.addView(v);
                    }
                    for (int i=0;i<uploadQ.size();i++)
                    {
                        View v=getLayoutInflater().inflate(R.layout.repeatable_table_upload_view,null);
                        Button preview=v.findViewById(R.id.view);
                        int finalI = i;
                        int finalI1 = i;
                        preview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(uploadQ.get(finalI).getQuestion().equalsIgnoreCase("Photograph"))
                                {
                                    AlertDialog.Builder ab=new AlertDialog.Builder(AdminViewAllStudentData.this);
                                    ImageView profilepic=new ImageView(AdminViewAllStudentData.this);
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(adminData.getCollegeId()).child("Photograph").child(thisStudent.getEmail());
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(AdminViewAllStudentData.this)
                                                    .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .error(R.drawable.error_profile_picture)
                                                    .placeholder(R.drawable.default_loading_img)
                                                    .into(profilepic);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AdminViewAllStudentData.this,"Loading Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    ab.setView(profilepic);
                                    ab.create().show();
                                }
                                else
                                {
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReference(adminData.getCollegeId()).child(uploadQ.get(finalI1).getQuestion()).child(thisStudent.getEmail());
                                    Task<Uri> message = storageRef.getDownloadUrl();
                                    message.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Intent intent = new Intent(view.getContext(), ViewPDFActivity.class);
                                            intent.putExtra("url", uri.toString());
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AdminViewAllStudentData.this, "No document Uploaded", Toast.LENGTH_SHORT).show();
                                           // progressbar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }


                            }
                        });
                        tr.addView(v);
                    }
                    tl.post(new Runnable() {
                        @Override
                        public void run() {
                            tl.addView(tr);
                        }
                    });

                }
                /*else if(initialSort==true && allStudentData.size()>0)
                {
                    tl.post(new Runnable() {
                        @Override
                        public void run() {
                            if(tl!=null)
                                tl.removeAllViews();
                        }
                    });
                    show();
                    sort();
                    Log.e("Riya","INITIAL SORT");
                    initialSort=false;
                }*/
            }
        },1000,10);
    }
    public void filters(View v){
        startActivity(new Intent(AdminViewAllStudentData.this, AdminViewAllStudentDataFilters.class));
    }
    public void downloadExcel(View v)
    {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
        ab.setTitle("Enter the name of Excel File");
        EditText et=new EditText(this);
        ab.setView(et);
        ab.setMessage("Upload Details wont be downloaded here");
        ab.setPositiveButton("Download", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String fileName=et.getText().toString().trim()+".xls";
                String sheetName=et.getText().toString();
                File filePath=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),fileName);
                try {

                    if (!filePath.exists()){
                        filePath.getParentFile().mkdirs();
                        filePath.createNewFile();
                        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
                        HSSFSheet hssfSheet = hssfWorkbook.createSheet(sheetName);
                        ProgressBar progressDialog=new ProgressBar(AdminViewAllStudentData.this);
                       // progressDialog.(false);
                        //progressDialog.setTitle("Downloading Data");
                        //progressDialog.show();
                        putData(hssfSheet,progressDialog);
                        FileOutputStream fileOutputStream= new FileOutputStream(filePath);
                        hssfWorkbook.write(fileOutputStream);

                        if (fileOutputStream!=null){
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        Toast.makeText(AdminViewAllStudentData.this,"File stored at"+filePath.getPath(),Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(AdminViewAllStudentData.this,"File Already Exists",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("File path",e.toString());
                }

            }
        });
        ab.create().show();
       /* Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(filePath),"application/vnd.ms-excel");
        startActivity(intent);*/
    }
    public void putData(HSSFSheet thisSheet,ProgressBar progressDialog)
    {
        HSSFRow heading=thisSheet.createRow(0);
        int c=0;
        int rows=allStudentData.size()+1;
        progressDialog.setProgress(1/rows);
        for(int i=0;i<personalQ.size();i++)
        {
            HSSFCell thisCell=heading.createCell(c++);
            thisCell.setCellValue(personalQ.get(i).getQuestion());
        }
        for(int i=0;i<academicQ.size();i++)
        {
            HSSFCell thisCell=heading.createCell(c++);
            thisCell.setCellValue(academicQ.get(i).getQuestion());
        }
        for(int i=0;i<allStudentData.size();i++)
        {
            StudentData thisStudent = allStudentData.get(i);

            progressDialog.setProgress(i+2/rows);
            HSSFRow thisRow=thisSheet.createRow(i+1);
            int c1=0;
            ArrayList<CollegeRegisterQuestions> personalAnswers = thisStudent.getPersonal_ques();
            ArrayList<CollegeRegisterQuestions> academicAnswers=thisStudent.getAcademic_ques();
            for (CollegeRegisterQuestions p:personalAnswers)
            {
                String ans=p.getAnswer();
                HSSFCell thisCell=thisRow.createCell(c1++);
                thisCell.setCellValue(ans);
            }
            for (CollegeRegisterQuestions p:academicAnswers)
            {
                String ans=p.getAnswer();
                HSSFCell thisCell=thisRow.createCell(c1++);
                thisCell.setCellValue(ans);
            }

        }

    }
    public static void get(AdminData adminData)
    {
        //sort all the questions based on id
        CollectionReference studentsAll = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo");
        studentsAll.orderBy("Name", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> students = queryDocumentSnapshots.getDocuments();
                for (int i=0;i<students.size();i++)
                {
                    DocumentSnapshot d=students.get(i);
                    String cat= (String) d.get("Category");
                    if(cat.equalsIgnoreCase("Student")==false) {
                        continue;
                    }
                    StudentData thisStudent=new StudentData();
                    String email=d.getId();
                    thisStudent.setEmail(email);
                    thisStudent.setName((String) d.get("Name"));
                    Log.e("Name in order",thisStudent.getName());
                    ArrayList<CollegeRegisterQuestions> studentPersonalDetails=new ArrayList<>();
                    CollectionReference personalQuesDoc = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(email).collection("Personal Question");
                    int finalI = i;
                    personalQuesDoc.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> personalDetails = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot p:personalDetails) {
                                CollegeRegisterQuestions q=new CollegeRegisterQuestions();
                                q.setId(Integer.parseInt(p.getId()));
                                q.setAnswer((String) p.get("Answer"));
                                studentPersonalDetails.add(q);

                                if(studentPersonalDetails.size()==personalDetails.size())
                                {
                                    Collections.sort(studentPersonalDetails, new Comparator<CollegeRegisterQuestions>() {
                                        @Override
                                        public int compare(CollegeRegisterQuestions collegeRegisterQuestions, CollegeRegisterQuestions t1) {
                                            return collegeRegisterQuestions.getId()-t1.getId();
                                        }
                                    });
                                    thisStudent.setPersonal_ques(studentPersonalDetails);

                                    ArrayList<CollegeRegisterQuestions> studentAcademicDetails=new ArrayList<>();

                                    //once done
                                    CollectionReference academicQuesDoc = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(email).collection("Academic Question");
                                    academicQuesDoc.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> academicDetails = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot a:academicDetails) {
                                                CollegeRegisterQuestions q=new CollegeRegisterQuestions();
                                                q.setId(Integer.parseInt(a.getId()));
                                                q.setAnswer((String) a.get("Answer"));
                                                studentAcademicDetails.add(q);
                                                if(studentAcademicDetails.size()==academicDetails.size()) {
                                                    Collections.sort(studentAcademicDetails, new Comparator<CollegeRegisterQuestions>() {
                                                        @Override
                                                        public int compare(CollegeRegisterQuestions collegeRegisterQuestions, CollegeRegisterQuestions t1) {
                                                            return collegeRegisterQuestions.getId() - t1.getId();
                                                        }
                                                    });
                                                    thisStudent.setAcademic_ques(studentAcademicDetails);
                                                    {
                                                        allStudentData.add(thisStudent);
                                                        Log.e("Adding",allStudentData.toString());
                                                        sort();
                                                        //allStudentDatatemp=new ArrayList<>(allStudentData);

                                                    }
                                                }
                                            }

                                        }
                                    });

                                }

                            }
                        }
                    });
                }
            }
        });
    }
    public static void sort()
    {
        Log.e("Answers before",allStudentData.toString());
        Collections.sort(allStudentData, new Comparator<StudentData>() {
            @Override
            public int compare(StudentData studentData, StudentData t1) {
                if(domain==0)
                {
                    ArrayList<CollegeRegisterQuestions> s1=studentData.getPersonal_ques();
                    ArrayList<CollegeRegisterQuestions> s2=t1.getPersonal_ques();
                    CollegeRegisterQuestions s11=s1.get(k);
                    CollegeRegisterQuestions s12=s2.get(k);
                    if(s==0)
                        return s11.getAnswer().toLowerCase(Locale.ROOT).compareTo(s12.getAnswer().toLowerCase(Locale.ROOT));
                    else
                        return s12.getAnswer().toLowerCase(Locale.ROOT).compareTo(s11.getAnswer().toLowerCase(Locale.ROOT));
                }
                else
                {
                    ArrayList<CollegeRegisterQuestions> s1=studentData.getAcademic_ques();
                    ArrayList<CollegeRegisterQuestions> s2=t1.getAcademic_ques();
                    CollegeRegisterQuestions s11=s1.get(k);
                    CollegeRegisterQuestions s12=s2.get(k);
                    if(s==0)
                        return s11.getAnswer().toLowerCase(Locale.ROOT).compareTo(s12.getAnswer().toLowerCase(Locale.ROOT));
                    else
                        return s12.getAnswer().toLowerCase(Locale.ROOT).compareTo(s11.getAnswer().toLowerCase(Locale.ROOT));
                }

            }
        });
        Log.e("Answers",allStudentData.toString());
        allStudentDatatemp=new ArrayList<>(allStudentData);
    }
    public void show()
    {
        //heading
        TableRow heading=new TableRow(this);
        Collections.sort(personalQ, new Comparator<CollegeRegisterQuestions>() {
            @Override
            public int compare(CollegeRegisterQuestions collegeRegisterQuestions, CollegeRegisterQuestions t1) {
                return collegeRegisterQuestions.getId()-t1.getId();
            }
        });
        Collections.sort(uploadQ, new Comparator<CollegeRegisterQuestions>() {
            @Override
            public int compare(CollegeRegisterQuestions collegeRegisterQuestions, CollegeRegisterQuestions t1) {
                return collegeRegisterQuestions.getId()-t1.getId();
            }
        });
        Collections.sort(academicQ, new Comparator<CollegeRegisterQuestions>() {
            @Override
            public int compare(CollegeRegisterQuestions collegeRegisterQuestions, CollegeRegisterQuestions t1) {
                return collegeRegisterQuestions.getId()-t1.getId();
            }
        });

        for(int i=0;i<personalQ.size();i++)
        {
            View headingname=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
            TextView name=headingname.findViewById(R.id.table_head);
            name.setText( personalQ.get(i).getQuestion());
            name.setMovementMethod(new ScrollingMovementMethod());
            heading.addView(headingname);
        }
        for(int i=0;i<academicQ.size();i++)
        {
            View headingname=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
            TextView name=headingname.findViewById(R.id.table_head);
            name.setText(academicQ.get(i).getQuestion());
            name.setMovementMethod(new ScrollingMovementMethod());
            heading.addView(headingname);
        }
        for(int i=0;i<uploadQ.size();i++)
        {
            View headingname=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
            TextView name=headingname.findViewById(R.id.table_head);
            name.setText(uploadQ.get(i).getQuestion());
            name.setMovementMethod(new ScrollingMovementMethod());
            heading.addView(headingname);
        }
        tl.addView(heading);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        t.purge();
        t.cancel();
    }
}