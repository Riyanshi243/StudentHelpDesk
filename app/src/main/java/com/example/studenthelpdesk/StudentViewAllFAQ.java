package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
        ll=findViewById(R.id.linearlay);
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
                    thisFaq.setSenderEmail((String) d.get("SenderEmail"));
                    thisFaq.setHashtags((ArrayList<String>) d.get("HashTags"));
                    if(d.contains("Answer of FAQ"))
                    {
                        thisFaq.setFAQanswer((String) d.get("Answer of FAQ"));
                        thisFaq.setTaggedAdminName((String) d.get("AnswerBy"));
                        thisFaq.setTimeOfAnswer((String) d.get("Answer Time"));
                    }
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
                    ArrayList<String> allHashtags= new ArrayList<>();
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
                    String senderEmail,answerEmail;
                    TextView postContent=viewPost.findViewById(R.id.question);
                    postContent.setText(currPost.getContentPost());
                    Linkify.addLinks(postContent, Linkify.ALL);
                    postContent.setLinkTextColor(Color.parseColor("#034ABC"));
                    TextView questionTime=viewPost.findViewById(R.id.question_time);
                    questionTime.setText(currPost.getTimeOfPost().substring(0,20));
                    TextView sender=viewPost.findViewById(R.id.questionby);
                    sender.setText(currPost.getSenderName());
                    senderEmail=currPost.getSenderEmail();
                    if(senderEmail==null)
                    {
                        //toast
                    }
                    else {
                        sender.setPaintFlags(sender.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                        sender.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSeeSender(v, senderEmail);
                            }
                        });
                    }
                    if(currPost.getFAQanswer()!=null)
                    {
                        LinearLayout llAns=viewPost.findViewById(R.id.llAns);
                        llAns.setVisibility(View.VISIBLE);
                        answerEmail=currPost.getTaggedAdmin();
                        TextView answerTime=viewPost.findViewById(R.id.answer_time);
                        answerTime.setText(currPost.getTimeOfAnswer().substring(0,20));
                        String senderMail =currPost.getTaggedAdmin();
                        TextView answerby=viewPost.findViewById(R.id.refewName);
                        answerby.setPaintFlags(answerby.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                        answerby.setText(currPost.getTaggedAdminName());
                        answerby.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSeeAnswer(v,answerEmail);
                            }
                        });
                        TextView faqAnswer=viewPost.findViewById(R.id.answer);
                        faqAnswer.setText(currPost.getFAQanswer());
                        Linkify.addLinks(faqAnswer, Linkify.ALL);
                        faqAnswer.setLinkTextColor(Color.parseColor("#034ABC"));
                        ImageView profilePic=viewPost.findViewById(R.id.profilepic);
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(studentData.getCollegeid()).child("Photograph").child(senderMail);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                Glide.with(StudentViewAllFAQ.this)
                                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.drawable.error_profile_picture)
                                        .placeholder(R.drawable.default_loading_img)
                                        .into(profilePic);
                            }
                        });
                    }
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

    public void toSeeSender(View v,String senderEmail)
    {
        Intent intent=new Intent(StudentViewAllFAQ.this,AdminSearchUser.class);
        intent.putExtra("Email",senderEmail);
        startActivity(intent);
    }

    public void toSeeAnswer(View v,String answerEmail)
    {
        Intent intent=new Intent(StudentViewAllFAQ.this,AdminSearchUser.class);
        intent.putExtra("Email",answerEmail);
        startActivity(intent);
    }
}