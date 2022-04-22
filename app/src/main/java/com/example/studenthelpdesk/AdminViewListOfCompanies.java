package com.example.studenthelpdesk;

import static android.view.View.GONE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdminViewListOfCompanies extends AppCompatActivity {
    AdminData adminData;
    LinearLayout ll;
    ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_list_of_companies);
        pbar=findViewById(R.id.progressBar3);
        ll=findViewById(R.id.ll_listOfCompanies);
        adminData=AdminPage.adminData;
        CollectionReference allCompanies = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("UsersInfo");
        allCompanies.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> CompanyDetail = queryDocumentSnapshots.getDocuments();
                for (int i = 0; i < CompanyDetail.size(); i++) {
                    DocumentSnapshot d = CompanyDetail.get(i);
                    String cat = (String) d.get("Category");
                    if (cat.equalsIgnoreCase("Company") == false) {
                        continue;
                    }
                    CompanyData thisCompany=new CompanyData();
                    String eMail=d.getId();
                    thisCompany.setCompanyName((String) d.get("Company Name"));

                    View companyD=getLayoutInflater().inflate(R.layout.repeatable_list_of_companies,null);
                    TextView cName=companyD.findViewById(R.id.cName);
                    ImageView profilepic=companyD.findViewById(R.id.profilePic);
                    Button viewProfile=companyD.findViewById(R.id.viewProfile);
                    cName.setText(thisCompany.getCompanyName());
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference(adminData.getCollegeId()).child("Photograph").child(eMail);
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(AdminViewListOfCompanies.this)
                                    .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .error(R.drawable.admin_profile_img)
                                    .placeholder(R.drawable.default_loading_img)
                                    .into(profilepic);
                        }
                    });

                    viewProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(AdminViewListOfCompanies.this,AdminSearchUser.class);
                            intent.putExtra("Email",eMail);
                            startActivity(intent);
                        }
                    });
                    ll.addView(companyD);
                }

            }
        });
    }
}

