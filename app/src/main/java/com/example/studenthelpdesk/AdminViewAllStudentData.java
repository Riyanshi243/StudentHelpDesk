package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AdminViewAllStudentData extends AppCompatActivity {

    ArrayList<CollegeRegisterQuestions> personalQ,academicQ,uploadQ;
    AutoCompleteTextView sortin;
    AdminData adminData;
    TableLayout tl;
    int i=0;
    int s=0;
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
                if(i==0)
                    s=1;
                else
                    s=2;
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
                                                                                get();
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
        Timer t=new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Log.e("Student",allStudentData.size()+"");
                if(allStudentData.size()>0&&0<allStudentDatatemp.size())
                {
                    StudentData thisStudent=allStudentDatatemp.get(0);
                    allStudentDatatemp.remove(thisStudent);
                    ArrayList<CollegeRegisterQuestions> personalAnswers = thisStudent.getPersonal_ques();
                    TableRow tr=new TableRow(AdminViewAllStudentData.this);
                    for (CollegeRegisterQuestions p:personalAnswers)
                    {
                        View v=getLayoutInflater().inflate(R.layout.repeatable_table_content,null);
                        String ans=p.getAnswer();
                        TextView ansT=v.findViewById(R.id.table_content);
                        ansT.setText(ans);
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
        },1000,10);
    }
    public void filters(View v){
        startActivity(new Intent(AdminViewAllStudentData.this, AdminViewAllStudentDataFilters.class));
    }
    public void get()
    {
        //sort all the questions based on id
        Query studentsAll = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").orderBy("Name", com.google.firebase.firestore.Query.Direction.DESCENDING);
        studentsAll.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                    thisStudent.setName((String) d.get("Name"));
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
                                    //once done
                                    Log.e("Final i",finalI+" "+students.size());

                                    {
                                        allStudentData.add(thisStudent);
                                        Log.e("Adding",allStudentData.toString());
                                        allStudentDatatemp=new ArrayList<>(allStudentData);

                                    }
                                }

                            }
                        }
                    });
                }
            }
        });
    }
    int k=0;
    public void sort()
    {

        Log.e("Answers before",allStudentData.toString());
        Collections.sort(allStudentData, new Comparator<StudentData>() {
            @Override
            public int compare(StudentData studentData, StudentData t1) {
                ArrayList<CollegeRegisterQuestions> s1=studentData.getPersonal_ques();
                ArrayList<CollegeRegisterQuestions> s2=t1.getPersonal_ques();
                CollegeRegisterQuestions s11=s1.get(k);
                CollegeRegisterQuestions s12=s2.get(k);
                if(s==0)
                    return s11.getAnswer().compareTo(s12.getAnswer());
                else
                    return s12.getAnswer().compareTo(s11.getAnswer());
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
            heading.addView(headingname);
        }
        for(int i=0;i<academicQ.size();i++)
        {
            View headingname=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
            TextView name=headingname.findViewById(R.id.table_head);
            name.setText(academicQ.get(i).getQuestion());
            heading.addView(headingname);
        }
        for(int i=0;i<uploadQ.size();i++)
        {
            View headingname=getLayoutInflater().inflate(R.layout.repeatable_table_header,null);
            TextView name=headingname.findViewById(R.id.table_head);
            name.setText(uploadQ.get(i).getQuestion());
            heading.addView(headingname);
        }
        tl.addView(heading);
    }

}