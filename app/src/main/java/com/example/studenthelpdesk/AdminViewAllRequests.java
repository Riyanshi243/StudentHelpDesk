package com.example.studenthelpdesk;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdminViewAllRequests extends AppCompatActivity {
    ArrayList<RequestData> requestData;
    AdminData adminData;
    LinearLayout ll;
    ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_all_requests);
        ll=findViewById(R.id.linearlay);
        adminData=AdminPage.adminData;
        pbar=findViewById(R.id.progressBar3);
        requestData=new ArrayList<>();
        CollectionReference docReq = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Requests");
        docReq.orderBy("Sent Time", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> docRequest = queryDocumentSnapshots.getDocuments();
                if(docRequest.size()==0)
                {
                    TextView t=new TextView(AdminViewAllRequests.this);
                    pbar.setVisibility(View.INVISIBLE);
                    t.setText("  You have received NO Edit Requests.");
                    t.setTextSize(20);
                    ll.addView(t);
                }
                for(DocumentSnapshot a:docRequest)
                {
                    RequestData currReq=new RequestData();
                    currReq.setSender((String) a.get("Sender"));
                    currReq.setUid((String)a.get("UID"));
                    currReq.setStatus((Long) a.get("Status"));
                    currReq.setDocId(a.getId());
                    if(currReq.getStatus()!=-1)
                        continue;
                    currReq.setAnswerNow((String) a.get("Answer Now"));
                    currReq.setQuestion((String) a.get("Question"));
                    currReq.setChangeTo((String) a.get("Change To"));
                    currReq.setDomain((Long) a.get("Question Domain"));
                    currReq.setId((Long) a.get("Question Id"));
                    currReq.setSentTime((String) a.get("Sent Time"));
                    currReq.setReason((String) a.get("Reason"));
                    requestData.add(currReq);
                }
            }
        });

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if(requestData.size()==0)
                {
                    pbar.setVisibility(View.INVISIBLE);
                }
                if(requestData.size()!=0)
                {
                    ll=findViewById(R.id.linearlay);
                    RequestData currReq=requestData.get(0);
                    View viewReq=getLayoutInflater().inflate(R.layout.repeatable_admin_view_requests,null);
                    TextView email=viewReq.findViewById(R.id.email);
                    Linkify.addLinks(email, Linkify.ALL);
                    email.setLinkTextColor(Color.parseColor("#034ABC"));
                    TextView header=viewReq.findViewById(R.id.header);
                    header.setTypeface(null, Typeface. BOLD);
                    TextView reason=viewReq.findViewById(R.id.reason_value);
                    TextView appliedDate=viewReq.findViewById(R.id.request_Date);
                    Button accept=viewReq.findViewById(R.id.acc);
                    Button reject=viewReq.findViewById(R.id.rej);
                    header.setText("Change "+currReq.getQuestion()+" From "+currReq.getAnswerNow()+" To "+currReq.getChangeTo());
                    reason.setText(currReq.getReason());
                    Linkify.addLinks(reason, Linkify.ALL);
                    reason.setLinkTextColor(Color.parseColor("#034ABC"));
                    email.setText(currReq.getSender());
                    appliedDate.setText(currReq.getSentTime().substring(0,20));
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder ab=new AlertDialog.Builder(AdminViewAllRequests.this);
                            ab.setCancelable(false)
                                    .setTitle("Are you sure?")
                                    .setMessage("The changes will be reflected in the database")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            HashMap<String,Object> userNewAnswer=new HashMap<>();
                                            userNewAnswer.put("Answer",currReq.getChangeTo());
                                            String domain;
                                            if (currReq.getDomain()==1)
                                            {
                                                domain="Personal Question";
                                            }
                                            else if (currReq.getDomain()==2)
                                                domain="Academic Question";
                                            else
                                                domain="Upload";
                                            DocumentReference student=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(currReq.getSender()).collection(domain).document(currReq.getId()+"");
                                            student.update(userNewAnswer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    if(currReq.getQuestion().equalsIgnoreCase("Name"))
                                                    {
                                                        HashMap<String,Object> updatedValue=new HashMap<>();
                                                        updatedValue.put("Name",currReq.getChangeTo());
                                                        DocumentReference studentMainInfo=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(currReq.getSender());
                                                        studentMainInfo.update(updatedValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                HashMap<String,Object> reqChange=new HashMap<>();
                                                                Date t= Calendar.getInstance().getTime();
                                                                String dateToday=t.toString();
                                                                reqChange.put("Review Time",dateToday);
                                                                reqChange.put("Status",1);
                                                                DocumentReference reqDoc=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Requests").document(currReq.getDocId());
                                                                reqDoc.update(reqChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        String token="/topics/"+currReq.getUid();
                                                                        Log.e("Token",token);
                                                                        FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Request Accepted",currReq.getQuestion()+" changed to "+currReq.getChangeTo(),AdminViewAllRequests.this,"Request");
                                                                        notificationsSender.SendNotifications();

                                                                        Toast.makeText(AdminViewAllRequests.this,"Request Accepted",Toast.LENGTH_LONG).show();
                                                                        ll.removeView(viewReq);
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                    else if (currReq.getQuestion().equalsIgnoreCase("Course"))
                                                    {
                                                        HashMap<String,Object> updatedValue=new HashMap<>();
                                                        updatedValue.put("Course",currReq.getChangeTo());
                                                        DocumentReference studentMainInfo=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(currReq.getSender());
                                                        studentMainInfo.update(updatedValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                HashMap<String,Object> reqChange=new HashMap<>();
                                                                Date t= Calendar.getInstance().getTime();
                                                                String dateToday=t.toString();
                                                                reqChange.put("Review Time",dateToday);
                                                                reqChange.put("Status",1);
                                                                DocumentReference reqDoc=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Requests").document(currReq.getDocId());
                                                                reqDoc.update(reqChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        String token="/topics/"+currReq.getUid();
                                                                        Log.e("Token",token);
                                                                        FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Request Accepted",currReq.getQuestion()+" changed to "+currReq.getChangeTo(),AdminViewAllRequests.this,"Request");
                                                                        notificationsSender.SendNotifications();

                                                                        Toast.makeText(AdminViewAllRequests.this,"Request Accepted",Toast.LENGTH_LONG).show();
                                                                        ll.removeView(viewReq);
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                    else if (currReq.getQuestion().equalsIgnoreCase("Branch"))
                                                    {
                                                        HashMap<String,Object> updatedValue=new HashMap<>();
                                                        updatedValue.put("Branch",currReq.getChangeTo());
                                                        DocumentReference studentMainInfo=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(currReq.getSender());
                                                        studentMainInfo.update(updatedValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                HashMap<String,Object> reqChange=new HashMap<>();
                                                                Date t= Calendar.getInstance().getTime();
                                                                String dateToday=t.toString();
                                                                reqChange.put("Review Time",dateToday);
                                                                reqChange.put("Status",1);
                                                                DocumentReference reqDoc=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Requests").document(currReq.getDocId());
                                                                reqDoc.update(reqChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        String token="/topics/"+currReq.getUid();
                                                                        Log.e("Token",token);
                                                                        FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Request Accepted",currReq.getQuestion()+" changed to "+currReq.getChangeTo(),AdminViewAllRequests.this,"Request");
                                                                        notificationsSender.SendNotifications();

                                                                        Toast.makeText(AdminViewAllRequests.this,"Request Accepted",Toast.LENGTH_LONG).show();
                                                                        ll.removeView(viewReq);
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                    else if (currReq.getQuestion().equalsIgnoreCase("Year"))
                                                    {
                                                        HashMap<String,Object> updatedValue=new HashMap<>();
                                                        updatedValue.put("Year",currReq.getChangeTo());
                                                        DocumentReference studentMainInfo=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(currReq.getSender());
                                                        studentMainInfo.update(updatedValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                HashMap<String,Object> reqChange=new HashMap<>();
                                                                Date t= Calendar.getInstance().getTime();
                                                                String dateToday=t.toString();
                                                                reqChange.put("Review Time",dateToday);
                                                                reqChange.put("Status",1);
                                                                DocumentReference reqDoc=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Requests").document(currReq.getDocId());
                                                                reqDoc.update(reqChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        String token="/topics/"+currReq.getUid();
                                                                        Log.e("Token",token);
                                                                        FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Request Accepted",currReq.getQuestion()+" changed to "+currReq.getChangeTo(),AdminViewAllRequests.this,"Request");
                                                                        notificationsSender.SendNotifications();

                                                                        Toast.makeText(AdminViewAllRequests.this,"Request Accepted",Toast.LENGTH_LONG).show();
                                                                        ll.removeView(viewReq);
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                    else
                                                    {
                                                        HashMap<String,Object> reqChange=new HashMap<>();
                                                        Date t= Calendar.getInstance().getTime();
                                                        String dateToday=t.toString();
                                                        reqChange.put("Review Time",dateToday);
                                                        reqChange.put("Status",1);
                                                        DocumentReference reqDoc=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Requests").document(currReq.getDocId());
                                                        reqDoc.update(reqChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                String token="/topics/"+currReq.getUid();
                                                                Log.e("Token",token);
                                                                FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Request Accepted",currReq.getQuestion()+" changed to "+currReq.getChangeTo(),AdminViewAllRequests.this,"Request");
                                                                notificationsSender.SendNotifications();

                                                                Toast.makeText(AdminViewAllRequests.this,"Request Accepted",Toast.LENGTH_LONG).show();
                                                                ll.removeView(viewReq);
                                                            }
                                                        });
                                                    }

                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //do nothing
                                        }
                                    });
                            ab.create().show();
                        }
                    });
                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder ab=new AlertDialog.Builder(AdminViewAllRequests.this);
                            EditText reason=new EditText(AdminViewAllRequests.this);
                            ab.setCancelable(false)
                                    .setTitle("Are you sure?")
                                    .setMessage("Write the reason to reject")
                                    .setView(reason)
                                    .setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            HashMap<String,Object> reqChange=new HashMap<>();
                                            Date t= Calendar.getInstance().getTime();
                                            String dateToday=t.toString();
                                            if(reason.getText().toString().length()==0) {
                                                reason.setError("Enter a reason");
                                                return;
                                            }
                                            reqChange.put("Review Time",dateToday);
                                            reqChange.put("Reject Reason",reason.getText().toString());
                                            reqChange.put("Status",0);
                                            DocumentReference reqDoc=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("Requests").document(currReq.getDocId());
                                            reqDoc.update(reqChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    String token="/topics/"+currReq.getUid();
                                                    Log.e("Token",token);
                                                    FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Request Rejected",currReq.getQuestion()+" remains "+currReq.getAnswerNow(),AdminViewAllRequests.this,"Request");
                                                    notificationsSender.SendNotifications();
                                                    //FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,"Request Rejected","content.getText().toString()",AdminViewAllRequests.this,"Notification");
                                                    //notificationsSender.SendNotifications();

                                                    Toast.makeText(AdminViewAllRequests.this,"Request Rejected",Toast.LENGTH_LONG).show();
                                                    ll.removeView(viewReq);
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //do nothing
                                        }
                                    });
                            ab.create().show();
                        }
                    });
                    email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(AdminViewAllRequests.this,AdminSearchUser.class);
                            intent.putExtra("Email",email.getText().toString());
                            startActivity(intent);
                        }
                    });
                    ll.post(new Runnable() {
                        @Override
                        public void run() {

                            ll.addView(viewReq);
                        }
                    });
                    requestData.remove(currReq);
                    if(requestData.size()==0)
                        pbar.setVisibility(View.INVISIBLE);
                }

            }

        }, 0, 50);
    }
}