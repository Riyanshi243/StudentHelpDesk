package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
    String senderEmail,answerEmail;
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
                    thisFaq.setSenderEmail((String) d.get("SenderEmail"));
                    thisFaq.setTaggedAdmin((String) d.get("Tagged Admin"));
                    thisFaq.setHashtags((ArrayList<String>) d.get("HashTags"));
                    thisFaq.setId(d.getId());
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
                    View viewPost=getLayoutInflater().inflate(R.layout.repeatable_admin_answer_faq,null);

                    TextView postContent=viewPost.findViewById(R.id.question);
                    postContent.setText(currPost.getContentPost());
                    Linkify.addLinks(postContent, Linkify.ALL);
                    postContent.setLinkTextColor(Color.parseColor("#034ABC"));
                    TextView questionTime=viewPost.findViewById(R.id.question_time);
                    questionTime.setText(currPost.getTimeOfPost().substring(0,20));
                    TextView sender=viewPost.findViewById(R.id.questionby);
                    sender.setPaintFlags(sender.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                    sender.setText(currPost.getSenderName()+": ");

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
                    answerEmail=currPost.getTaggedAdmin();
                    senderEmail=currPost.getSenderEmail();
                    if(!taggedAdmin.equalsIgnoreCase(adminData.getEmail()))
                    {
                        headerMsg.setVisibility(View.GONE);
                        answerButton.setVisibility(View.GONE);
                    }
                    if(currPost.getFAQanswer()!=null)
                    {
                        LinearLayout llAns=viewPost.findViewById(R.id.llAns);
                        llAns.setVisibility(View.VISIBLE);
                        answerButton.setText("EDIT ANSWER");
                        TextView answerTime=viewPost.findViewById(R.id.answer_time);
                        answerTime.setText(currPost.getTimeOfAnswer().substring(0,20));
                        String senderMail =currPost.getTaggedAdmin();
                        TextView answerby=viewPost.findViewById(R.id.refewName);
                        answerby.setText(currPost.getTaggedAdminName());
                        answerby.setPaintFlags(answerby.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                        TextView faqAnswer=viewPost.findViewById(R.id.answer);
                        faqAnswer.setText(currPost.getFAQanswer());
                        Linkify.addLinks(faqAnswer, Linkify.ALL);
                        faqAnswer.setLinkTextColor(Color.parseColor("#034ABC"));
                        ImageView profilePic=viewPost.findViewById(R.id.profilepic);
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(adminData.getCollegeId()).child("Photograph").child(senderMail);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                Glide.with(AdminAnswerFAQ.this)
                                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.drawable.error_profile_picture)
                                        .placeholder(R.drawable.default_loading_img)
                                        .into(profilePic);
                            }
                        });
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
    public void toSeeSender(View v)
    {
        Intent intent=new Intent(AdminAnswerFAQ.this,AdminSearchUser.class);
        intent.putExtra("Email",senderEmail);
        startActivity(intent);
    }
    public void toSeeAnswer(View v)
    {
        Intent intent=new Intent(AdminAnswerFAQ.this,AdminSearchUser.class);
        intent.putExtra("Email",answerEmail);
        startActivity(intent);
    }

}
