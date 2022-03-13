package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class StudentSendRequestToChangeData extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    StudentData studentData;
    TextView hello,currValMsg,currVal;
    EditText reason;
    LinearLayout ll;
    String hint="Enter your new value...";
    CollegeRegisterQuestions a;
    View ans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_send_request_to_change_data);
        a= (CollegeRegisterQuestions) getIntent().getSerializableExtra("Details");
        studentData=StudentPage.studentData;
        hello=findViewById(R.id.welcome);
        currVal=findViewById(R.id.currentVal);
        currValMsg=findViewById(R.id.currentVal_msg);
        reason=findViewById(R.id.reasontochange);
        ll=findViewById(R.id.linearlay);
        ans=getType(a.getType(),null,a.getQuestion());
        ll.addView(ans);
        hello.setText("Hello "+studentData.getName());
        currValMsg.setText("Current value of "+ a.getQuestion());
        currVal.setText(a.getAnswer());

    }
    View getType(long i,String a,String q)
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
            ans.setHint(hint);
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
            ans.setHint(hint);
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
            ans.setHint(hint);
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
            ans.setHint(hint);
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
            if(a==null)
                m.setChecked(true);
            else if(a.equalsIgnoreCase("Male"))
                m.setChecked(true);
            else if(a.equalsIgnoreCase("Female"))
                f.setChecked(true);
            else
                o.setChecked(true);
            ans.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {

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
            ans=nView;
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
        if(i==7)
        {
            View nView=getLayoutInflater().inflate(R.layout.repeatable_dropdown,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setVisibility(View.GONE);
            AutoCompleteTextView drop=nView.findViewById(R.id.dropdown);
            drop.setText(a);
            if(q.trim().equalsIgnoreCase("Year"))
            {
                String yr[]={"1","2","3","4","5","5+"};
                ArrayAdapter<String> spinnerList=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,yr);
                drop.setText(yr[0]);
                drop.setAdapter(spinnerList);
            }
            if(q.equalsIgnoreCase("Course"))
            {
                FirebaseFirestore f=FirebaseFirestore.getInstance();
                DocumentReference allCourse = f.collection("All Colleges").document(studentData.getCollegeid());
                allCourse.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ArrayList<String> coursesList= (ArrayList<String>) documentSnapshot.get("Courses");
                        ArrayAdapter spinnerList=new ArrayAdapter(StudentSendRequestToChangeData.this,android.R.layout.simple_spinner_item,coursesList);
                        drop.setAdapter(spinnerList);

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
                        ArrayAdapter spinnerList = new ArrayAdapter(StudentSendRequestToChangeData.this, android.R.layout.simple_spinner_item, branchList);
                        drop.setAdapter(spinnerList);
                    }
                });

            }
            return nView;

        }
        return null;
    }
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        TextView datePicker=ans.findViewById(R.id.tvDate);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        datePicker.setText(selectedDate);
    }
    public String saveData(View nView,CollegeRegisterQuestions a)
    {
        int type = a.getType();
        if(type==2)
        {
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
        if(type==7)
        {
            AutoCompleteTextView drop=nView.findViewById(R.id.dropdown);
            return drop.getText().toString();
        }
        return "";

    }
    public void sendRequest(View view)
    {
        if(notEmpty() && compulsory(ans,a))
        {
            String answer=saveData(ans,a);
            HashMap<String,Object> requestData=new HashMap<>();
            requestData.put("Question",a.getQuestion());
            requestData.put("Reason",reason.getText().toString());
            requestData.put("Answer Now",a.getAnswer());
            requestData.put("Change To",answer);
            requestData.put("Sender",studentData.getEmail());
            Date t=Calendar.getInstance().getTime();
            String dateToday=t.toString();
            requestData.put("Sent Time",dateToday);
            requestData.put("Status",-1);
            requestData.put("UID",FirebaseAuth.getInstance().getUid());
            requestData.put("Question Id",a.getId());
            requestData.put("Question Domain",a.getDomain());
            DocumentReference reqData = FirebaseFirestore.getInstance().collection("All Colleges").document(studentData.getCollegeid()).collection("Requests").document(FirebaseAuth.getInstance().getUid() + "_" + dateToday);
            reqData.set(requestData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    HashMap<String,Object> reqUpdate=new HashMap<>();
                    reqUpdate.put("Number of Requests",studentData.getNoOfReq()+1);
                    FirebaseFirestore.getInstance().collection("All Colleges")
                            .document(studentData.getCollegeid()).collection("UsersInfo")
                            .document(studentData.getEmail()).update(reqUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            studentData.setNoOfReq(studentData.getNoOfReq()+1);
                            Toast.makeText(StudentSendRequestToChangeData.this,"Request Sent",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                }
            });
        }
    }
    public boolean notEmpty()
    {
        //check if reason and answer written

        if(reason.getText().toString().length()==0)
        {
            reason.setError("ENTER REASON");
            return false;
        }
        return true;
    }
    public boolean compulsory(View nView,CollegeRegisterQuestions a) {
        TextView ques = nView.findViewById(R.id.Ques);
        int type = a.getType();
        if (type == 0) {

            EditText ans = nView.findViewById(R.id.editTextTextMultiLine);
            if (ans.getText().toString().length() == 0) {
                ans.setError("This field is compulsory");
                return false;
            }
        }
        if (type == 1) {
            EditText ans = nView.findViewById(R.id.editTextMultiLine);
            if (ans.getText().toString().length() == 0) {
                ans.setError("This field is compulsory");
                return false;
            }
        }
        if (type == 2) {
            EditText ans = nView.findViewById(R.id.editvalnumeric);
            if (ans.getText().toString().length() == 0) {
                ans.setError("This field is compulsory");
                return false;
            }
        }
        if (type == 3) {
            EditText ans = nView.findViewById(R.id.editvalmulti);
            if (ans.getText().toString().length() == 0) {
                ans.setError("This field is compulsory");
                return false;
            }
        }
        if (type == 4) {
            return true;
        }
        if (type == 5) {
            TextView datePicked = nView.findViewById(R.id.tvDate);
            if (datePicked.getText().toString().length() == 0) {
                datePicked.setError("This field is compulsory");
                return false;
            }
        }
        if (type == 6)
        {
            AutoCompleteTextView drop=nView.findViewById(R.id.dropdown);
            if(drop.getText().toString().length()==0)
            {
                drop.setError("This field is compulsory");
                return false;
            }
        }
        return true;
    }
}
