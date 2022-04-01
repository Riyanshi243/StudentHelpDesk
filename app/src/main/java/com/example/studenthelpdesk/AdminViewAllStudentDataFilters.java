package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.apache.poi.sl.usermodel.Line;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminViewAllStudentDataFilters extends AppCompatActivity {
    TextView courses, personalDetails,academicDetails,uploadDetails,sort;
    LinearLayout filterFields;
    AdminData adminData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_student_data_filters);
        courses=findViewById(R.id.Courses);
        personalDetails=findViewById(R.id.Personal_Details);
        academicDetails=findViewById(R.id.Academic_Details);
        uploadDetails=findViewById(R.id.Upload_Details);
        sort=findViewById(R.id.sort);
        adminData=AdminPage.adminData;
        //adminViewAllStudentData= (AdminViewAllStudentData) getIntent().getExtras().get("Object");
        filterFields=findViewById(R.id.filterFields);
        sort.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        showSortingList();
    }
    public void clickCourse(View v)
    {
        filterFields.removeAllViews();
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        sort.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));

        HashMap<String, ArrayList<String>> allCourseAndBranch = AdminViewAllStudentData.allCourseAndBranch;
        HashMap<String, ArrayList<Boolean>> allCourseAndBranchShow = AdminViewAllStudentData.allCourseAndBranchShow;
        for(String c:allCourseAndBranch.keySet())
        {
            View thisCourse=getLayoutInflater().inflate(R.layout.repeatable_admin_viewallstudent_filter_course,null);
            CheckBox currCourseName=thisCourse.findViewById(R.id.coursename);
            currCourseName.setText(c);
            Button downKey=thisCourse.findViewById(R.id.downArrow);
            LinearLayout courseLL=thisCourse.findViewById(R.id.forBranch);
            Boolean status=true;
            ArrayList<String> branch = allCourseAndBranch.get(c);
            ArrayList<Boolean> branchShow = allCourseAndBranchShow.get(c);
            for(int i1=0;i1<branchShow.size();i1++)
            {
                status=status&branchShow.get(i1);
            }
            currCourseName.setChecked(status);
             downKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(courseLL.getChildCount()!=0) {
                        courseLL.removeAllViews();
                        return;
                    }

                    for(int i=0;i<branch.size();i++)
                    {
                        String b=branch.get(i);
                        View thisBranch=getLayoutInflater().inflate(R.layout.repeatable_admin_viewallstudent_filter_branch,null);
                        CheckBox currBranchName=thisBranch.findViewById(R.id.branchname);
                        currBranchName.setText(b);
                        currBranchName.setChecked(branchShow.get(i));
                        Button downKey=thisBranch.findViewById(R.id.downArrow);
                        LinearLayout branchLL=thisBranch.findViewById(R.id.branchLayout);
                        int finalI = i;
                        currBranchName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                branchShow.set(finalI,b);
                                allCourseAndBranchShow.remove(c);
                                allCourseAndBranchShow.put(c,branchShow);
                                AdminViewAllStudentData.appliedfilter();

                            }
                        });
                        downKey.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(branchLL.getChildCount()!=0)
                                {
                                    branchLL.removeAllViews();
                                    return;
                                }

                            }
                        });
                        courseLL.addView(thisBranch);
                    }
                }
            });
            currCourseName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    for(int i1=0;i1<branchShow.size();i1++)
                    {
                        branchShow.set(i1,b);
                        allCourseAndBranchShow.remove(c);
                        allCourseAndBranchShow.put(c,branchShow);
                    }
                    for(int i=0;i<courseLL.getChildCount();i++)
                    {
                        View thisBranch=courseLL.getChildAt(i);
                        CheckBox currBranchName=thisBranch.findViewById(R.id.branchname);
                        currBranchName.setChecked(currCourseName.isChecked());
                    }
                    AdminViewAllStudentData.appliedfilter();
                }
            });
            filterFields.addView(thisCourse);
            //AdminViewAllStudentData.getHeading();
        }
    }
    public void clickPersonalDetails(View v)
    {
        filterFields.removeAllViews();
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        sort.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        ArrayList<CollegeRegisterQuestions> perQ = AdminViewAllStudentData.personalQ;
        for(int i=0;i<perQ.size();i++)
        {
            CheckBox c=new CheckBox(this);
            c.setText(perQ.get(i).getQuestion());
            if(AdminViewAllStudentData.allheadings.contains(perQ.get(i)))
                c.setChecked(true);
            int finalI = i;
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b==false)
                        AdminViewAllStudentData.allheadings.remove(perQ.get(finalI));
                    else
                        AdminViewAllStudentData.allheadings.add(perQ.get(finalI));
                    AdminViewAllStudentData.appliedfilter();
                }
            });
            filterFields.addView(c);
        }
    }
    public void clickAcademicDetails(View v)
    {
        filterFields.removeAllViews();
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        sort.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        ArrayList<CollegeRegisterQuestions> acQ = AdminViewAllStudentData.academicQ;
        for(int i=0;i<acQ.size();i++)
        {
            CheckBox c=new CheckBox(this);
            c.setText(acQ.get(i).getQuestion());
            if(AdminViewAllStudentData.allheadings.contains(acQ.get(i)))
                c.setChecked(true);
            int finalI = i;
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b==false)
                        AdminViewAllStudentData.allheadings.remove(acQ.get(finalI));
                    else
                        AdminViewAllStudentData.allheadings.add(acQ.get(finalI));
                    AdminViewAllStudentData.appliedfilter();
                }
            });
            filterFields.addView(c);
        }
    }
    public void clickUploadDetails(View v)
    {
        filterFields.removeAllViews();
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        sort.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        ArrayList<CollegeRegisterQuestions> upQ = AdminViewAllStudentData.uploadQ;
        for(int i=0;i<upQ.size();i++)
        {
            CheckBox c=new CheckBox(this);
            c.setText(upQ.get(i).getQuestion());
            if(AdminViewAllStudentData.allheadings.contains(upQ.get(i)))
                c.setChecked(true);
            int finalI = i;
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b==false)
                        AdminViewAllStudentData.allheadings.remove(upQ.get(finalI));
                    else
                        AdminViewAllStudentData.allheadings.add(upQ.get(finalI));
                    AdminViewAllStudentData.appliedfilter();
                }
            });
            filterFields.addView(c);
        }
    }
    public void clickSort(View v)
    {
        filterFields.removeAllViews();
        sort.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        showSortingList();
    }
    public void showSortingList()
    {
        filterFields.removeAllViews();
        RadioGroup radioGroup=new RadioGroup(this);
        ArrayList<CollegeRegisterQuestions> personalQ = AdminViewAllStudentData.personalQ;
        ArrayList<CollegeRegisterQuestions> acadQ = AdminViewAllStudentData.academicQ;
        for(int i=0;i<personalQ.size();i++)
        {
            RadioButton r=new RadioButton(this);
            r.setText(personalQ.get(i).getQuestion());
            radioGroup.addView(r);
            int finalI = i;
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AdminViewAllStudentData.k= finalI;
                    AdminViewAllStudentData.domain=0;
                    AdminViewAllStudentData.sort();
                    Toast.makeText(AdminViewAllStudentDataFilters.this, "Data sorting", Toast.LENGTH_SHORT).show();
                }
            });
        }
        for(int i=0;i<acadQ.size();i++)
        {
            RadioButton r=new RadioButton(this);
            r.setText(acadQ.get(i).getQuestion());
            radioGroup.addView(r);
            int finalI = i;
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AdminViewAllStudentData.k= finalI;
                    AdminViewAllStudentData.domain=1;
                    AdminViewAllStudentData.sort();
                    Toast.makeText(AdminViewAllStudentDataFilters.this, "Data sorting", Toast.LENGTH_SHORT).show();
                }
            });
        }
        filterFields.addView(radioGroup);

    }
}