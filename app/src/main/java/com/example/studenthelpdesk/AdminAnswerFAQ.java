package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.util.Linkify;
import android.util.Log;
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class AdminAnswerFAQ extends AppCompatActivity {
    static AdminData adminData;
    LinearLayout ll;
    ProgressBar pbar;
    ArrayList<FAQData> faqData;
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
                    Timestamp t = (Timestamp) d.get("Sent Time");
                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(t.getSeconds() * 1000L);
                    String dateNTime = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
                    thisFaq.setContentPost((String) d.get("Content"));
                    thisFaq.setSenderName((String) d.get("Sender"));
                    thisFaq.setTimeOfPost(dateNTime);
                    thisFaq.setSenderEmail((String) d.get("SenderEmail"));
                    thisFaq.setTaggedAdmin((String) d.get("Tagged Admin"));
                    thisFaq.setHashtags((ArrayList<String>) d.get("HashTags"));
                    thisFaq.setId(d.getId());
                    if(d.contains("Answer of FAQ"))
                    {
                        thisFaq.setFAQanswer((String) d.get("Answer of FAQ"));
                        thisFaq.setTaggedAdminName((String) d.get("AnswerBy"));
                        Timestamp t2 = (Timestamp) d.get("Answer Time");
                        Calendar cal2 = Calendar.getInstance(Locale.ENGLISH);
                        cal.setTimeInMillis(t2.getSeconds() * 1000L);
                        String dateNTime2 = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal2).toString();
                        thisFaq.setTimeOfAnswer(dateNTime2);
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
                    String senderEmail,answerEmail;
                    TextView postContent=viewPost.findViewById(R.id.question);
                    postContent.setText(currPost.getContentPost());
                    Linkify.addLinks(postContent, Linkify.ALL);
                    postContent.setLinkTextColor(Color.parseColor("#034ABC"));
                    TextView questionTime=viewPost.findViewById(R.id.question_time);
                    questionTime.setText(currPost.getTimeOfPost());
                    TextView sender=viewPost.findViewById(R.id.questionby);
                    sender.setText(currPost.getSenderName());
                    senderEmail=currPost.getSenderEmail();
                    ImageView profilePic2=viewPost.findViewById(R.id.profilepic2);

                    if(senderEmail==null)
                    {
                        //anonymous
                        sender.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //do nothing
                            }
                        });
                        profilePic2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //do nothing
                            }
                        });
                    }
                    else {
                        sender.setPaintFlags(sender.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        sender.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSeeSender(v, senderEmail);
                            }
                        });
                        if(adminData==null)
                            return;
                        StorageReference storageReference2 = FirebaseStorage.getInstance().getReference(adminData.getCollegeId()).child("Photograph").child(senderEmail);
                        storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(AdminAnswerFAQ.this)
                                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .error(R.drawable.error_profile_picture)
                                        .placeholder(R.drawable.default_loading_img)
                                        .into(profilePic2);
                            }
                        });
                        profilePic2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSeeAnswer(v,senderEmail);
                            }
                        });
                    }
                    Button answerButton=(Button) viewPost.findViewById(R.id.answer_button);
                    Button postAnswerButton= (Button) viewPost.findViewById(R.id.post_answer_button);
                    Button discardButton=(Button) viewPost.findViewById(R.id.discard_button);
                    EditText answerToFAQ=viewPost.findViewById(R.id.answer_to_FAQ);
                    LinearLayout answerFAQll=viewPost.findViewById(R.id.answerFAQll);
                    TextView headerMsg=viewPost.findViewById(R.id.msg);
                    answerEmail=currPost.getTaggedAdmin();
                    if(!answerEmail.equalsIgnoreCase(adminData.getEmail()))
                    {
                        headerMsg.setVisibility(View.GONE);
                        answerButton.setVisibility(View.GONE);
                    }
                    LinearLayout llAns=viewPost.findViewById(R.id.llAns);
                    TextView answerby=viewPost.findViewById(R.id.refewName);
                    TextView answerTime=viewPost.findViewById(R.id.answer_time);
                    TextView faqAnswer=viewPost.findViewById(R.id.answer);
                    ImageView profilePic=viewPost.findViewById(R.id.profilepic);
                    if(currPost.getFAQanswer()!=null)
                    {
                        llAns.setVisibility(View.VISIBLE);
                        answerButton.setText("EDIT ANSWER");
                        answerTime.setText(currPost.getTimeOfAnswer());
                        answerby.setText(currPost.getTaggedAdminName());
                        answerby.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSeeAnswer(v,answerEmail);
                            }
                        });
                        answerby.setPaintFlags(answerby.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                        faqAnswer.setText(currPost.getFAQanswer());
                        Linkify.addLinks(faqAnswer, Linkify.ALL);
                        faqAnswer.setLinkTextColor(Color.parseColor("#034ABC"));
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference(adminData.getCollegeId()).child("Photograph").child(answerEmail);
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
                        profilePic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toSeeAnswer(v,answerEmail);
                            }
                        });
                    }
                    answerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            answerFAQll.setVisibility(View.VISIBLE);
                            answerButton.setVisibility(View.GONE);
                            answerToFAQ.setText(faqAnswer.getText().toString());
                            answerToFAQ.requestFocus();
                        }
                    });
                    postAnswerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View v)
                        {
                            if(answerToFAQ.getText().toString().length()==0)
                            {
                                answerToFAQ.setError("This cannot be empty");
                                answerToFAQ.requestFocus();
                                return;
                            }
                            Date t= Calendar.getInstance().getTime();
                            faqDetails.put("Answer of FAQ",answerToFAQ.getText().toString());
                            String ans=answerToFAQ.getText().toString();
                            faqDetails.put("Answer Time",t);
                            String ansTime=t.toString().substring(0,20);
                            faqDetails.put("AnswerBy",adminData.getAdminName());
                            String admin=adminData.getAdminName();
                            DocumentReference faqDoc=FirebaseFirestore.getInstance().collection("All Colleges").document(adminData.getCollegeId()).collection("FAQ").document(currPost.getId());
                            faqDoc.update(faqDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AdminAnswerFAQ.this, "Answer Posted!", Toast.LENGTH_SHORT).show();

                                    answerFAQll.setVisibility(View.GONE);
                                    llAns.setVisibility(View.VISIBLE);
                                    answerby.setText(admin);
                                    answerTime.setText(ansTime);
                                    faqAnswer.setText(ans);
                                    Linkify.addLinks(faqAnswer, Linkify.ALL);
                                    faqAnswer.setLinkTextColor(Color.parseColor("#034ABC"));
                                    answerToFAQ.setText(ans);
                                    answerToFAQ.requestFocus();
                                    answerButton.setVisibility(View.VISIBLE);
                                    answerButton.setText("EDIT ANSWER");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AdminAnswerFAQ.this, "Error Occured! Please try again", Toast.LENGTH_SHORT).show();

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
    public void toSeeSender(View v,String senderEmail)
    {
        Intent intent=new Intent(AdminAnswerFAQ.this,AdminSearchUser.class);
        intent.putExtra("Email",senderEmail);
        startActivity(intent);
    }

    public void toSeeAnswer(View v,String answerEmail)
    {
        Intent intent=new Intent(AdminAnswerFAQ.this,AdminSearchUser.class);
        intent.putExtra("Email",answerEmail);
        startActivity(intent);
    }

}
