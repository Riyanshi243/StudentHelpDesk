package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class CompanyRequestDataFromAdmin extends AppCompatActivity {
    CompanyData companyData;
    TextView courses, personalDetails,academicDetails,uploadDetails,filter;
    LinearLayout filterFields;
     ArrayList<CollegeRegisterQuestions> personalQ,academicQ,uploadQ;
     HashMap<String,ArrayList<Boolean>> allCourseAndBranchRequest=new HashMap<>();
     HashMap<String,ArrayList<String>> allCourseAndBranch=new HashMap<>();
     ArrayList<Integer> perQ,acaQ,upQ;
    HashMap<String,HashMap<String,String>> equal=new HashMap<>();
    HashMap<String,HashMap<String,ArrayList<Double>>> range=new HashMap<>();


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

        perQ = new ArrayList<>();
        acaQ = new ArrayList<>();
        upQ = new ArrayList<>();
        getCoursesInCollege();
        getQuestionsOfAllType();
        TextView t=new TextView(this);
        t.setText("Select the required tab");
        filterFields.addView(t);
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
        for(int i=0;i<personalQ.size();i++)
        {
            CheckBox c=new CheckBox(this);
            c.setText(personalQ.get(i).getQuestion());
            if(perQ.contains(i))
                c.setChecked(true);
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
        for(int i=0;i<academicQ.size();i++)
        {
            CheckBox c=new CheckBox(this);
            c.setText(academicQ.get(i).getQuestion());
            if(acaQ.contains(i))
                c.setChecked(true);
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
        for(int i=0;i<uploadQ.size();i++)
        {
            CheckBox c=new CheckBox(this);
            c.setText(uploadQ.get(i).getQuestion());
            if(upQ.contains(i))
                c.setChecked(true);
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
   public void clickFilter(View v)
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
                if(equal.containsKey("0"))
                {
                    HashMap<String, String> quesMap = equal.get("0");
                    filterName.setText(quesMap.get(i+""));
                }
                int finalI = i;
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(filterName.getText().toString()==null||filterName.getText().toString().length()==0)
                        {
                            filterName.setError("This is compulsory");
                            return;
                        }
                        if(equal.containsKey("0"))
                        {
                            HashMap<String, String> quesMap = equal.get("0");
                            quesMap.put(finalI+"",filterName.getText().toString());
                            equal.put("0",quesMap);

                        }
                        else
                        {
                            HashMap<String,String>quesMap=new HashMap<>();
                            quesMap.put(finalI+"",filterName.getText().toString());
                            equal.put("0",quesMap);

                        }
                    }


                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(equal.containsKey("0")==false)
                            return;
                        HashMap<String, String> quesMap = equal.get("0");
                        quesMap.remove(finalI+"");
                        equal.put("0",quesMap);
                        filterName.setText(null);
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
                if(range.containsKey("0"))
                {
                    HashMap<String, ArrayList<Double>> quesMap = range.get("0");
                    if(quesMap.containsKey(finalI+""))
                    {
                        ArrayList<Double> minmax=quesMap.get(finalI+"");
                        filterNameMax.setText(minmax.get(1)+"");
                        filterNameMin.setText(minmax.get(0)+"");
                    }
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
                        if(range.containsKey("0"))
                        {
                            HashMap<String, ArrayList<Double>> quesMap = range.get("0");
                            ArrayList<Double> minmax=new ArrayList<>();
                            minmax.add(Double.parseDouble(filterNameMin.getText().toString()));
                            minmax.add(Double.parseDouble(filterNameMax.getText().toString()));
                            quesMap.put(finalI+"",minmax);
                            range.put("0",quesMap);

                        }
                        else
                        {
                            HashMap<String, ArrayList<Double>> quesMap = new HashMap<>();
                            ArrayList<Double> minmax=new ArrayList<>();
                            minmax.add(Double.parseDouble(filterNameMin.getText().toString()));
                            minmax.add(Double.parseDouble(filterNameMax.getText().toString()));
                            quesMap.put(finalI+"",minmax);
                            range.put("0",quesMap);

                        }
                    }
                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(range.containsKey("0")==false)
                            return;
                        HashMap<String, ArrayList<Double>> quesMap = range.get("0");
                        quesMap.remove(finalI+"");
                        filterNameMax.setText(null);
                        filterNameMin.setText(null);
                        range.put("0",quesMap);
                    }
                });
                filterFields.addView(rangeFilter);
            }
        }
        for(int i=0;i<academicQ.size();i++)
        {
            if(academicQ.get(i).getType()==1||academicQ.get(i).getType()==4)
            {
                View stringFilter=getLayoutInflater().inflate(R.layout.repeatable_filter_equal,null);
                TextView questionName=stringFilter.findViewById(R.id.Heading2);
                EditText filterName=stringFilter.findViewById(R.id.FilterValue);
                Button set=stringFilter.findViewById(R.id.set);
                Button remove=stringFilter.findViewById(R.id.remove);
                questionName.setText(academicQ.get(i).getQuestion());
                if(equal.containsKey("1"))
                {
                    HashMap<String, String> quesMap = equal.get("1");
                    filterName.setText(quesMap.get(i+""));
                }
                int finalI = i;

                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(filterName.getText().toString()==null||filterName.getText().toString().length()==0)
                        {
                            filterName.setError("This is compulsory");
                            return;
                        }
                        if(equal.containsKey("1"))
                        {
                            HashMap<String, String> quesMap = equal.get("1");
                            quesMap.put(finalI+"",filterName.getText().toString());
                            equal.put("0",quesMap);

                        }
                        else
                        {
                            HashMap<String,String>quesMap=new HashMap<>();
                            quesMap.put(finalI+"",filterName.getText().toString());
                            equal.put("0",quesMap);

                        }

                    }

                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(equal.containsKey("1")==false)
                            return;
                        HashMap<String, String> quesMap = equal.get(1);
                        quesMap.remove(finalI+"");
                        equal.put("1",quesMap);
                        filterName.setText(null);

                    }
                });
                filterFields.addView(stringFilter);
            }
            else if(academicQ.get(i).getType()==2||academicQ.get(i).getType()==3||i==2)
            {
                View rangeFilter=getLayoutInflater().inflate(R.layout.repeatable_filter_range,null);
                TextView questionName=rangeFilter.findViewById(R.id.Heading2);
                EditText filterNameMin=rangeFilter.findViewById(R.id.FilterValueMin);
                EditText filterNameMax=rangeFilter.findViewById(R.id.FilterValueMax);
                Button remove=rangeFilter.findViewById(R.id.remove1);
                questionName.setText(academicQ.get(i).getQuestion());
                int finalI=i;
                if(range.containsKey("1"))
                {
                    HashMap<String, ArrayList<Double>> quesMap = range.get("1");
                    if(quesMap.containsKey(finalI+"")) {
                        ArrayList<Double> minmax = quesMap.get(finalI + "");
                        filterNameMax.setText(minmax.get(1) + "");
                        filterNameMin.setText(minmax.get(0) + "");
                    }
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
                        if(range.containsKey("1"))
                        {
                            HashMap<String, ArrayList<Double>> quesMap = range.get("1");
                            ArrayList<Double> minmax=new ArrayList<>();
                            minmax.add(Double.parseDouble(filterNameMin.getText().toString()));
                            minmax.add(Double.parseDouble(filterNameMax.getText().toString()));
                            quesMap.put(finalI+"",minmax);
                            range.put("1",quesMap);

                        }
                        else
                        {
                            HashMap<String, ArrayList<Double>> quesMap = new HashMap<>();
                            ArrayList<Double> minmax=new ArrayList<>();
                            minmax.add(Double.parseDouble(filterNameMin.getText().toString()));
                            minmax.add(Double.parseDouble(filterNameMax.getText().toString()));
                            quesMap.put(finalI+"",minmax);
                            range.put("1",quesMap);

                    }
                    }
                });
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(range.containsKey("1")==false)
                            return;
                        HashMap<String, ArrayList<Double>> quesMap = range.get("1");
                        quesMap.remove(finalI+"");
                        filterNameMax.setText(null);
                        filterNameMin.setText(null);
                        range.put("1",quesMap);
                    }
                });
                filterFields.addView(rangeFilter);
            }
        }
    }
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
    public void setRequest(View v)
    {
        AlertDialog.Builder ab=new AlertDialog.Builder(this);

        ab.setTitle("Please confirm the information");
        ab.setMessage("Please enter the name by which this data request is identified");

        View titlePage=getLayoutInflater().inflate(R.layout.repeatable_edit_text_white_layout,null);
        TextView ques=titlePage.findViewById(R.id.Ques);
        ques.setVisibility(View.GONE);
        EditText titleReq=titlePage.findViewById(R.id.editTextTextMultiLine);
        ab.setView(titlePage);
        ab.setCancelable(false);
        ab.setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(titleReq.getText().toString()==null || titleReq.length()==0)
                {
                    titleReq.setError("This is compulsory");
                    return;
                }
                String title=titleReq.getText().toString();
                Date time= Calendar.getInstance().getTime();
                String token=FirebaseAuth.getInstance().getCurrentUser().getUid()+" "+time;
                HashMap<String,Object> requestData=new HashMap<>();
                requestData.put("Title",title);
                requestData.put("Sender",companyData.getEmail());
                requestData.put("Status",0);
                requestData.put("Time",time);
                requestData.put("Branches",allCourseAndBranchRequest);
                requestData.put("Personal Question",perQ);
                requestData.put("Academic Question",acaQ);
                requestData.put("Upload Question",upQ);
                requestData.put("Filters Equal",equal);
                requestData.put("Filters Range",range);
                FirebaseFirestore.getInstance().collection("All Colleges").document(companyData.getCollegeId())
                        .collection("Data Request").document(token)
                        .set(requestData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CompanyRequestDataFromAdmin.this, "Request Sent", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        }).setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //do nothing
            }
        });
        ab.create().show();
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
                    personalDetails.collection(finalI+"").document(finalI+"")
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            CollegeRegisterQuestions crq=new CollegeRegisterQuestions();
                            crq.setQuestion((String) documentSnapshot.get("Question"));
                            long x= (long) documentSnapshot.get("Type");
                            crq.setType((int) x);
                            crq.setId(finalI);
                            personalQ.add(crq);
                            if(personalQ.size() ==total)
                            {
                                Collections.sort(personalQ, new Comparator<CollegeRegisterQuestions>() {
                                    @Override
                                    public int compare(CollegeRegisterQuestions collegeRegisterQuestions, CollegeRegisterQuestions t1) {
                                        return collegeRegisterQuestions.getId()-t1.getId();
                                    }
                                });

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
                                                    crq.setId(finalI2);
                                                    academicQ.add(crq);
                                                    if(academicQ.size()==total2)
                                                    {

                                                        Collections.sort(academicQ, new Comparator<CollegeRegisterQuestions>() {
                                                            @Override
                                                            public int compare(CollegeRegisterQuestions collegeRegisterQuestions, CollegeRegisterQuestions t1) {
                                                                return collegeRegisterQuestions.getId()-t1.getId();
                                                            }
                                                        });

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
                                                                            if(uploadQ.size()==total3)
                                                                            {

                                                                                Collections.sort(uploadQ, new Comparator<CollegeRegisterQuestions>() {
                                                                                    @Override
                                                                                    public int compare(CollegeRegisterQuestions collegeRegisterQuestions, CollegeRegisterQuestions t1) {
                                                                                        return collegeRegisterQuestions.getId()-t1.getId();
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
        });

    }
}