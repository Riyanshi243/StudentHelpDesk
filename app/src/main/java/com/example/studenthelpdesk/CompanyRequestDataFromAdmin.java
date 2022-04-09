package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class CompanyRequestDataFromAdmin extends AppCompatActivity {
    CompanyData companyData;
    TextView courses, personalDetails,academicDetails,uploadDetails,filter;
    LinearLayout filterFields;
     ArrayList<CollegeRegisterQuestions> personalQ,academicQ,uploadQ;
     HashMap<String,ArrayList<Boolean>> allCourseAndBranchRequest=new HashMap<>();
     HashMap<String,ArrayList<String>> allCourseAndBranch=new HashMap<>();
     ArrayList<Integer> perQ,acaQ,upQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_request_data_from_admin);
        companyData=CompanyPage.companyData;
        courses=findViewById(R.id.Courses);
        personalDetails=findViewById(R.id.Personal_Details);
        academicDetails=findViewById(R.id.Academic_Details);
        uploadDetails=findViewById(R.id.Upload_Details);
        filter=findViewById(R.id.Filters);
        filterFields=findViewById(R.id.filterFields);
        personalQ=new ArrayList<>();
        academicQ=new ArrayList<>();
        uploadQ=new ArrayList<>();
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        getCoursesInCollege();
        getQuestionsOfAllType();
        clickCourse(new View(this));

    }
    public void clickCourse(View v)
    {
        filterFields.removeAllViews();
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        filter.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));

        for(String c:allCourseAndBranch.keySet())
        {
            View thisCourse=getLayoutInflater().inflate(R.layout.repeatable_admin_viewallstudent_filter_course,null);
            CheckBox currCourseName=thisCourse.findViewById(R.id.coursename);
            currCourseName.setText(c);
            Button downKey=thisCourse.findViewById(R.id.downArrow);
            LinearLayout courseLL=thisCourse.findViewById(R.id.forBranch);
            Boolean status=true;
            ArrayList<String> branch = allCourseAndBranch.get(c);
            ArrayList<Boolean> branchShow = allCourseAndBranchRequest.get(c);
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
                        //Button downKey=thisBranch.findViewById(R.id.downArrow);
                        //LinearLayout branchLL=thisBranch.findViewById(R.id.branchLayout);
                        int finalI = i;
                        currBranchName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                branchShow.set(finalI,b);
                                allCourseAndBranchRequest.remove(c);
                                allCourseAndBranchRequest.put(c,branchShow);
                                AdminViewAllStudentData.appliedfilter();

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
                        allCourseAndBranchRequest.remove(c);
                        allCourseAndBranchRequest.put(c,branchShow);
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
        }
    }
    public void clickPersonalDetails(View v)
    {
        filterFields.removeAllViews();
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        filter.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        perQ=new ArrayList<>();
        for(int i=0;i<personalQ.size();i++)
        {
            CheckBox c=new CheckBox(this);
            c.setText(personalQ.get(i).getQuestion());
            c.setChecked(false);
            int finalI = i;
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b==false)
                        perQ.remove((Integer)(finalI));
                    else
                        perQ.add((finalI));
                }
            });
            filterFields.addView(c);
        }
    }
    public void clickAcademicDetails(View v)
    {
        filterFields.removeAllViews();
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        filter.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        acaQ=new ArrayList<>();
        for(int i=0;i<academicQ.size();i++)
        {
            CheckBox c=new CheckBox(this);
            c.setText(academicQ.get(i).getQuestion());
            c.setChecked(false);
            int finalI = i;
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b==false)
                        acaQ.remove((Integer) (finalI));
                    else
                        acaQ.add((finalI));
                }
            });
            filterFields.addView(c);
        }
    }
    public void clickUploadDetails(View v)
    {
        filterFields.removeAllViews();
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        filter.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        upQ = new ArrayList<>();
        for(int i=0;i<uploadQ.size();i++)
        {
            CheckBox c=new CheckBox(this);
            c.setText(uploadQ.get(i).getQuestion());
            c.setChecked(false);
            int finalI = i;
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b==false)
                        upQ.remove((Integer) (finalI));
                    else
                        upQ.add((finalI));
                }
            });
            filterFields.addView(c);
        }
    }
   /* public void clickFilter(View v)
    {
        filterFields.removeAllViews();
        filter.setBackgroundColor(ContextCompat.getColor(this, R.color.hint_text));
        courses.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        personalDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        academicDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        uploadDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_blue_vlight));
        for(int i=0;i<personalQ.size();i++)
        {
            if(personalQ.get(i).getType()==1||personalQ.get(i).getType()==0||personalQ.get(i).getType()==4)
            {
                View stringFilter=getLayoutInflater().inflate(R.layout.repeatable_filter_equal,null);
                TextView questionName=stringFilter.findViewById(R.id.Heading2);
                EditText filterName=stringFilter.findViewById(R.id.FilterValue);
                Button set=stringFilter.findViewById(R.id.set);
                Button remove=stringFilter.findViewById(R.id.remove);
                questionName.setText(personalQ.get(i).getQuestion());

                int finalI = i;
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(filterName.getText().toString()==null||filterName.getText().toString().length()==0)
                        {
                            filterName.setError("This is compulsory");
                            return;
                        }
                        if(equal.containsKey(0))
                        {
                            HashMap<Integer, String> quesMap = equal.get(0);
                            quesMap.put(finalI,filterName.getText().toString());
                            equal.put(0,quesMap);

                        }
                        else
                        {
                            HashMap<Integer,String>quesMap=new HashMap<>();
                            quesMap.put(finalI,filterName.getText().toString());
                            equal.put(0,quesMap);
                            AdminViewAllStudentData.sort();
                            Toast.makeText(AdminViewAllStudentDataFilters.this,"Filter Applied",Toast.LENGTH_LONG);

                        }

                    }

                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(equal.containsKey(0)==false)
                            return;
                        HashMap<Integer, String> quesMap = equal.get(0);
                        quesMap.remove(finalI);
                        equal.put(0,quesMap);
                        filterName.setText(null);
                        AdminViewAllStudentData.sort();

                    }
                });
                filterFields.addView(stringFilter);
            }
            else if(personalQ.get(i).getType()==2||personalQ.get(i).getType()==3)
            {
                View rangeFilter=getLayoutInflater().inflate(R.layout.repeatable_filter_range,null);
                TextView questionName=rangeFilter.findViewById(R.id.Heading2);
                EditText filterNameMin=rangeFilter.findViewById(R.id.FilterValueMin);
                EditText filterNameMax=rangeFilter.findViewById(R.id.FilterValueMax);
                Button remove=rangeFilter.findViewById(R.id.remove1);
                questionName.setText(personalQ.get(i).getQuestion());
                int finalI=i;
                HashMap<Integer, HashMap<Integer, ArrayList<Double>>> range = AdminViewAllStudentData.range;
                if(range.containsKey(0))
                {
                    HashMap<Integer, ArrayList<Double>> quesMap = range.get(0);
                    if(quesMap.containsKey(finalI)) {
                        filterNameMin.setText(quesMap.get(finalI).get(0)+"");
                        filterNameMax.setText(quesMap.get(finalI).get(1)+"");
                    }
                }
                else {
                    filterNameMin.setText(null);
                    filterNameMax.setText(null);
                }
                Button set=rangeFilter.findViewById(R.id.Set);
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(filterNameMax.getText().toString()==null||filterNameMax.getText().toString().length()==0)
                        {
                            filterNameMax.setError("This is compulsory");
                            return;
                        }
                        if(filterNameMin.getText().toString()==null||filterNameMin.getText().toString().length()==0)
                        {
                            filterNameMin.setError("This is compulsory");
                            return;
                        }
                        if(range.containsKey(0))
                        {
                            HashMap<Integer, ArrayList<Double>> quesMap = range.get(0);
                            ArrayList<Double> minmax=new ArrayList<>();
                            minmax.add(Double.parseDouble(filterNameMin.getText().toString()));
                            minmax.add(Double.parseDouble(filterNameMax.getText().toString()));
                            quesMap.put(finalI,minmax);
                            range.put(0,quesMap);
                            AdminViewAllStudentData.sort();
                            Toast.makeText(AdminViewAllStudentDataFilters.this,"Filter Applied",Toast.LENGTH_LONG);

                        }
                        else
                        {
                            HashMap<Integer, ArrayList<Double>> quesMap = new HashMap<>();
                            ArrayList<Double> minmax=new ArrayList<>();
                            minmax.add(Double.parseDouble(filterNameMin.getText().toString()));
                            minmax.add(Double.parseDouble(filterNameMax.getText().toString()));
                            quesMap.put(finalI,minmax);
                            range.put(0,quesMap);
                            AdminViewAllStudentData.sort();
                            Toast.makeText(AdminViewAllStudentDataFilters.this,"Filter Applied",Toast.LENGTH_LONG);

                        }
                    }
                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(range.containsKey(0)==false)
                            return;
                        HashMap<Integer, ArrayList<Double>> quesMap = range.get(0);
                        quesMap.remove(finalI);
                        filterNameMax.setText(null);
                        filterNameMin.setText(null);
                        range.put(0,quesMap);
                    }
                });
                filterFields.addView(rangeFilter);
            }
        }
        for(int i=0;i<acadQ.size();i++)
        {
            if(acadQ.get(i).getType()==1||acadQ.get(i).getType()==4)
            {
                View stringFilter=getLayoutInflater().inflate(R.layout.repeatable_filter_equal,null);
                TextView questionName=stringFilter.findViewById(R.id.Heading2);
                EditText filterName=stringFilter.findViewById(R.id.FilterValue);
                Button set=stringFilter.findViewById(R.id.set);
                Button remove=stringFilter.findViewById(R.id.remove);
                questionName.setText(acadQ.get(i).getQuestion());
                HashMap<Integer, HashMap<Integer, String>> equal = AdminViewAllStudentData.equal;
                int finalI = i;
                if(equal.containsKey(1))
                {
                    HashMap<Integer, String> quesMap = equal.get(1);
                    if(quesMap.containsKey(finalI))
                        filterName.setText(quesMap.get(finalI));
                }
                else {
                    filterName.setText("");
                }
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(filterName.getText().toString()==null||filterName.getText().toString().length()==0)
                        {
                            filterName.setError("This is compulsory");
                            return;
                        }
                        HashMap<Integer, HashMap<Integer, String>> equal = AdminViewAllStudentData.equal;
                        if(equal.containsKey(1))
                        {
                            HashMap<Integer, String> quesMap = equal.get(1);
                            quesMap.put(finalI,filterName.getText().toString());
                            equal.put(0,quesMap);
                            AdminViewAllStudentData.sort();
                            Toast.makeText(AdminViewAllStudentDataFilters.this,"Filter Applied",Toast.LENGTH_LONG);

                        }
                        else
                        {
                            HashMap<Integer,String>quesMap=new HashMap<>();
                            quesMap.put(finalI,filterName.getText().toString());
                            equal.put(0,quesMap);
                            AdminViewAllStudentData.sort();
                            Toast.makeText(AdminViewAllStudentDataFilters.this,"Filter Applied",Toast.LENGTH_LONG);

                        }

                    }

                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(equal.containsKey(1)==false)
                            return;
                        HashMap<Integer, String> quesMap = equal.get(1);
                        quesMap.remove(finalI);
                        equal.put(1,quesMap);
                        filterName.setText(null);
                        AdminViewAllStudentData.sort();

                    }
                });
                filterFields.addView(stringFilter);
            }
            else if(acadQ.get(i).getType()==2||acadQ.get(i).getType()==3||i==2)
            {
                View rangeFilter=getLayoutInflater().inflate(R.layout.repeatable_filter_range,null);
                TextView questionName=rangeFilter.findViewById(R.id.Heading2);
                EditText filterNameMin=rangeFilter.findViewById(R.id.FilterValueMin);
                EditText filterNameMax=rangeFilter.findViewById(R.id.FilterValueMax);
                Button remove=rangeFilter.findViewById(R.id.remove1);
                questionName.setText(acadQ.get(i).getQuestion());
                int finalI=i;
                HashMap<Integer, HashMap<Integer, ArrayList<Double>>> range = AdminViewAllStudentData.range;
                if(range.containsKey(1))
                {
                    HashMap<Integer, ArrayList<Double>> quesMap = range.get(1);
                    if(quesMap.containsKey(finalI)) {
                        filterNameMin.setText(quesMap.get(finalI).get(0)+"");
                        filterNameMax.setText(quesMap.get(finalI).get(1)+"");
                    }
                }
                else {
                    filterNameMin.setText(null);
                    filterNameMax.setText(null);
                }
                Button set=rangeFilter.findViewById(R.id.Set);
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(filterNameMax.getText().toString()==null||filterNameMax.getText().toString().length()==0)
                        {
                            filterNameMax.setError("This is compulsory");
                            return;
                        }
                        if(filterNameMin.getText().toString()==null||filterNameMin.getText().toString().length()==0)
                        {
                            filterNameMin.setError("This is compulsory");
                            return;
                        }
                        if(range.containsKey(1))
                        {
                            HashMap<Integer, ArrayList<Double>> quesMap = range.get(1);
                            ArrayList<Double> minmax=new ArrayList<>();
                            minmax.add(Double.parseDouble(filterNameMin.getText().toString()));
                            minmax.add(Double.parseDouble(filterNameMax.getText().toString()));
                            quesMap.put(finalI,minmax);
                            range.put(1,quesMap);
                            AdminViewAllStudentData.sort();
                            Toast.makeText(AdminViewAllStudentDataFilters.this,"Filter Applied",Toast.LENGTH_LONG);

                        }
                        else
                        {
                            HashMap<Integer, ArrayList<Double>> quesMap = new HashMap<>();
                            ArrayList<Double> minmax=new ArrayList<>();
                            minmax.add(Double.parseDouble(filterNameMin.getText().toString()));
                            minmax.add(Double.parseDouble(filterNameMax.getText().toString()));
                            quesMap.put(finalI,minmax);
                            range.put(1,quesMap);
                            AdminViewAllStudentData.sort();
                            Toast.makeText(AdminViewAllStudentDataFilters.this,"Filter Applied",Toast.LENGTH_LONG);

                        }
                    }
                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(range.containsKey(1)==false)
                            return;
                        HashMap<Integer, ArrayList<Double>> quesMap = range.get(1);
                        quesMap.remove(finalI);
                        filterNameMax.setText(null);
                        filterNameMin.setText(null);
                        range.put(1,quesMap);
                    }
                });
                filterFields.addView(rangeFilter);
            }
        }
    }*/
    public void getCoursesInCollege()
    {
        DocumentReference allcourse = FirebaseFirestore.getInstance().collection("All Colleges")
                .document(companyData.getCollegeId());
        allcourse.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<String> course = (ArrayList<String>) documentSnapshot.get("Courses");
                for (String c:course) {
                    allcourse.collection("Branches").document(c)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            ArrayList<String>branch= (ArrayList<String>) documentSnapshot.get("Branches");
                            ArrayList<Boolean>branchstatus= new ArrayList<>();
                            for(String c:branch)
                                branchstatus.add(true);
                            allCourseAndBranch.put(c,branch);
                            allCourseAndBranchRequest.put(c,branchstatus);
                        }
                    });
                }
            }
        });
    }
    public void getQuestionsOfAllType()
    {
        DocumentReference personalDetails= FirebaseFirestore.getInstance().collection("All Colleges").document(companyData.getCollegeId()).collection("Questions").document("Personal Question");
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
                            Log.e("Type original",x+" "+crq.getQuestion());
                            crq.setType((int) x);
                            crq.setId(finalI);
                            personalQ.add(crq);
                            if(finalI1 ==total-1)
                            {
                                DocumentReference academicDetails=FirebaseFirestore.getInstance().collection("All Colleges").document(companyData.getCollegeId()).collection("Questions").document("Academic Question");
                                academicDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        long total2= (long) documentSnapshot.get("Total");
                                        for (int i=0;i<total2;i++)
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
                                                    Log.e("Type original",x+" "+crq.getQuestion());
                                                    crq.setId(finalI2);
                                                    academicQ.add(crq);
                                                    if(finalI2 ==total2-1)
                                                    {
                                                        DocumentReference uploadDetails=FirebaseFirestore.getInstance().collection("All Colleges").document(companyData.getCollegeId()).collection("Questions").document("Upload Question");
                                                        uploadDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                long total3=(long)documentSnapshot.get("Total");
                                                                for(int i3=0;i3<total3;i3++)
                                                                {
                                                                    int finalI3 = i3;
                                                                    int finalI4 = i3;
                                                                    uploadDetails.collection(i3+"").document(i3+"")
                                                                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                            CollegeRegisterQuestions crq=new CollegeRegisterQuestions();
                                                                            crq.setQuestion((String) documentSnapshot.get("Question"));
                                                                            long x= (long) documentSnapshot.get("Type");
                                                                            crq.setType((int) x);
                                                                            crq.setId(finalI3);
                                                                            uploadQ.add(crq);

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
}