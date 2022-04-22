package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminViewDataRequestsFromCompanyDetails extends AppCompatActivity {
    AdminData adminData;
    TextView companyName,title;
    LinearLayout preq,ureq,areq,freq;
    CompanyData companyData;
    static ArrayList<CollegeRegisterQuestions> allheadings=new ArrayList<>();
    CheckBox verify;
    String doc="";
    static HashMap<Integer, HashMap<Integer,String>> equal=new HashMap<>();
    static HashMap<Integer,HashMap<Integer,ArrayList<Double>>> range=new HashMap<>();
    static HashMap<String,ArrayList<Boolean>> allCourseAndBranchRequest=new HashMap<>();
    String cId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_data_requests_from_company_details);
        adminData=AdminPage.adminData;
        companyData=CompanyPage.companyData;
        companyName=findViewById(R.id.companyName);
        title=findViewById(R.id.RequestTopic);
        verify=findViewById(R.id.checkBox_admin);
        title.setPaintFlags(title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        preq=findViewById(R.id.ll_PRequests);
        ureq=findViewById(R.id.ll_URequests);
        areq=findViewById(R.id.ll_ARequests);
        freq=findViewById(R.id.ll_FRequest);
        allheadings=new ArrayList<>();
        equal=new HashMap<>();
        range=new HashMap<>();
        allCourseAndBranchRequest=new HashMap<>();
        if(adminData!=null)
            cId=adminData.getCollegeId();
        else if(companyData!=null)
            cId= companyData.getCollegeId();
        if(getIntent().hasExtra("From Company"))
        {
            verify.setVisibility(View.GONE);
            boolean status=getIntent().getBooleanExtra("Status",false);
            if(!status)
            {
                Button viewData=(Button) findViewById(R.id.view_this_data);
                viewData.setVisibility(View.INVISIBLE);
            }
            TextView msg=findViewById(R.id.msg);
            msg.setVisibility(View.GONE);
            companyName.setVisibility(View.GONE);
            Button sendData=(Button) findViewById(R.id.send_data);
            sendData.setVisibility(View.GONE);
            Button RejectData=(Button) findViewById(R.id.reject);
            RejectData.setVisibility(View.GONE);
            TextView or=findViewById(R.id.or);
            or.setVisibility(View.GONE);
        }
        if(getIntent().hasExtra("Request")) {
            doc = (getIntent().getStringExtra("Request"));
           FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("Data Request").document(doc)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String companyEmail = (String) documentSnapshot.get("Sender");
                    FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("UsersInfo").document(companyEmail)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            companyName.setText((String) documentSnapshot.get("Company Name"));
                        }
                    });
                    title.setText((String) documentSnapshot.get("Title"));
                    ArrayList<Long> personalDataQuestionsReq = (ArrayList<Long>) documentSnapshot.get("Personal Question");
                    for (long l : personalDataQuestionsReq) {
                        FirebaseFirestore.getInstance().collection("All Colleges").document(cId)
                                .collection("Questions").document("Personal Question")
                                .collection(l + "").document(l + "")
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                CollegeRegisterQuestions thisq = new CollegeRegisterQuestions();
                                thisq.setQuestion((String) documentSnapshot.get("Question"));
                                long t = (long) documentSnapshot.get("Type");
                                thisq.setType((int) t);
                                thisq.setChangeable((Boolean) documentSnapshot.get("Editable"));
                                thisq.setCompulsory((Boolean) documentSnapshot.get("Compulsory"));
                                allheadings.add(thisq);
                                TextView tv = new TextView(AdminViewDataRequestsFromCompanyDetails.this);
                                tv.setText(thisq.getQuestion());
                                preq.addView(tv);
                            }
                        });
                    }
                    ArrayList<Long> academicDataQuestionsReq = (ArrayList<Long>) documentSnapshot.get("Academic Question");
                    for (long l : academicDataQuestionsReq) {
                        FirebaseFirestore.getInstance().collection("All Colleges").document(cId)
                                .collection("Questions").document("Academic Question")
                                .collection(l + "").document(l + "")
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                CollegeRegisterQuestions thisq = new CollegeRegisterQuestions();
                                thisq.setQuestion((String) documentSnapshot.get("Question"));
                                long t = (long) documentSnapshot.get("Type");
                                thisq.setType((int) t);
                                thisq.setChangeable((Boolean) documentSnapshot.get("Editable"));
                                thisq.setCompulsory((Boolean) documentSnapshot.get("Compulsory"));
                                allheadings.add(thisq);
                                TextView tv = new TextView(AdminViewDataRequestsFromCompanyDetails.this);
                                tv.setText(thisq.getQuestion());
                                areq.addView(tv);
                            }
                        });
                    }
                    ArrayList<Long> uploadDataQuestionsReq = (ArrayList<Long>) documentSnapshot.get("Upload Question");
                    for (long l : uploadDataQuestionsReq) {
                        FirebaseFirestore.getInstance().collection("All Colleges").document(cId)
                                .collection("Questions").document("Upload Question")
                                .collection(l + "").document(l + "")
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                CollegeRegisterQuestions thisq = new CollegeRegisterQuestions();
                                thisq.setQuestion((String) documentSnapshot.get("Question"));
                                long t = (long) documentSnapshot.get("Type");
                                thisq.setType((int) t);
                                thisq.setChangeable((Boolean) documentSnapshot.get("Editable"));
                                thisq.setCompulsory((Boolean) documentSnapshot.get("Compulsory"));
                                allheadings.add(thisq);
                                TextView tv = new TextView(AdminViewDataRequestsFromCompanyDetails.this);
                                tv.setText(thisq.getQuestion());
                                ureq.addView(tv);
                            }
                        });
                    }

                    allCourseAndBranchRequest= (HashMap<String, ArrayList<Boolean>>) documentSnapshot.get("Branches");
                    TextView whichbranch=new TextView(AdminViewDataRequestsFromCompanyDetails.this);

                    final String[] what = {""};
                    whichbranch.setText(what[0]);
                    for(String s:allCourseAndBranchRequest.keySet())
                    {
                        ArrayList<Boolean> thisbranch = allCourseAndBranchRequest.get(s);
                        for(int i=0;i<thisbranch.size();i++) {
                            if(thisbranch.get(i)==false)
                                continue;
                            int finalI = i;
                            FirebaseFirestore.getInstance().collection("All Colleges").document(cId)
                                    .collection("Branches").document(s)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    ArrayList<String> allbranch= (ArrayList<String>) documentSnapshot.get("Branches");
                                    what[0] = what[0]+'\n'+s+" : "+allbranch.get(finalI).toString();
                                    whichbranch.setText(what[0]);
                                    freq.removeAllViews();
                                    freq.addView(whichbranch);
                                }
                            });
                        }
                    }
                    HashMap<String, HashMap<String, String>> equal1 = (HashMap<String, HashMap<String, String>>) documentSnapshot.get("Filters Equal");
                    for(String s:equal1.keySet())
                    {
                        int c=Integer.parseInt(s);
                        HashMap<String, String> thismap = equal1.get(s);
                        HashMap<Integer,String> temp=new HashMap<>();
                        if(thismap.size()==0)
                            continue;
                        for(String i:thismap.keySet())
                        {
                            String typeq;
                            if(c==0)
                                typeq="Personal Question";
                            else
                                typeq="Academic Question";
                            int b=Integer.parseInt(i);
                            temp.put(b,thismap.get(i));
                            FirebaseFirestore.getInstance().collection("All Colleges").document(cId)
                                    .collection("Questions").document(typeq).collection(i).document(i)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    what[0] = what[0]+'\n'+documentSnapshot.get("Question")+" = "+thismap.get(i);
                                    whichbranch.setText(what[0]);
                                    freq.removeAllViews();
                                    freq.addView(whichbranch);
                                }
                            });

                        }
                        equal.put(c,temp);
                    }
                    HashMap<String, HashMap<String, ArrayList<Double>>> range1 = (HashMap<String, HashMap<String, ArrayList<Double>>>) documentSnapshot.get("Filters Range");
                    for(String l:range1.keySet())
                    {
                        int c=Integer.parseInt(l);
                        String typeq;
                        if(c==0)
                            typeq="Personal Question";
                        else
                            typeq="Academic Question";

                        HashMap<String, ArrayList<Double>> thismap = range1.get(l);
                        HashMap<Integer,ArrayList<Double>> temp=new HashMap<>();
                        for (String i:thismap.keySet())
                        {
                            int b=Integer.parseInt(i);
                            ArrayList<Double> r = thismap.get(i);
                            temp.put(b,r);

                            FirebaseFirestore.getInstance().collection("All Colleges").document(cId)
                                    .collection("Questions").document(typeq).collection(i).document(i)
                                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    what[0] = what[0]+'\n'+thismap.get(i).get(0)+"<"+documentSnapshot.get("Question")+" < "+thismap.get(i).get(1);
                                    whichbranch.setText(what[0]);
                                    freq.removeAllViews();
                                    freq.addView(whichbranch);
                                }
                            });
                        }
                        range.put(c,temp);
                    }
                }
            });
        }
    }
    public void viewThisData(View v)
    {
        Intent intent=new Intent(AdminViewDataRequestsFromCompanyDetails.this,AdminViewAllStudentData.class);
        intent.putExtra("From Data Requests",true);
        if(getIntent().hasExtra("From Company"))
        {
            intent.putExtra("From Company", true);
        }
        startActivity(intent);
    }
    //0- pending 1- rejected and 2- accepted
    public void acceptReq(View v)
    {
        if(verify.isChecked()==false)
        {
            verify.setError("Please verify");
            return;
        }
        HashMap<String,Object> reqChange=new HashMap<>();
        DocumentReference reqStatus=FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("Data Request").document(doc);
        reqChange.put("Status",2);
        reqStatus.update(reqChange).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AdminViewDataRequestsFromCompanyDetails.this,"Request Accepted",Toast.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminViewDataRequestsFromCompanyDetails.this,"Request Acception Failed",Toast.LENGTH_LONG).show();

            }
        });
    }
    public void rejectReq(View v)
    {
        EditText et=new EditText(this);

        AlertDialog ab=new AlertDialog.Builder(this)
        .setTitle("Are you sure?")
        .setMessage("Provide a reason to reject the request.")
        .setView(et)
        .setCancelable(false)
                .setPositiveButton("Reject",null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
        .show();

        Button posButton=ab.getButton(AlertDialog.BUTTON_POSITIVE);
        posButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reason = et.getText().toString();
                if(reason==null||reason.length()==0)
                {
                    et.setError("Enter a reason");
                    return;
                }
                HashMap<String,Object> reqChange=new HashMap<>();
                DocumentReference reqStatus=FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("Data Request").document(doc);
                reqChange.put("Status",1);
                reqChange.put("Reason",reason);
                reqStatus.update(reqChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AdminViewDataRequestsFromCompanyDetails.this,"Request Rejected",Toast.LENGTH_LONG).show();
                        ab.dismiss();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminViewDataRequestsFromCompanyDetails.this,"Request Rejection Failed",Toast.LENGTH_LONG).show();
                        ab.dismiss();

                    }
                });

            }
        });
    }
}