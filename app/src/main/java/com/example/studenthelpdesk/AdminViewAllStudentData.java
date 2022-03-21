package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminViewAllStudentData extends AppCompatActivity {

    CollegeRegisterQuestions allpeople;
    ArrayList<CollegeRegisterQuestions> personalQ,academicQ,uploadQ;
    AutoCompleteTextView sortin;
    AdminData adminData;
    TableLayout tl;
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
                                                                        if(finalI4 ==total-1)
                                                                            show();
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
    public void filters(View v){
        startActivity(new Intent(AdminViewAllStudentData.this, AdminViewAllStudentDataFilters.class));
    }
    ArrayList<StudentData> studentData=new ArrayList<>();
    public void get()
    {

    }
    public void show()
    {
        //heading
        TableRow heading=new TableRow(this);
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

}