package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class StudentPersonalDetails extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener  {
    StudentData studentData;
    LinearLayout ll;
    boolean lock=true;
    Timer t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_personal_details);
        ll=findViewById(R.id.linearlay);
        studentData=StudentPage.studentData;
        String s="";
        if(studentData==null || studentData.getCourse()==null || studentData.getBranch()==null)
        {
            return;
        }
        FirebaseFirestore.getInstance().collection("All Colleges")
                .document(studentData.getCollegeid()).collection("Lock")
                .document(studentData.getCourse())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(studentData==null)
                    return;
                //Log.e("Branch", studentData.getBranch()+"");
                lock= (boolean) documentSnapshot.get(studentData.getBranch());
            }});
        t=new Timer();
        FirebaseFirestore.getInstance().collection("All Colleges")
                .document(studentData.getCollegeid()).collection("Lock")
                .document(studentData.getCourse())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                lock= (boolean) documentSnapshot.get(studentData.getBranch());
            }
        });
        t=new Timer();

        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if(studentData==null || studentData.getCourse()==null || studentData.getBranch()==null)
                {
                    return;
                }
                FirebaseFirestore.getInstance().collection("All Colleges")
                        .document(studentData.getCollegeid()).collection("Lock")
                        .document(studentData.getCourse())
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(studentData.getBranch()==null)
                            return;
                        lock= (boolean) documentSnapshot.get(studentData.getBranch());
                    }
                });
            }
        },1,1000);
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
                    if(lock==true)
                    {
                        Snackbar .make(view, "Database is Locked!!", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (a.isChangeable() == true) {
                            View typeInput=getType(a.getType(),a.getAnswer());
                            AlertDialog builder = new AlertDialog.Builder(StudentPersonalDetails.this)
                                    .setTitle(a.getQuestion())
                                    .setMessage("Enter new value")
                                    .setView(typeInput)
                                    .setCancelable(false)
                                    .setPositiveButton("SAVE",null)
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //do nothing
                                        }

                                    }).show();
                            Button posButton=builder.getButton(AlertDialog.BUTTON_POSITIVE);
                            posButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    {

                                        String answer=saveData(typeInput,a);
                                        if(answer.length()==0 && a.isCumplolsory())
                                        {
                                            //Toast.makeText(StudentPersonalDetails.this, "ERROORR", Toast.LENGTH_SHORT).show();
                                            EditText answer1= (EditText) setError(a.getType(),typeInput);
                                            if(answer1!=null) {
                                                answer1.setError("This is Compulsory");
                                                return;
                                            }

                                        }
                                        DocumentReference ansDoc= FirebaseFirestore.getInstance().collection("All Colleges").document(studentData.getCollegeid()).collection("UsersInfo").document(studentData.getEmail()).collection("Personal Question").document(a.getId()+"");
                                        HashMap<String,Object> updatedAns=new HashMap<>();
                                        updatedAns.put("Question",a.getQuestion());
                                        updatedAns.put("Answer",answer);
                                        //save in database
                                        ansDoc.set(updatedAns).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                DocumentReference mainDoc = FirebaseFirestore.getInstance().collection("All Colleges").document(studentData.getCollegeid()).collection("UsersInfo").document(studentData.getEmail());
                                                HashMap<String,Object> mainD=new HashMap<>();
                                                if(a.getQuestion().equalsIgnoreCase("Name"))
                                                {
                                                    mainD.put("Name",answer);
                                                    mainDoc.update(mainD).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(StudentPersonalDetails.this,"Change Successfull",Toast.LENGTH_LONG).show();
                                                            studentData.setName(answer);
                                                            ans.setText(answer);
                                                            a.setAnswer(answer);
                                                            builder.dismiss();
                                                        }
                                                    });
                                                }
                                                else if(a.getQuestion().equalsIgnoreCase("Course"))
                                                {
                                                    mainD.put("Course",answer);
                                                    mainDoc.update(mainD).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(StudentPersonalDetails.this,"Change Successfull",Toast.LENGTH_LONG).show();
                                                            studentData.setCourse(answer);
                                                            ans.setText(answer);
                                                            a.setAnswer(answer);
                                                            builder.dismiss();
                                                        }
                                                    });
                                                }
                                                else if(a.getQuestion().equalsIgnoreCase("Branch"))
                                                {
                                                    mainD.put("Branch",answer);
                                                    mainDoc.update(mainD).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(StudentPersonalDetails.this,"Change Successfull",Toast.LENGTH_LONG).show();
                                                            studentData.setBranch(answer);
                                                            ans.setText(answer);
                                                            a.setAnswer(answer);
                                                            builder.dismiss();
                                                        }
                                                    });
                                                }
                                                else if(a.getQuestion().equalsIgnoreCase("Year"))
                                                {
                                                    mainD.put("Year",answer);
                                                    mainDoc.update(mainD).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(StudentPersonalDetails.this,"Change Successfull",Toast.LENGTH_LONG).show();
                                                            studentData.setYr(answer);
                                                            ans.setText(answer);
                                                            a.setAnswer(answer);
                                                            builder.dismiss();
                                                        }
                                                    });
                                                }
                                                else
                                                {
                                                    Toast.makeText(StudentPersonalDetails.this,"Change Successfull",Toast.LENGTH_LONG).show();
                                                    ans.setText(answer);
                                                    a.setAnswer(answer);
                                                    builder.dismiss();
                                                }
                                            }
                                        });

                                    }
                                }
                            });



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
    public String saveData(View nView,CollegeRegisterQuestions a)
    {
        int type = a.getType();
        if(type==2)
        {
            //numeric
            EditText ans=nView.findViewById(R.id.editvalnumeric);
            return ans.getText().toString();
        }
        if(type==3)
        {
            EditText ans=nView.findViewById(R.id.editvalmulti);
            return ans.getText().toString();
        }
        if(type==0)
        {
            EditText ans=nView.findViewById(R.id.editTextTextMultiLine);
            return ans.getText().toString();
        }
        if(type==1)
        {
            EditText ans=nView.findViewById(R.id.editTextMultiLine);
            return ans.getText().toString();
        }
        if (type==4)
        {
            RadioGroup ans=nView.findViewById(R.id.rg);
            RadioButton m=ans.findViewById(R.id.male);
            RadioButton f=ans.findViewById(R.id.female);
            RadioButton o=ans.findViewById(R.id.not);
            if(m.isChecked())
                return "Male";
            else if(f.isChecked())
                return "Female";
            else
                return "Other";
        }
        if(type==5)
        {
            TextView datePicked=nView.findViewById(R.id.tvDate);
            return datePicked.getText().toString();
        }
        return "";

    }
    View getType(long i,String a)
    {

        if(i==2)
        {
            //numeric
            //View nView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.repeatable_numeric_text_layout, null, false);
            View nView=getLayoutInflater().inflate(R.layout.repeatable_numeric_text_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            EditText ans=nView.findViewById(R.id.editvalnumeric);
            ans.setInputType(InputType.TYPE_CLASS_NUMBER);
            ans.setText(a);
            ques.setVisibility(View.GONE);
            return nView;
        }
        if(i==3)
        {
            //numeric decimal
            View nView=getLayoutInflater().inflate(R.layout.repeatable_number_decimal_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            EditText ans=nView.findViewById(R.id.editvalmulti);
            ans.setText(a);
            ques.setVisibility(View.GONE);

            return nView;
        }
        if(i==0)
        {
            //single line string
            View nView=getLayoutInflater().inflate(R.layout.repeatable_edit_text_white_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            EditText ans=nView.findViewById(R.id.editTextTextMultiLine);
            ans.setText(a);
            ques.setVisibility(View.GONE);
            return nView;
        }
        if(i==1)
        {
            //multiline string
            View nView=getLayoutInflater().inflate(R.layout.repeatable_multiline_text_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            EditText ans=nView.findViewById(R.id.editTextMultiLine);
            ans.setText(a);
            ques.setVisibility(View.GONE);
            return nView;
        }
        if(i==4)
        {
            //gender
            View nView=getLayoutInflater().inflate(R.layout.repeatable_radio_button_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setVisibility(View.GONE);
            RadioGroup ans=nView.findViewById(R.id.rg);
            ans.setClickable(true);
            RadioButton m=ans.findViewById(R.id.male);

            RadioButton f=ans.findViewById(R.id.female);

            RadioButton o=ans.findViewById(R.id.not);
            if(a.equalsIgnoreCase("Male"))
                m.setChecked(true);
            else if(a.equalsIgnoreCase("Female"))
                f.setChecked(true);
            else
                o.setChecked(true);
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
            return nView;
        }
        if(i==5)
        {
            //date
            View nView=getLayoutInflater().inflate(R.layout.repeatable_date_input_layout,null);
            TextView ques=nView.findViewById(R.id.dobtext);
            ques.setVisibility(View.GONE);
            Button datePicker =nView.findViewById(R.id.btPickDate);
            TextView datePicked=nView.findViewById(R.id.tvDate);
            datePicked.setText(a);
            selectedDate=a;
            currentView=nView;
            datePicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePicker mDatePickerDialogFragment;
                    mDatePickerDialogFragment = new DatePicker();
                    mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
                    mDatePickerDialogFragment.setCancelable(false);
                }
            });
            return nView;
        }

        return null;
    }

    String selectedDate;
    View currentView;
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        TextView datePicker=currentView.findViewById(R.id.tvDate);
        selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        datePicker.setText(selectedDate);
    }
    public View setError(int type, View nView)
    {

            if(type==2)
            {
                //numeric
                EditText ans=nView.findViewById(R.id.editvalnumeric);
                return ans;
            }
            if(type==3)
            {
                EditText ans=nView.findViewById(R.id.editvalmulti);
                return ans;
            }
            if(type==0)
            {
                EditText ans=nView.findViewById(R.id.editTextTextMultiLine);
                return ans;
            }
            if(type==1)
            {
                EditText ans=nView.findViewById(R.id.editTextMultiLine);
                return ans;
            }
            if (type==4)
            {
                return null;
            }
            if(type==5)
            {
                return null;
            }
            return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        t.cancel();
        t.purge();
    }
}