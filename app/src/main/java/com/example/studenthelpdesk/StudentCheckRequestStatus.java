package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class StudentCheckRequestStatus extends AppCompatActivity {
    LinearLayout ll;
    StudentData studentData;
    ArrayList<RequestData> requestData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_request_status);
        ll=findViewById(R.id.linearlay);
        studentData =StudentPage.studentData;
        requestData=new ArrayList<>();
        if(studentData.getNoOfReq()==0)
        {
            TextView t=new TextView(this);
            t.setText("  No request sent");
            t.setTextSize(20);
            ll.addView(t);
        }
        else
        {
            CollectionReference docReq = FirebaseFirestore.getInstance().collection("All Colleges").document(studentData.getCollegeid()).collection("Requests");
            docReq.orderBy("Sent Time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> docRequest = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot a:docRequest)
                    {
                        RequestData currReq=new RequestData();
                        currReq.setSender((String) a.get("Sender"));
                        if(currReq.getSender().equalsIgnoreCase(studentData.getEmail())==false)
                            continue;
                        currReq.setAnswerNow((String) a.get("Answer Now"));
                        currReq.setDocId(a.getId());
                        currReq.setQuestion((String) a.get("Question"));
                        currReq.setChangeTo((String) a.get("Change To"));
                        currReq.setDomain((Long) a.get("Question Domain"));
                        currReq.setId((Long) a.get("Question Id"));
                        Timestamp t = (Timestamp)  a.get("Sent Time");
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(t.getSeconds() * 1000L);
                        String dateNTime = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
                        currReq.setSentTime(dateNTime);
                        currReq.setStatus((Long) a.get("Status"));
                        currReq.setReason((String) a.get("Reason"));
                        if(currReq.getStatus()>=0)
                        {
                            Timestamp t1 = (Timestamp)  a.get("Review Time");
                            Calendar cal1 = Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(t.getSeconds() * 1000L);
                            String dateNTime1 = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

                            currReq.setAdminTime(dateNTime1);
                            if(currReq.getStatus()==0)
                            {
                                currReq.setRejectReason((String) a.get("Reject Reason"));
                            }
                        }

                        requestData.add(currReq);
                    }
                }
            });
        }
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                if(requestData.size()!=0)
                {
                    ll=findViewById(R.id.linearlay);
                    RequestData currReq=requestData.get(0);
                    View viewReq=getLayoutInflater().inflate(R.layout.repeatable_student_request_status_box,null);
                    TextView header=viewReq.findViewById(R.id.header);
                    header.setTypeface(null, Typeface. BOLD);
                    TextView reason=viewReq.findViewById(R.id.reason);
                    TextView appliedDate=viewReq.findViewById(R.id.date1);
                    TextView status=viewReq.findViewById(R.id.status);
                    TextView reviewDateText=viewReq.findViewById(R.id.reviewedDateText);
                    TextView date2=viewReq.findViewById(R.id.date2);
                    TextView reasonText=viewReq.findViewById(R.id.reason_);
                    TextView reasonReject=viewReq.findViewById(R.id.reasonvalue);
                    ImageView del=viewReq.findViewById(R.id.DELETE);
                    header.setText("Change "+currReq.getQuestion()+" From "+currReq.getAnswerNow()+" To "+currReq.getChangeTo());
                    reason.setText(currReq.getReason());
                    appliedDate.setText(currReq.getSentTime());
                    if(currReq.getStatus()<0)
                    {
                        status.setText("Pending");
                        reviewDateText.setVisibility(View.GONE);
                        reasonText.setVisibility(View.GONE);
                    }
                    else if (currReq.getStatus()>0)
                    {
                        status.setText("Accepted");
                        status.setBackgroundColor(Color.parseColor("#36e034"));
                        date2.setText(currReq.getAdminTime());
                        date2.setVisibility(View.VISIBLE);
                        reasonText.setVisibility(View.GONE);
                    }
                    else
                    {
                        status.setText("Rejected");
                        status.setBackgroundColor(Color.parseColor("#e83342"));
                        date2.setText(currReq.getAdminTime());
                        date2.setVisibility(View.VISIBLE);
                        reasonReject.setText(currReq.getRejectReason());
                        Linkify.addLinks(reasonReject, Linkify.ALL);
                        reasonReject.setLinkTextColor(Color.parseColor("#034ABC"));
                        reasonReject.setVisibility(View.VISIBLE);
                    }
                    del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder ab=new AlertDialog.Builder(StudentCheckRequestStatus.this);
                            ab.setTitle("Are you sure?")
                                    .setMessage("This will be deleted from database")
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            DocumentReference delDoc=FirebaseFirestore.getInstance().collection("All Colleges").document(studentData.getCollegeid()).collection("Requests").document(currReq.getDocId());
                                            delDoc.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                            HashMap<String,Object> reqUpdate=new HashMap<>();
                                                            reqUpdate.put("Number of Requests",studentData.getNoOfReq()-1);
                                                            FirebaseFirestore.getInstance().collection("All Colleges")
                                                                    .document(studentData.getCollegeid()).collection("UsersInfo")
                                                                    .document(studentData.getEmail()).update(reqUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    studentData.setNoOfReq(studentData.getNoOfReq()-1);
                                                                    Toast.makeText(StudentCheckRequestStatus.this,"Request deleted",Toast.LENGTH_LONG).show();
                                                                    ll.removeView(viewReq);
                                                                }
                                                            });

                                                        }

                                            });
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //nothing
                                        }
                                    })
                                    .create().show();
                        }
                    });
                    ll.post(new Runnable() {
                        @Override
                        public void run() {

                            ll.addView(viewReq);
                        }
                    });
                    requestData.remove(currReq);
                }

            }

        }, 0, 50);

    }
}