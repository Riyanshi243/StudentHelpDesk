package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class StudentSignup1_PersonalData extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    StudentData studentData;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsignup1_personal_data);
        studentData=Signup.studentData;
        ll=findViewById(R.id.ll);
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference persQuestions = f.collection("All Colleges").document(studentData.getCollegeid()).collection("Questions").document("Personal Question");
        persQuestions.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()==false) {
                    return;
                }
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
                            if(addQues==null){

                                Log.e("hi",type+"");
                                return;
                            }
                            if(compulsory==true)
                                addQues.setId(finalI);
                            else
                                addQues.setId(-finalI);
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
        for(int i=0;i<quesNumber;i++)
        {
            View v=ll.getChildAt(i);
            int id=v.getId();
            if(id>0)
            {

            }
        }
        //check if all compulsary data filled and then move to next

        //intent to step 2
        startActivity(new Intent(StudentSignup1_PersonalData.this,StudentSignup2_AcademicData.class));
    }

    @SuppressLint("ResourceAsColor")
    View getType(long i, String q)
    {
        
        if(i==2)
        {
            //numeric
            View nView=getLayoutInflater().inflate(R.layout.repeatable_numeric_text_layout,null);
            TextView ques=nView.findViewById(R.id.Ques);
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
            View nView=getLayoutInflater().inflate(R.layout.repeatable_edit_text_layout,null);
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
                    datePicked.setText(selectedDate);
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
            /*View nView=getLayoutInflater().inflate(R.layout.repeatable_dropdown,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setText(q);
            return nView;*/
        }
        return null;
    }
    String selectedDate="";
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());

    }
}