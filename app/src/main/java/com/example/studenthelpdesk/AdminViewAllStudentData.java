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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AdminViewAllStudentData extends AppCompatActivity implements Serializable  {
Timer t;
    static ArrayList<CollegeRegisterQuestions> personalQ,academicQ,uploadQ;
    AutoCompleteTextView sortin;
    static AdminData adminData;
    static TableLayout tl;
    static int domain=0;
    static int k=0;
    static int s=1;
    static ArrayList<StudentData> allStudentData=new ArrayList<>();
    static ArrayList<StudentData> allStudentDatatemp=new ArrayList<>();
    static HashMap<Integer,HashMap<Integer,String>> equal=new HashMap<>();
    static HashMap<Integer,HashMap<Integer,ArrayList<Double>>> range=new HashMap<>();
    static ArrayList<CollegeRegisterQuestions> allheadings=new ArrayList<>();
    static HashMap<String,ArrayList<String>> allCourseAndBranch=new HashMap<>();
    static HashMap<String,ArrayList<Boolean>> allCourseAndBranchShow=new HashMap<>();

    ProgressBar progressBar;
    static boolean flag=false,flag2=false;
    static int srno=1;

    static Boolean fromWhere=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_student_data);
        sortin=findViewById(R.id.sortin);
        adminData=AdminPage.adminData;
        progressBar=findViewById(R.id.progressBar5);
        tl=findViewById(R.id.TableLayout);
        personalQ=new ArrayList<>();
        academicQ=new ArrayList<>();
        uploadQ=new ArrayList<>();
        flag=false;
        flag2=false;
        fromWhere=false;
        allStudentDatatemp=new ArrayList<>();
        allStudentData=new ArrayList<>();
        allCourseAndBranch=new HashMap<>();
        allCourseAndBranchShow=new HashMap<>();
        allheadings=new ArrayList<>();
        equal=new HashMap<>();
        range=new HashMap<>();
        if(getIntent().hasExtra("From Data Requests"))
        {
            fromWhere= getIntent().getBooleanExtra("From Data Requests",false);
            LinearLayout LL=findViewById(R.id.ll_Filters);
            LL.setVisibility(View.GONE);
            allCourseAndBranchShow=AdminViewDataRequestsFromCompanyDetails.allCourseAndBranchRequest;
            equal=AdminViewDataRequestsFromCompanyDetails.equal;
            range=AdminViewDataRequestsFromCompanyDetails.range;
            allheadings=AdminViewDataRequestsFromCompanyDetails.allheadings;
        }



        s=0;k=0;
        srno=1;
        String[] sortinList={"Ascending Order","Descending Order"}; //Ascending = 0 and descending =1
        ArrayAdapter spinnerList=new ArrayAdapter(this,android.R.layout.simple_spinner_item,sortinList);
        sortin.setAdapter(spinnerList);
        sortin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String what="";
                if(progressBar.getVisibility()==View.VISIBLE)
                {

                    Toast.makeText(AdminViewAllStudentData.this,"Data is Loading.Please Wait",Toast.LENGTH_SHORT).show();
                    return;
                }
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
                srno=1;
                progressBar.setVisibility(View.VISIBLE);
                flag=false;
                show();
                sort();
            }
        });
         {
            DocumentReference personalDetails = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Questions").document("Personal Question");
            personalDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    long total = (long) documentSnapshot.get("Total");
                    for (int i = 0; i < total; i++) {
                        int finalI = i;
                        int finalI1 = i;
                        personalDetails.collection(i + "").document(i + "")
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                CollegeRegisterQuestions crq = new CollegeRegisterQuestions();
                                crq.setQuestion((String) documentSnapshot.get("Question"));
                                long x = (long) documentSnapshot.get("Type");
                                crq.setType((int) x);
                                crq.setId(finalI);
                                    personalQ.add(crq);
                                if(fromWhere==false)
                                    allheadings.add(crq);
                                if (personalQ.size() == total ) {
                                    DocumentReference academicDetails = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Questions").document("Academic Question");
                                    academicDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            long total2 = (long) documentSnapshot.get("Total");
                                            for (int i = 0; i < total2; i++) {
                                                int finalI2 = i;
                                                academicDetails.collection(i + "").document(i + "")
                                                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        CollegeRegisterQuestions crq = new CollegeRegisterQuestions();
                                                        crq.setQuestion((String) documentSnapshot.get("Question"));
                                                        long x = (long) documentSnapshot.get("Type");
                                                        crq.setType((int) x);
                                                        Log.e("Type original", x + " " + crq.getQuestion());
                                                        crq.setId(finalI2);
                                                            academicQ.add(crq);
                                                        if(fromWhere==false)
                                                            allheadings.add(crq);
                                                        if (academicQ.size()== total2) {
                                                            DocumentReference uploadDetails = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Questions").document("Upload Question");
                                                            uploadDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                    long total3 = (long) documentSnapshot.get("Total");
                                                                    for (int i3 = 0; i3 < total3; i3++) {
                                                                        int finalI3 = i3;
                                                                        uploadDetails.collection(i3 + "").document(i3 + "")
                                                                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                CollegeRegisterQuestions crq = new CollegeRegisterQuestions();
                                                                                crq.setQuestion((String) documentSnapshot.get("Question"));
                                                                                long x = (long) documentSnapshot.get("Type");
                                                                                crq.setType((int) x);
                                                                                crq.setId(finalI3);
                                                                                    uploadQ.add(crq);
                                                                                if(fromWhere==false)
                                                                                    allheadings.add(crq);
                                                                                if (uploadQ.size() == total3) {

                                                                                    show();
                                                                                    get(adminData);
                                                                                    flag2 = true;
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
        }
        getHeading();
        t=new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(allStudentDatatemp==null)
                    return;

                if(flag==false&&flag2==true)
                {
                    tl.post(new Runnable() {
                        @Override
                        public void run() {
                            show();
                            flag2=false;
                        }
                    });
                }
                if(0<allStudentDatatemp.size()&&flag)
                {
                    StudentData thisStudent=allStudentDatatemp.get(0);
                    allStudentDatatemp.remove(thisStudent);
                    if(allStudentDatatemp.size()==0) {
                        progressBar.post(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    if(allCourseAndBranchShow.containsKey(thisStudent.getCourse()))
                    {
                        int i=allCourseAndBranch.get(thisStudent.getCourse()).indexOf(thisStudent.getBranch());
                        if(allCourseAndBranchShow.get(thisStudent.getCourse()).get(i)==false) {

                            return;
                        }

                    }
                    ArrayList<CollegeRegisterQuestions> personalAnswers = thisStudent.getPersonal_ques();
                    ArrayList<CollegeRegisterQuestions> academicAnswers = thisStudent.getAcademic_ques();

                    if(equal.containsKey(0))
                    {
                        HashMap<Integer, String> quesMap = equal.get(0);
                        for(Integer quesNumber:quesMap.keySet())
                        {
                            String ans=personalAnswers.get(quesNumber).getAnswer();
                            if(ans.trim().equalsIgnoreCase(quesMap.get(quesNumber).trim())==false)
                            {
                                return;
                            }
                        }
                    }
                    if(equal.containsKey(1))
                    {
                        HashMap<Integer, String> quesMap = equal.get(1);
                        for(Integer quesNumber:quesMap.keySet())
                        {
                            String ans=academicAnswers.get(quesNumber).getAnswer();
                            if(ans.equalsIgnoreCase(quesMap.get(quesNumber))==false)
                            {
                                return;
                            }
                        }
                    }
                    if(range.containsKey(0))
                    {
                        HashMap<Integer, ArrayList<Double>> quesMap = range.get(0);
                        for(Integer quesNumber:quesMap.keySet())
                        {
                            double min=quesMap.get(quesNumber).get(0);
                            double max=quesMap.get(quesNumber).get(1);
                            double ans= Double.parseDouble(personalAnswers.get(quesNumber).getAnswer());
                            if(ans>max||ans<min)
                                return;
                        }
                    }
                    if(range.containsKey(1))
                    {
                    HashMap<Integer, ArrayList<Double>> quesMap = range.get(1);
                    for(Integer quesNumber:quesMap.keySet())
                    {
                        double min=quesMap.get(quesNumber).get(0);
                        double max=quesMap.get(quesNumber).get(1);
                        double ans= Double.parseDouble(academicAnswers.get(quesNumber).getAnswer());
                        if(ans>max||ans<min)
                            return;
                    }
                }

                    int srno1=srno++;
                    TableRow tr=new TableRow(AdminViewAllStudentData.this);
                    View v1=getLayoutInflater().inflate(R.layout.repeatable_table_content,null);
                    TextView ansSr=v1.findViewById(R.id.table_content);
                    ansSr.setMovementMethod(new ScrollingMovementMethod());
                    ansSr.setText(srno1+"");
                    tr.addView(v1);
                    View v2=getLayoutInflater().inflate(R.layout.repeatable_table_content,null);
                    TextView ansemail=v2.findViewById(R.id.table_content);
                    ansemail.setMovementMethod(new ScrollingMovementMethod());
                    ansemail.setText(thisStudent.getEmail());
                    Linkify.addLinks(ansemail, Linkify.ALL);
                    ansemail.setLinkTextColor(Color.parseColor("#034ABC"));
                    tr.addView(v2);
                    Log.e("All headings",allheadings.toString()+" "+personalQ.toString());

                    for (int i=0;i<personalAnswers.size();i++)
                    {
                        CollegeRegisterQuestions p = personalAnswers.get(i);
                        if(allheadings.contains(personalQ.get(i))==false)
                            continue;
                        View v=getLayoutInflater().inflate(R.layout.repeatable_table_content,null);
                        String ans=p.getAnswer();
                        TextView ansT=v.findViewById(R.id.table_content);
                        ansT.setMovementMethod(new ScrollingMovementMethod());
                        ansT.setText(ans);
                        tr.addView(v);
                    }


                    for (int i=0;i<academicAnswers.size();i++)
                    {
                        CollegeRegisterQuestions a = academicAnswers.get(i);
                        if(allheadings.contains(academicQ.get(i))==false)
                            continue;
                        View v=getLayoutInflater().inflate(R.layout.repeatable_table_content,null);
                        String ans=a.getAnswer();
                        TextView ansT=v.findViewById(R.id.table_content);
                        ansT.setMovementMethod(new ScrollingMovementMethod());
                        ansT.setText(ans);
                        tr.addView(v);
                    }
                    for (int i=0;i<uploadQ.size();i++)
                    {
                        if(allheadings.contains(uploadQ.get(i))==false)
                                continue;
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
            }
        },10,1);
    }
    public static void getHeading()
    {
        DocumentReference allcourse = FirebaseFirestore.getInstance().collection("All Colleges")
                .document(adminData.getCollegeId());
        allcourse.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> course = (ArrayList<String>) documentSnapshot.get("Courses");
                for (String c:course) {
                    allcourse.collection("Branches").document(c)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String>branch= (ArrayList<String>) documentSnapshot.get("Branches");
                            ArrayList<Boolean>branchstatus= new ArrayList<>();
                            for(String c:branch)
                                branchstatus.add(true);
                            if(fromWhere==false)
                                allCourseAndBranchShow.put(c,branchstatus);
                            allCourseAndBranch.put(c,branch);
                        }
                    });
                }
            }
        });
    }
    public void filters(View v) {
        if (progressBar.getVisibility() == View.VISIBLE) {
            Toast.makeText(AdminViewAllStudentData.this, "Data is Loading. Please Wait", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(AdminViewAllStudentData.this, AdminViewAllStudentDataFilters.class);
       // intent.putExtra("Object", this);
        startActivity(intent);
    }
    public void downloadExcel(View v)
    {
        if(progressBar.getVisibility()==View.VISIBLE)
        {
            Toast.makeText(AdminViewAllStudentData.this,"Data is Loading.Please Wait",Toast.LENGTH_SHORT).show();
            return;
        }
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
                        putData(hssfSheet,progressDialog);
                        FileOutputStream fileOutputStream= new FileOutputStream(filePath);
                        hssfWorkbook.write(fileOutputStream);

                        if (fileOutputStream!=null){
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                       Snackbar.make(v,"File stored at"+filePath.getPath(),Snackbar.LENGTH_LONG).show();
                        //Toast.makeText(AdminViewAllStudentData.this,"File stored at"+filePath.getPath(),Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(AdminViewAllStudentData.this,"File Already Exists",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
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
            if(allheadings.contains(personalQ.get(i))==false)
                continue;
            HSSFCell thisCell=heading.createCell(c++);
            thisCell.setCellValue(personalQ.get(i).getQuestion());
        }
        for(int i=0;i<academicQ.size();i++)
        {
            if(allheadings.contains(academicQ.get(i))==false)
                continue;
            HSSFCell thisCell=heading.createCell(c++);
            thisCell.setCellValue(academicQ.get(i).getQuestion());
        }
        int count=0;
        for(int i=0;i<allStudentData.size();i++)
        {
            StudentData thisStudent = allStudentData.get(i);
            if(allCourseAndBranchShow.containsKey(thisStudent.getCourse()))
            {
                int i1=allCourseAndBranch.get(thisStudent.getCourse()).indexOf(thisStudent.getBranch());
                if(allCourseAndBranchShow.get(thisStudent.getCourse()).get(i1)==false)
                    continue;

            }
            progressDialog.setProgress((i+2)/rows);
            HSSFRow thisRow=thisSheet.createRow(count+1);
            count++;
            int c1=0;
            ArrayList<CollegeRegisterQuestions> personalAnswers = thisStudent.getPersonal_ques();
            ArrayList<CollegeRegisterQuestions> academicAnswers=thisStudent.getAcademic_ques();
            for (int i1=0;i1<personalAnswers.size();i1++)
            {
                CollegeRegisterQuestions p = personalAnswers.get(i1);;

                if(allheadings.contains(personalQ.get(i1))==false)
                    continue;
                String ans=p.getAnswer();
                HSSFCell thisCell=thisRow.createCell(c1++);
                thisCell.setCellValue(ans);
            }
            for (int i1=0;i1<academicAnswers.size();i1++)
            {
                CollegeRegisterQuestions p = academicAnswers.get(i1);;
                if(allheadings.contains(academicQ.get(i1))==false)
                    continue;
                String ans=p.getAnswer();
                HSSFCell thisCell=thisRow.createCell(c1++);
                thisCell.setCellValue(ans);
            }
        }
    }
    public static void appliedfilter()
    {
        allStudentDatatemp=new ArrayList<>(allStudentData);
        tl.removeAllViews();
        flag=false;
        flag2=true;
        srno=1;
    }
    public static void get(AdminData adminData)
    {
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
                    thisStudent.setBranch((String) d.get("Branch"));
                    thisStudent.setCourse((String) d.get("Course"));
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
                                q.setType(personalQ.get(q.getId()).getType());
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
                                                q.setType(academicQ.get(q.getId()).getType());
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
                                                        allStudentDatatemp.add(thisStudent);
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

        tl.removeAllViews();
        flag=false;
        flag2=true;
        Collections.sort(allStudentData, new Comparator<StudentData>() {
            @Override
            public int compare(StudentData studentData, StudentData t1) {
                if(domain==0) {
                    ArrayList<CollegeRegisterQuestions> s1 = studentData.getPersonal_ques();
                    ArrayList<CollegeRegisterQuestions> s2 = t1.getPersonal_ques();
                    CollegeRegisterQuestions s11 = s1.get(k);
                    CollegeRegisterQuestions s12 = s2.get(k);
                    int type1 = s11.getType();
                    if (type1 == 2) {
                        double a1 = Double.parseDouble(s11.getAnswer());
                        double a2 = Double.parseDouble(s12.getAnswer());
                        if(s==0)
                            return Double.compare(a1,a2);
                        else
                            return Double.compare(a2,a1);
                    }
                    if (type1 == 3) {
                        double a1 = Double.parseDouble(s11.getAnswer());
                        double a2 = Double.parseDouble(s12.getAnswer());
                        if(s==0)
                            return Double.compare(a1,a2);
                        else
                            return Double.compare(a2,a1);
                    }
                    else {
                        if (s == 0)
                            return s11.getAnswer().toLowerCase(Locale.ROOT).compareTo(s12.getAnswer().toLowerCase(Locale.ROOT));
                        else
                            return s12.getAnswer().toLowerCase(Locale.ROOT).compareTo(s11.getAnswer().toLowerCase(Locale.ROOT));
                    }
                }
                else
                {
                    ArrayList<CollegeRegisterQuestions> s1=studentData.getAcademic_ques();
                    ArrayList<CollegeRegisterQuestions> s2=t1.getAcademic_ques();
                    CollegeRegisterQuestions s11=s1.get(k);
                    CollegeRegisterQuestions s12=s2.get(k);
                    int type1 = s11.getType();
                    if (type1 == 2) {
                        int a1 = Integer.parseInt(s11.getAnswer());
                        int a2 = Integer.parseInt(s12.getAnswer());
                        if (s == 0)
                            return a1-a2;
                        else
                            return a2-a1;

                    }
                    if (type1 == 3) {
                        double a1 = Double.parseDouble(s11.getAnswer());
                        double a2 = Double.parseDouble(s12.getAnswer());
                        if(s==0)
                            return Double.compare(a1,a2);
                        else
                            return Double.compare(a2,a1);

                    }
                    else {
                        if (s == 0)
                            return s11.getAnswer().toLowerCase(Locale.ROOT).compareTo(s12.getAnswer().toLowerCase(Locale.ROOT));
                        else
                            return s12.getAnswer().toLowerCase(Locale.ROOT).compareTo(s11.getAnswer().toLowerCase(Locale.ROOT));
                    }
                }

            }
        });
         allStudentDatatemp=new ArrayList<>(allStudentData);

    }
    public void show()
    {
        //heading

        if(flag)
            return;
        tl.removeAllViews();
        srno=1;
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
        View headingName1=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
        TextView srh=headingName1.findViewById(R.id.table_head);
        srh.setText( "Sr No");
        srh.setMovementMethod(new ScrollingMovementMethod());
        heading.addView(headingName1);
        View headingName2=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
        TextView email=headingName2.findViewById(R.id.table_head);
        email.setText( "Registered Email");
        email.setMovementMethod(new ScrollingMovementMethod());
        heading.addView(headingName2);
        for(int i=0;i<personalQ.size();i++)
        {
            if(allheadings.contains(personalQ.get(i))==false)
                continue;
            View headingname=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
            TextView name=headingname.findViewById(R.id.table_head);
            name.setText( personalQ.get(i).getQuestion());
            name.setMovementMethod(new ScrollingMovementMethod());
            heading.addView(headingname);
        }
        for(int i=0;i<academicQ.size();i++)
        {
            if(allheadings.contains(academicQ.get(i))==false)
                continue;
            View headingname=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
            TextView name=headingname.findViewById(R.id.table_head);
            name.setText(academicQ.get(i).getQuestion());
            name.setMovementMethod(new ScrollingMovementMethod());
            heading.addView(headingname);
        }
        for(int i=0;i<uploadQ.size();i++)
        {
            if(allheadings.contains(uploadQ.get(i))==false)
                continue;
            View headingname=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
            TextView name=headingname.findViewById(R.id.table_head);
            name.setText(uploadQ.get(i).getQuestion());
            name.setMovementMethod(new ScrollingMovementMethod());
            heading.addView(headingname);
        }
        tl.addView(heading);
        flag=true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        t.purge();
        t.cancel();
        fromWhere=false;
    }
}