package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminViewAllStudentDataFilters extends AppCompatActivity {
    TextView courses, personalDetails,academicDetails,uploadDetails,sort;
    LinearLayout filterFields;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_student_data_filters);
        courses=findViewById(R.id.Courses);
        personalDetails=findViewById(R.id.Personal_Details);
        academicDetails=findViewById(R.id.Academic_Details);
        uploadDetails=findViewById(R.id.Upload_Details);
        sort=findViewById(R.id.sort);
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