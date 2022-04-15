package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminViewDataRequestsFromCompany extends AppCompatActivity {
    AdminData adminData;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_data_requests_from_company);
        adminData=AdminPage.adminData;
        ll=findViewById(R.id.linearlay);
        FirebaseFirestore fs=FirebaseFirestore.getInstance();
        fs.collection("All Colleges").document(adminData.getCollegeId()).collection("Data Request").orderBy("Time", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> requestsDoc = queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot r:requestsDoc)
                {
                    View requestView=getLayoutInflater().inflate(R.layout.repeatable_data_requests_from_company,null);
                    TextView titleT=requestView.findViewById(R.id.topic);
                    titleT.setText((String)r.get("Title"));
                    TextView companyNameT=requestView.findViewById(R.id.companyName);
                    ImageView profilepic_company=requestView.findViewById(R.id.profilepic_company);
                    TextView timeT=requestView.findViewById(R.id.Request_time);
                    String companyEmail= (String) r.get("Sender");
                    Timestamp t = (Timestamp)  r.get("Time");
                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(t.getSeconds() * 1000L);
                    String dateNTime = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
                    timeT.setText(dateNTime);
                    FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo").document(companyEmail)
                            .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            companyNameT.setText((String) documentSnapshot.get("Company Name"));
                        }
                    });
                    StorageReference storageref = FirebaseStorage.getInstance().getReference().child(adminData.getCollegeId()).child("Photograph").child(companyEmail);
                    storageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(AdminViewDataRequestsFromCompany.this)
                                    .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .error(R.drawable.admin_profile_img)
                                    .placeholder(R.drawable.default_loading_img)
                                    .into(profilepic_company);

                        }
                    });
                    Button view=requestView.findViewById(R.id.ViewDetails);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i=new Intent(AdminViewDataRequestsFromCompany.this,AdminViewDataRequestsFromCompanyDetails.class);
                            i.putExtra("Request",r.getId());
                            startActivity(i);

                        }
                    });
                    ll.addView(requestView);
                }
            }
        });
    }
}