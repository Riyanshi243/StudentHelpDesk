package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ViewNotificationsByAll extends AppCompatActivity {
    StudentData studentData;
    AdminData adminData;
    CompanyData companyData;
    LinearLayout ll;
    ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications_by_all);
        studentData=StudentPage.studentData;
        adminData=AdminPage.adminData;
        companyData=CompanyPage.companyData;
        pbar=findViewById(R.id.progressBar6);
        ll=findViewById(R.id.linearlay);
        HashSet<String> token;
        ArrayList<NotificationData> allNotif=new ArrayList<>();
        String cId;
        if(studentData!=null) {
            token = studentData.getToken();
            cId=studentData.getCollegeid();
        }
        else if(adminData!=null) {
            token = adminData.getToken();
            cId=adminData.getCollegeId();
        }
        else {
            token = companyData.getToken();
            cId=companyData.getCollegeId();
        }
        Query df=  FirebaseFirestore.getInstance().collection("All Colleges").document(cId).collection("Notification").orderBy("Timestamp",com.google.firebase.firestore.Query.Direction.DESCENDING);
        df.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> notif1 = queryDocumentSnapshots.getDocuments();
                if(notif1.size()==0)
                {
                    TextView t=new TextView(ViewNotificationsByAll.this);
                    pbar.setVisibility(View.INVISIBLE);
                    t.setText("  You have received NO Notifications till now.");
                    t.setTextSize(20);
                    ll.addView(t);
                }
                for(DocumentSnapshot n:notif1)
                {
                    NotificationData nd=new NotificationData();
                    nd.setContent((String) n.get("Content"));
                    nd.setTitle((String) n.get("Title"));
                    nd.setAttachment((ArrayList<String>) n.get("Attachment"));
                    if(n.get("Attachment")!=null)
                        nd.setNotifLocation((String) n.get("Notif Location"));
                    else
                        nd.setNotifLocation(null);
                    nd.setSentBy((String) n.get("Sender"));

                    nd.setSenderMail((String) n.get("Sender mail"));
                    Timestamp date= (Timestamp) n.get("Timestamp");
                    String tokenHere= (String) n.get("Token");
                    nd.setSentTime(date);
                    if(token.contains(tokenHere))
                        allNotif.add(nd);

                }
            }
        });
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                DocumentReference dref = firestore.collection("AllowedUser").document("AdminSettings");
                dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(allNotif.size()==0)
                            return;

                        NotificationData n1=allNotif.get(0);
                        View repeatNotif=getLayoutInflater().inflate(R.layout.repeatable_notifications_received,null);
                        TextView time=repeatNotif.findViewById(R.id.date1);
                        TextView header=repeatNotif.findViewById(R.id.header);
                        header.setTypeface(null, Typeface. BOLD);
                        TextView info=repeatNotif.findViewById(R.id.info);
                        TextView sender=repeatNotif.findViewById(R.id.reason);
                        ImageView attachment=repeatNotif.findViewById(R.id.attachments);
                        header.setText(n1.getTitle());
                        if(n1.getContent().length()<=50) {
                            info.setText(n1.getContent());
                            Linkify.addLinks(info, Linkify.ALL);
                            info.setLinkTextColor(Color.parseColor("#034ABC"));
                        }
                        else
                        {
                            info.setText(n1.getContent().substring(0,50)+"...");
                            Linkify.addLinks(info, Linkify.ALL);
                            info.setLinkTextColor(Color.parseColor("#034ABC"));
                        }

                        if(n1.getAttachment()==null)
                            attachment.setVisibility(View.GONE);
                        sender.setText(n1.getSentBy());

                        Timestamp t = n1.getSentTime();
                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(t.getSeconds() * 1000L);
                        String dateNTime = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
                        time.setText(dateNTime);
                        repeatNotif.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(ViewNotificationsByAll.this,ViewNotificationDataByAll.class);
                                intent.putExtra(ViewNotificationDataByAll.intentPoint, n1);
                                startActivity(intent);
                            }
                        });
                        ll.addView(repeatNotif);
                        allNotif.remove(n1);
                        if(allNotif.size()==0)
                            pbar.setVisibility(View.INVISIBLE);

                    }
                });
            }

        }, 0, 50);

    }

}