package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CompanyViewStudentsData extends AppCompatActivity {
    LinearLayout ll;
    CompanyData companyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_view_students_data);
        ll=findViewById(R.id.linearlay);
        companyData=CompanyPage.companyData;
        FirebaseFirestore fs=FirebaseFirestore.getInstance();
        fs.collection("All Colleges").document(companyData.getCollegeId()).collection("Data Request").orderBy("Time", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> requestsDoc = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot r:requestsDoc)
                {
                    if(!(((String)r.get("Sender")).equalsIgnoreCase(companyData.getEmail())))
                        continue;
                    View requestView=getLayoutInflater().inflate(R.layout.repeatable_data_requests_status_of_company,null);
                    TextView titleT=requestView.findViewById(R.id.topic);
                    titleT.setText((String)r.get("Title"));
                    TextView timeT=requestView.findViewById(R.id.Request_time);
                    Timestamp t = (Timestamp)  r.get("Time");
                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(t.getSeconds() * 1000L);
                    String dateNTime = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
                    Button viewDetails=requestView.findViewById(R.id.ViewDetails);
                    timeT.setText(dateNTime);
                    if((long)r.get("Status")==1) {
                        TextView status = requestView.findViewById(R.id.status);
                        status.setText("Rejected");
                        status.setBackgroundColor(Color.parseColor("#e83342"));
                        TextView reasonReject = requestView.findViewById(R.id.reason);
                        reasonReject.setVisibility(View.VISIBLE);
                        reasonReject.setText((String) r.get("Reason"));
                        Linkify.addLinks(reasonReject, Linkify.ALL);
                        reasonReject.setLinkTextColor(Color.parseColor("#034ABC"));
                        reasonReject.setVisibility(View.VISIBLE);
                    }
                    else if((long)r.get("Status")==2) {
                        TextView status = requestView.findViewById(R.id.status);
                        status.setText("Accepted");
                        status.setBackgroundColor(Color.parseColor("#36e034"));
                    }
                    viewDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(CompanyViewStudentsData.this,AdminViewDataRequestsFromCompanyDetails.class);
                            intent.putExtra("Request",r.getId());
                            intent.putExtra("From Company",true);
                            if((long) r.get("Status")==2)
                                intent.putExtra("Status",true);
                            else
                                intent.putExtra("Status",false);
                            startActivity(intent);
                        }
                    });
                    ll.addView(requestView);
                }
            }
        });
    }
}