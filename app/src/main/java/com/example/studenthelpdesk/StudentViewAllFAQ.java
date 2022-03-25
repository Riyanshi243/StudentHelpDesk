package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StudentViewAllFAQ extends AppCompatActivity {
    FloatingActionButton addPost;
    ArrayList<FAQData> faqData;
    static StudentData studentData;
    LinearLayout ll;
    ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_all_faq);
        addPost=(FloatingActionButton)findViewById(R.id.addCourse);
        studentData=StudentPage.studentData;
        pbar=findViewById(R.id.progressBar5);
        faqData=new ArrayList<>();
        ll=findViewById(R.id.linearlay);
        CollectionReference docReq = FirebaseFirestore.getInstance().collection("All Colleges").document(studentData.getCollegeid()).collection("FAQ");
        docReq.orderBy("Sent Time", Query.Direction.DESCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> faqList = queryDocumentSnapshots.getDocuments();
                if(faqList.size()==0)
                {
                    TextView t=new TextView(StudentViewAllFAQ.this);
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
                    View viewPost=getLayoutInflater().inflate(R.layout.repeatable_faq_student,null);

                    ArrayList<String> allHashtags=new ArrayList<>();
                    allHashtags= currPost.getHashtags();
                    LinearLayout hashtags=viewPost.findViewById(R.id.hashtagLinearL);
                    for (int i = 0; i <allHashtags.size() ; i++) {
                        String thisHashtag=allHashtags.get(i);
                        View viewHashtags=getLayoutInflater().inflate(R.layout.repeatable_hashtag2,null);
                        TextView hashvale= viewHashtags.findViewById(R.id.Hashtag);
                        hashvale.setText("#"+thisHashtag);
                        hashtags.addView(viewHashtags);
                    }

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
                    if(faqData.size()==0)
                    {
                        pbar.post(new Runnable() {
                            @Override
                            public void run() {
                                pbar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }
        },1000,100);

    }

    public void postNew(View v)
    {
        startActivity(new Intent(StudentViewAllFAQ.this, StudentPostFAQ.class));
    }
}