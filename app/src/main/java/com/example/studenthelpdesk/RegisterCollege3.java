package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

//In this activity we add couses and branches in a particular couses availaible in the college
public class RegisterCollege3 extends AppCompatActivity {
    final String courseQ="Enter Course Name",ansHint="Course name here",headingQ="Enter branches of the course",subQHint="Branch Name Here";
    LinearLayout ll;
    int numberOfCourse;
    ArrayList<String> courseName;
    ArrayList<ArrayList<String>> branchOfEachCourse,depOfEachBranch;
    CollegeRegistrationData allData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_college3);
        ll=findViewById(R.id.linearL);
        allData=RegisterCollege.allData;
        courseName=new ArrayList<>();
        numberOfCourse=0;
        branchOfEachCourse =new ArrayList<>(1);
        depOfEachBranch =new ArrayList<>(1);
        getWorkingView();
        numberOfCourse=1;
    }
    public void addBranch(View v)
    {
        getWorkingView();
        numberOfCourse++;
    }
    public void saveAndNext(View v)
    {
        AlertDialog.Builder savePrompt=new AlertDialog.Builder(this);
        savePrompt.setTitle("ARE YOU SURE?");
        savePrompt.setMessage("ALL THE UNSAVED COURSES WILL NOT BE SAVED");
        savePrompt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                allData.setCourseName(courseName);
                allData.setBranchForEachCourse(branchOfEachCourse);
                allData.setDepForEachCourse(depOfEachBranch);
                //intent to registration step 4
                startActivity(new Intent(RegisterCollege3.this,RegisterCollege4_PersonalQuestions.class));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //close
            }
        });
        savePrompt.create().show();
    }
    void getWorkingView()
    {
        View questionRepeatable = getLayoutInflater().inflate(R.layout.repeatable_course_register_layout, null);
        TextView question=questionRepeatable.findViewById(R.id.Heading2);
        EditText currentCourseName=questionRepeatable.findViewById(R.id.EditText);
        TextView heading=questionRepeatable.findViewById(R.id.Heading);
        LinearLayout subQuestions=questionRepeatable.findViewById(R.id.AddSubQuestions);
        Button addQuestion=questionRepeatable.findViewById(R.id.addsubQ);
        Button done=questionRepeatable.findViewById(R.id.complete);
        currentCourseName.setHint(ansHint);
        currentCourseName.requestFocus();
        question.setText(courseQ);
        heading.setText(headingQ);
        View branchDeptRepeatable = getLayoutInflater().inflate(R.layout.repeatable_branch_department_college_register, null);
        EditText subq=branchDeptRepeatable.findViewById(R.id.branch);
        AutoCompleteTextView dept=branchDeptRepeatable.findViewById(R.id.department_name);
        ArrayList<String> listDept = allData.getDeptName();
        ArrayAdapter spinnerList = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listDept);
        dept.setAdapter(spinnerList);
        dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //add the dept selected
            }
        });
        subq.setHint(subQHint);
        subQuestions.addView(branchDeptRepeatable);
        done.setText("Save");
        //add more branches
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int branchNumber=subQuestions.getChildCount();
                View lastBranchAndDept= (View) subQuestions.getChildAt(branchNumber-1);
                EditText lastBranch=lastBranchAndDept.findViewById(R.id.branch);
                if(lastBranch.getText().toString().length()!=0) {
                    View branchDeptRepeatable = getLayoutInflater().inflate(R.layout.repeatable_branch_department_college_register, null);
                    EditText subq=branchDeptRepeatable.findViewById(R.id.branch);
                    AutoCompleteTextView dept=branchDeptRepeatable.findViewById(R.id.department_name);
                    ArrayList<String> listDept = allData.getDeptName();
                    ArrayAdapter spinnerList = new ArrayAdapter(RegisterCollege3.this, android.R.layout.simple_spinner_item, listDept);
                    dept.setAdapter(spinnerList);
                    dept.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            //add the dept selected
                        }
                    });
                    subq.setHint(subQHint);
                    subq.requestFocus();
                    subQuestions.addView(branchDeptRepeatable);
                }
                else
                {
                    lastBranch.setError("ENTER HERE");
                }
            }
        });
        //save current course details
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save information
                if(currentCourseName.getText().toString().length()==0)
                {
                    currentCourseName.setError("ENTER COURSE NAME");
                    return;
                }
                ArrayList<String> allbranch=new ArrayList<>();
                ArrayList<String> alldept=new ArrayList<>();
                int branchNumber=subQuestions.getChildCount();
                for(int i=0;i<branchNumber;i++)
                {
                    View branchDeptRepeatable =subQuestions.getChildAt(i);
                    EditText lastBranch=branchDeptRepeatable.findViewById(R.id.branch);
                    AutoCompleteTextView dept=branchDeptRepeatable.findViewById(R.id.department_name);
                    String branchName=lastBranch.getText().toString();
                    if(branchName.length()!=0) {
                        allbranch.add(branchName);
                        alldept.add(dept.getText().toString());
                        //Log.e("Get name",branchName+" : "+ dept.getText().toString());
                    }
                }
                branchOfEachCourse.add(allbranch);
                depOfEachBranch.add(alldept);
                courseName.add(currentCourseName.getText().toString());
                ll.removeView(questionRepeatable);
                View permanentDataView = getLayoutInflater().inflate(R.layout.repeatable_text_view_layout, null);
                TextView heading=permanentDataView.findViewById(R.id.Heading2);
                heading.setText(currentCourseName.getText().toString());
                LinearLayout subHeading=permanentDataView.findViewById(R.id.subHeadings);
                for(int i=0;i<branchNumber;i++)
                {
                    TextView thisBranch = new TextView(permanentDataView.getContext());
                    if(i<allbranch.size())
                    {
                        thisBranch.setText(allbranch.get(i)+" : "+alldept.get(i));
                        subHeading.addView(thisBranch);
                    }
                }
                ll.addView(permanentDataView);
            }
        });
        ll.addView(questionRepeatable);

    }
}