package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class StudentPersonalDetails extends AppCompatActivity {
    StudentData studentData;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_personal_details);
        ll=findViewById(R.id.ll);
        studentData=StudentPage.studentData;
        String s="";
        ArrayList<CollegeRegisterQuestions> quesAns = studentData.getPersonal_ques();
        for(CollegeRegisterQuestions a:quesAns)
        {
            View repeatAnswers=getLayoutInflater().inflate(R.layout.repeatable_student_details,null);
            TextView ques=repeatAnswers.findViewById(R.id.Ques);
            TextView ans=repeatAnswers.findViewById(R.id.ans);
            ques.setText(a.getQuestion());
            ans.setText(a.getAnswer());
            ans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (a.isChangeable() == true) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(StudentPersonalDetails.this);
                            builder.setTitle(a.getQuestion());
                            builder.setMessage("Enter new value");
                            //change it with the desired view using getType
                            EditText neww=new EditText(repeatAnswers.getContext());
                            builder.setView(neww);
                            neww.setText(a.getAnswer());
                            builder.setCancelable(false)
                                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DocumentReference ansDoc= FirebaseFirestore.getInstance().collection("All Colleges").document(studentData.getCollegeid()).collection("Personal Question").document(a.getId()+"");
                                            HashMap<String,Object> updatedAns=new HashMap<>();
                                            updatedAns.put("Answer",neww.getText().toString());
                                            //save in database
                                            ansDoc.update(updatedAns).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(StudentPersonalDetails.this,"Change Successfull",Toast.LENGTH_LONG).show();
                                                    ans.setText(neww.getText().toString());
                                                    a.setAnswer(neww.getText().toString());
                                                }
                                            });

                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //do nothing
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                    } else {
                            AlertDialog.Builder ab=new AlertDialog.Builder(StudentPersonalDetails.this);
                            ab.setTitle("This field is not editable");
                            ab.setMessage("If you want to change the data in the field, please send request to admin");
                            ab.setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent=new Intent(StudentPersonalDetails.this,StudentSendRequestToChangeData.class);
                                    intent.putExtra("Details",a);
                                    startActivity(intent);
                                    finish();
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
            ll.addView(repeatAnswers);
        }
    }
    View getType(long i,String q)
    {

        if(i==2)
        {
            //numeric
            //View nView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.repeatable_numeric_text_layout, null, false);
            View nView=getLayoutInflater().inflate(R.layout.repeatable_numeric_text_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            EditText ans=nView.findViewById(R.id.editvalnumeric);
            ans.setInputType(InputType.TYPE_CLASS_NUMBER);
            ques.setText(q);
            return nView;
        }
        if(i==3)
        {
            //numeric decimal
            View nView=getLayoutInflater().inflate(R.layout.repeatable_number_decimal_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setText(q);

            return nView;
        }
        if(i==0)
        {
            //single line string
            View nView=getLayoutInflater().inflate(R.layout.repeatable_edit_text_white_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setText(q);
            return nView;
        }
        if(i==1)
        {
            //multiline string
            View nView=getLayoutInflater().inflate(R.layout.repeatable_multiline_text_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setText(q);
            return nView;
        }
        if(i==4)
        {
            //gender
            View nView=getLayoutInflater().inflate(R.layout.repeatable_radio_button_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            RadioGroup ans=nView.findViewById(R.id.rg);
            ans.setClickable(true);
            RadioButton m=ans.findViewById(R.id.male);

            RadioButton f=ans.findViewById(R.id.female);

            RadioButton o=ans.findViewById(R.id.not);
            ans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    /*if(i==m.getId())
                        gender="Male";
                    else if(i==f.getId())
                        gender="Female";
                    else
                        gender="Other";*/
                }
            });
            f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            m.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            o.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            ques.setText(q);
            return nView;
        }
        if(i==5)
        {
            //date
            View nView=getLayoutInflater().inflate(R.layout.repeatable_date_input_layout,null);
            TextView ques=nView.findViewById(R.id.dobtext);
            ques.setText(q);
            Button datePicker =nView.findViewById(R.id.btPickDate);
            TextView datePicked=nView.findViewById(R.id.tvDate);
            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePicker mDatePickerDialogFragment;
                    mDatePickerDialogFragment = new DatePicker();
                    mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
                    //datePicked.setText(selectedDate);
                }
            });
            return nView;
        }
        if(i==6)
        {
            //upload
            View nView=getLayoutInflater().inflate(R.layout.repeatable_upload_document,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setText(q);
            return nView;
        }
        if(i==7)
        {

            //dropdown
            View nView=getLayoutInflater().inflate(R.layout.repeatable_dropdown,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setText(q);
            AutoCompleteTextView drop=nView.findViewById(R.id.dropdown);
            if(q.trim().equalsIgnoreCase("Year"))
            {
                String yr[]={"1","2","3","4","5","5+"};
                ArrayAdapter<String> spinnerList=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,yr);
                drop.setText(yr[0]);
                drop.setAdapter(spinnerList);
                studentData.setYr(yr[0]);
                drop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                        studentData.setYr(yr[pos]);
                    }
                });

            }
            /*if(q.equalsIgnoreCase("Course"))
            {
                FirebaseFirestore f=FirebaseFirestore.getInstance();
                DocumentReference allCourse = f.collection("All Colleges").document(studentData.getCollegeid());
                allCourse.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> coursesList= (ArrayList<String>) documentSnapshot.get("Courses");
                        ArrayAdapter spinnerList=new ArrayAdapter(StudentSignup1_PersonalData.this,android.R.layout.simple_spinner_item,coursesList);
                        drop.setText(coursesList.get(0));
                        drop.setAdapter(spinnerList);
                        studentData.setCourse(coursesList.get(0));
                        Collections.sort(coursesList);
                        drop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                                if(coursesList.get(pos).equalsIgnoreCase(studentData.getCourse())&&studentData.getBranch().equalsIgnoreCase(" ")==false)
                                    return;
                                studentData.setCourse(coursesList.get(pos));
                                FirebaseFirestore f = FirebaseFirestore.getInstance();
                                AutoCompleteTextView drop=branchView.findViewById(R.id.dropdown);
                                DocumentReference allBranch = f.collection("All Colleges").document(studentData.getCollegeid()).collection("Branches").document(studentData.getCourse());

                                //Log.e("Value in student",studentData.getCourse());
                                allBranch.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                                        if(documentSnapshot.exists()==false) {
                                            drop.setText("Select Course First");
                                            return;
                                        }

                                        ArrayList<String> branchList = (ArrayList<String>) documentSnapshot.get("Branches");

                                        ArrayAdapter spinnerList = new ArrayAdapter(StudentSignup1_PersonalData.this, android.R.layout.simple_spinner_item, branchList);
                                        drop.setText(branchList.get(0));
                                        drop.setAdapter(spinnerList);
                                        studentData.setBranch(branchList.get(0));
                                        drop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                                                studentData.setBranch(branchList.get(pos));
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
            if(q.equalsIgnoreCase("Branch")) {
                FirebaseFirestore f = FirebaseFirestore.getInstance();
                DocumentReference allCourse = f.collection("All Colleges").document(studentData.getCollegeid()).collection("Branches").document(studentData.getCourse());
                allCourse.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()==false) {
                            drop.setText("Select Course First");
                            return;
                        }
                        ArrayList<String> branchList = (ArrayList<String>) documentSnapshot.get("Branches");
                        ArrayAdapter spinnerList = new ArrayAdapter(StudentSignup1_PersonalData.this, android.R.layout.simple_spinner_item, branchList);
                        drop.setText(branchList.get(0));
                        drop.setAdapter(spinnerList);
                        studentData.setBranch(branchList.get(0));
                        Collections.sort(branchList);
                        drop.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                                studentData.setBranch(branchList.get(pos));
                            }
                        });

                    }
                });
                branchView=nView;
            }
            return nView;*/
        }
        return null;
    }
}