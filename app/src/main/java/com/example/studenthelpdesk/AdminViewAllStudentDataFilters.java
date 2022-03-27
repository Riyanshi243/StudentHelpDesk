package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class AdminViewAllStudentDataFilters extends AppCompatActivity {
    TextView courses, personalDetails,academicDetails,uploadDetails,sort;
    LinearLayout filterFields;
    AdminViewAllStudentData adminViewAllStudentData;
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
        ArrayList<String> selectedCourse=new ArrayList<>();
        DocumentReference allcourse = FirebaseFirestore.getInstance().collection("All Colleges")
                .document(adminData.getCollegeId());
                allcourse.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String>course= (ArrayList<String>) documentSnapshot.get("Courses");
                for(String c:course)
                {
                    View thisCourse=getLayoutInflater().inflate(R.layout.repeatable_admin_viewallstudent_filter_course,null);
                    CheckBox currCourseName=thisCourse.findViewById(R.id.coursename);
                    currCourseName.setText(c);
                    currCourseName.setChecked(true);
                    Button downKey=thisCourse.findViewById(R.id.downArrow);
                    downKey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            allcourse.collection("Branches").document(c)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    ArrayList<String>branch= (ArrayList<String>) documentSnapshot.get("Branches");
                                    for(String b:branch)
                                    {
                                        View thisBranch=getLayoutInflater().inflate(R.layout.repeatable_admin_viewallstudent_filter_branch,null);
                                        CheckBox currBranchName=thisBranch.findViewById(R.id.branchname);
                                        currBranchName.setText(b);
                                        currBranchName.setChecked(true);
                                        Button downKey=thisBranch.findViewById(R.id.downArrow);
                                        downKey.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });

                }
            }
        });
    }
    public void clickPersonalDetails(View v)
    {
        filterFields.removeAllViews();
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        sort.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
    }
    public void clickAcademicDetails(View v)
    {
        filterFields.removeAllViews();
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        sort.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
    }
    public void clickUploadDetails(View v)
    {
        filterFields.removeAllViews();
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        sort.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
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
                    Toast.makeText(AdminViewAllStudentDataFilters.this, "Apply sorting by selecting appropriate sorting option from menu", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AdminViewAllStudentDataFilters.this, "Apply sorting by selecting appropriate sorting option from menu", Toast.LENGTH_SHORT).show();

                }
            });
        }
        filterFields.addView(radioGroup);

    }
}