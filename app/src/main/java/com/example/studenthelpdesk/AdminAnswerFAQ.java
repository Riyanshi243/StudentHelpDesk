package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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

public class AdminAnswerFAQ extends AppCompatActivity {
    static AdminData adminData;
    LinearLayout ll;
    ProgressBar pbar;
    ArrayList<FAQData> faqData;
    FAQData FAQData;
    HashMap<String, Object> faqDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_answer_faq);
        adminData=AdminPage.adminData;
        pbar=findViewById(R.id.progressBar5);
        faqData=new ArrayList<>();
        faqDetails=new HashMap<>();

        CollectionReference docReq = FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("FAQ");
        docReq.orderBy("Sent Time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> faqList = queryDocumentSnapshots.getDocuments();
                if(faqList.size()==0)
                {
                    TextView t=new TextView(AdminAnswerFAQ.this);
                    pbar.setVisibility(View.GONE);
                    t.setText("  You have no related posts yet.");
                    t.setTextSize(20);
                    ll.addView(t);
                }
                for (int i = 0; i < faqList.size(); i++) {
                    FAQData thisFaq=new FAQData();
                    DocumentSnapshot d=faqList.get(i);
                    thisFaq.setContentPost((String) d.get("Content"));
                    thisFaq.setSenderName((String) d.get("Sender"));
                    thisFaq.setTimeOfPost((String) d.get("Sent Time"));
                    thisFaq.setTaggedAdmin((String) d.get("Tagged Admin"));
                    thisFaq.setHashtags((ArrayList<String>) d.get("HashTags"));
                    thisFaq.setId(d.getId());
                    faqData.add(thisFaq);
                }
            }
        });
        Timer t=new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(faqData.size()==0)
                {
                    pbar.post(new Runnable() {
                        @Override
                        public void run() {
                            pbar.setVisibility(View.GONE);
                        }
                    });
                }
                if(faqData.size()>0)
                {
                    ll=findViewById(R.id.linearlay);
                    FAQData currPost=faqData.get(0);
                    View viewPost=getLayoutInflater().inflate(R.layout.repeatable_admin_answer_faq,null);
                    ArrayList<String> allHashtags = new ArrayList<>();
                    allHashtags= currPost.getHashtags();
                    LinearLayout hashtags=viewPost.findViewById(R.id.hashtagLinearL);
                    for (int i = 0; i <allHashtags.size() ; i++)
                    {
                        String thisHashtag=allHashtags.get(i);
                        View viewHashtags=getLayoutInflater().inflate(R.layout.repeatable_hashtag2,null);
                        TextView hashvale= viewHashtags.findViewById(R.id.Hashtag);
                        hashvale.setText("#"+thisHashtag);
                        hashtags.addView(viewHashtags);
                    }
                    Button answerButton=(Button) viewPost.findViewById(R.id.answer_button);
                    Button postAnswerButton= (Button) viewPost.findViewById(R.id.post_answer_button);
                    Button discardButton=(Button) viewPost.findViewById(R.id.discard_button);
                    EditText answerToFAQ=viewPost.findViewById(R.id.answer_to_FAQ);
                    LinearLayout answerFAQll=viewPost.findViewById(R.id.answerFAQll);
                    TextView headerMsg=viewPost.findViewById(R.id.msg);
                    String taggedAdmin= currPost.getTaggedAdmin();
                    if(!taggedAdmin.equalsIgnoreCase(adminData.getEmail()))
                    {
                        headerMsg.setVisibility(View.GONE);
                        answerButton.setVisibility(View.GONE);
                    }
                    answerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            answerFAQll.setVisibility(View.VISIBLE);
                            answerButton.setVisibility(View.GONE);
                        }
                    });
                    postAnswerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(answerToFAQ.getText().toString().length()==0)
                            {
                                answerToFAQ.setError("This cannot be empty");
                                answerToFAQ.requestFocus();
                                return;
                            }
                            Date t= Calendar.getInstance().getTime();
                            faqDetails.put("Answer of FAQ",answerToFAQ.getText().toString());
                            faqDetails.put("Answer Time",t.toString());
                            faqDetails.put("AnswerBy",adminData.getAdminName());
                            DocumentReference faqDoc=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("FAQ").document(currPost.getId());
                            faqDoc.update(faqDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminAnswerFAQ.this, "Answer Posted!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminAnswerFAQ.this, "Error Occured! Please try again", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    });
                    discardButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            answerFAQll.setVisibility(View.GONE);
                            answerButton.setVisibility(View.VISIBLE);
                        }
                    });

                    TextView postContent=viewPost.findViewById(R.id.question);
                    postContent.setText(currPost.getContentPost());
                    Linkify.addLinks(postContent, Linkify.ALL);
                    postContent.setLinkTextColor(Color.parseColor("#034ABC"));
                    TextView questionTime=viewPost.findViewById(R.id.question_time);
                    questionTime.setText(currPost.getTimeOfPost().substring(0,20));
                    TextView sender=viewPost.findViewById(R.id.questionby);
                    sender.setText(currPost.getSenderName()+": ");
                    ll.post(new Runnable() {
                        @Override
                        public void run() {
                            ll.addView(viewPost);
                        }
                    });
                    faqData.remove(currPost);
                }
            }
        },1000,100);
    }

}
