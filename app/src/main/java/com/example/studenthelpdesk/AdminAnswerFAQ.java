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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    LinearLayout ll, ll_FAQFilter;
    ProgressBar pbar;
    ArrayList<FAQData> faqData,faqDataMain;
    HashMap<String, Object> faqDetails;
    Button filterFAQButton, applyButton;
    CheckBox ch1,ch2,ch3,ch4;
    int filterValue=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_answer_faq);
        adminData=AdminPage.adminData;
        pbar=findViewById(R.id.progressBar5);
        faqData=new ArrayList<>();
        faqDataMain=new ArrayList<>();
        faqDetails=new HashMap<>();
        filterFAQButton=(Button) findViewById(R.id.Filter_FAQs);
        applyButton=(Button) findViewById(R.id.apply);
        ll=findViewById(R.id.linearlay);
        ll_FAQFilter=findViewById(R.id.ll_FAQFilter);
        ch1=findViewById(R.id.checkBox1);
        ch2=findViewById(R.id.checkBox2);
        ch3=findViewById(R.id.checkBox3);
        ch4=findViewById(R.id.checkBox4);

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
                    faqDataMain.add(thisFaq);
                }
            }
        });
        Timer t=new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                filterFAQButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(filterFAQButton.getText().toString().equals("FILTER FAQs")) {
                            ll_FAQFilter.setVisibility(View.VISIBLE);
                            filterFAQButton.setText("CLOSE FILTER");
                            ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(ch1.isChecked())
                                    {
                                        check_ch1();
                                    }
                                }
                            });
                            ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(ch2.isChecked())
                                    {
                                        check_ch2();
                                    }
                                }
                            });
                            ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(ch3.isChecked())
                                    {
                                        check_ch3();
                                    }
                                }
                            });
                            ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(ch4.isChecked())
                                    {
                                        check_ch4();
                                    }
                                }
                            });
                        }
                        else
                        {
                            ll_FAQFilter.setVisibility(View.GONE);
                            filterFAQButton.setText("FILTER FAQs");
                        }

                    }
                });
                applyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ch1.isChecked())
                            filterValue=1;
                        if(ch2.isChecked())
                            filterValue=2;
                        if(ch3.isChecked())
                            filterValue=3;
                        if(ch4.isChecked())
                            filterValue=4;
                        ll.removeAllViews();
                        faqData=new ArrayList<>(faqDataMain);
                        ll_FAQFilter.setVisibility(View.GONE);
                        filterFAQButton.setText("FILTER FAQs");
                    }
                });

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
                    answerEmail=currPost.getTaggedAdmin();
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
                               if(isDestroyed())
                                    return;
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
                    if(!answerEmail.equalsIgnoreCase(adminData.getEmail()))
                    {
                        if(filterValue!=1)
                        {

                            faqData.remove(currPost);
                            return;
                        }
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
                        if(filterValue==3) {
                            faqData.remove(currPost);
                            return;
                        }
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
                                if(isDestroyed())
                                    return;
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
                    else
                    {
                        if(filterValue==4) {

                            faqData.remove(currPost);
                            return;
                        }
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
    public void check_ch1()
    {
        if(ch2.isChecked())
            ch2.setChecked(false);
        if(ch3.isChecked())
            ch3.setChecked(false);
        if(ch4.isChecked())
            ch4.setChecked(false);
        ch1.setChecked(true);
    }
    public void check_ch2()
    {
        if(ch1.isChecked())
            ch1.setChecked(false);
        if(ch3.isChecked())
            ch3.setChecked(false);
        if(ch4.isChecked())
            ch4.setChecked(false);
        ch2.setChecked(true);
    }
    public void check_ch3()
    {
        if(ch1.isChecked())
            ch1.setChecked(false);
        if(ch2.isChecked())
            ch2.setChecked(false);
        if(ch4.isChecked())
            ch4.setChecked(false);
        ch3.setChecked(true);
    }
    public void check_ch4()
    {
        if(ch1.isChecked())
            ch1.setChecked(false);
        if(ch3.isChecked())
            ch3.setChecked(false);
        if(ch2.isChecked())
            ch2.setChecked(false);
        ch4.setChecked(true);
    }

}
