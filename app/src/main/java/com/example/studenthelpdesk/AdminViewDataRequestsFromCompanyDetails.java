package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminViewDataRequestsFromCompanyDetails extends AppCompatActivity {
    AdminData adminData;
    TextView companyName,title;
    LinearLayout preq,ureq,areq,freq;

    static HashMap<Integer, HashMap<Integer,String>> equal=new HashMap<>();
    static HashMap<Integer,HashMap<Integer,ArrayList<Double>>> range=new HashMap<>();
    HashMap<String,ArrayList<Boolean>> allCourseAndBranchRequest=new HashMap<>();
    static ArrayList<CollegeRegisterQuestions> personalQ,academicQ,uploadQ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_data_requests_from_company_details);
        adminData=AdminPage.adminData;
        companyName=findViewById(R.id.companyName);
        title=findViewById(R.id.RequestTopic);
        preq=findViewById(R.id.ll_PRequests);
        ureq=findViewById(R.id.ll_URequests);
        areq=findViewById(R.id.ll_ARequests);
        freq=findViewById(R.id.ll_FRequest);
        personalQ=new ArrayList<>();
        academicQ=new ArrayList<>();
        uploadQ=new ArrayList<>();
        String doc="";
        if(getIntent().hasExtra("Request")) {
            doc = (getIntent().getStringExtra("Request"));
            Log.e("Request",doc);
           FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Data Request").document(doc)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String companyEmail = (String) documentSnapshot.get("Sender");
                    Log.e("Sender",companyEmail+" "+documentSnapshot.toString()+" "+documentSnapshot.getString("Sender")+" "+documentSnapshot.get("Time")+" "+ documentSnapshot.exists());
                    FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(companyEmail)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            companyName.setText((String) documentSnapshot.get("Company Name"));
                        }
                    });
                    title.setText((String) documentSnapshot.get("Title"));
                    ArrayList<Long> personalDataQuestionsReq = (ArrayList<Long>) documentSnapshot.get("Personal Question");
                    for (long l : personalDataQuestionsReq) {
                        FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId())
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
                                personalQ.add(thisq);
                                TextView tv = new TextView(AdminViewDataRequestsFromCompanyDetails.this);
                                tv.setText(thisq.getQuestion());
                                preq.addView(tv);
                            }
                        });
                    }
                    ArrayList<Long> academicDataQuestionsReq = (ArrayList<Long>) documentSnapshot.get("Academic Question");
                    for (long l : academicDataQuestionsReq) {
                        FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId())
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
                                academicQ.add(thisq);
                                TextView tv = new TextView(AdminViewDataRequestsFromCompanyDetails.this);
                                tv.setText(thisq.getQuestion());
                                areq.addView(tv);
                            }
                        });
                    }
                    ArrayList<Long> uploadDataQuestionsReq = (ArrayList<Long>) documentSnapshot.get("Upload Question");
                    for (long l : uploadDataQuestionsReq) {
                        FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId())
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
                                uploadQ.add(thisq);
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

                            Log.e("Branches",thisbranch.toString()+i+" "+s);

                            int finalI = i;
                            FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId())
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
                            FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId())
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

                            FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId())
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
}