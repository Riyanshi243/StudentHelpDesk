package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
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
        ll=findViewById(R.id.ll);
        studentData =StudentPage.studentData;
        requestData=new ArrayList<>();
        if(studentData.getNoOfReq()==0)
        {
            TextView t=new TextView(this);
            t.setText("No request sent");
            ll.addView(t);
        }
        else
        {
            CollectionReference docReq = FirebaseFirestore.getInstance().collection("All Colleges").document(studentData.getCollegeid()).collection("Requests");
            docReq.orderBy("Sent Time",com.google.firebase.firestore.Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> docRequest = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot a:docRequest)
                    {
                        RequestData currReq=new RequestData();
                        currReq.setAnswerNow((String) a.get("Answer Now"));
                        currReq.setQuestion((String) a.get("Question"));
                        currReq.setChangeTo((String) a.get("Change To"));
                        currReq.setDomain((Long) a.get("Question Domain"));
                        currReq.setId((Long) a.get("Question Id"));
                        currReq.setSender((String) a.get("Sender"));
                        currReq.setSentTime((String) a.get("Sent Time"));
                        currReq.setStatus((Long) a.get("Status"));
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
                    ll=findViewById(R.id.ll);
                    RequestData currReq=requestData.get(0);
                    View viewReq=getLayoutInflater().inflate(R.layout.repeatable_student_request_status_box,null);
                    TextView header=viewReq.findViewById(R.id.header);
                    TextView reason=viewReq.findViewById(R.id.reason);
                    TextView appliedDate=viewReq.findViewById(R.id.date1);
                    TextView status=viewReq.findViewById(R.id.status);
                    ImageView del=viewReq.findViewById(R.id.DELETE);
                    header.setText("Change "+currReq.getQuestion()+" From "+currReq.getAnswerNow()+" To "+currReq.getChangeTo());
                    // reason.setText(currReq.getReason());
                    appliedDate.setText(currReq.getSentTime());
                    if(currReq.getStatus()<0)
                    {
                        status.setText("Pendeing");
                    }
                    else if (currReq.getStatus()>0)
                    {
                        status.setText("Accepted");
                    }
                    else
                    {
                        status.setText("Rejected");
                    }
                    ll.post(new Runnable() {
                        @Override
                        public void run() {

                            ll.addView(viewReq);
                        }
                    });
                    requestData.remove(currReq);
                }

            }

        }, 0, 1000);

    }
}