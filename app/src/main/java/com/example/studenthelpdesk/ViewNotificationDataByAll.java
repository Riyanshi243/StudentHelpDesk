package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewNotificationDataByAll extends AppCompatActivity {
    static String intentPoint="Notification Data";
    TextView header,timestamp,sentBy,info;
    LinearLayout ll;
    ImageView profilePic;
    StudentData studentData;
    AdminData adminData;
    CompanyData companyData;
    TextView attachedmsg;
    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    private boolean isZoomAndRotate;
    private boolean isOutSide;
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;
    private float xCoOrdinate, yCoOrdinate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification_data_by_all);
        NotificationData notificationData= (NotificationData) getIntent().getSerializableExtra(intentPoint);
        header=findViewById(R.id.header);
        timestamp=findViewById(R.id.timestamp);
        sentBy=findViewById(R.id.refew);
        info=findViewById(R.id.info);
        ll=findViewById(R.id.linearL);
        studentData=StudentPage.studentData;
        adminData=AdminPage.adminData;
        companyData=CompanyPage.companyData;
        attachedmsg=findViewById(R.id.attachedmsg);
        attachedmsg.setVisibility(View.GONE);
        String cId;
        if(studentData!=null) {
            cId=studentData.getCollegeid();
        }
        else if(adminData!=null) {
            cId=adminData.getCollegeId();
        }
        else {
            cId=companyData.getCollegeId();
        }

        profilePic=findViewById(R.id.profilepic);
        header.setText(notificationData.getTitle());
        if(notificationData.getContent().length()!=0)
            info.setText(notificationData.getContent());
        else
            info.setVisibility(View.GONE);
        sentBy.setText(notificationData.getSentBy());
        timestamp.setText(notificationData.getTimeS());
        ArrayList<String> attachments= notificationData.getAttachment();
        if(attachments!=null)
        {
            attachedmsg.setVisibility(View.VISIBLE);
            for (String a:attachments)
            {
                View repeatAttachment=getLayoutInflater().inflate(R.layout.repeatable_notification_attachment,null);
                Button view=repeatAttachment.findViewById(R.id.view);
                Button download=repeatAttachment.findViewById(R.id.download);
                TextView attach=repeatAttachment.findViewById(R.id.attachment_name);
                attach.setText(a);
                ll.addView(repeatAttachment);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference(cId).child("Notification_"+cId).child(notificationData.getNotifLocation()).child(a);
                        Task<Uri> message = storageRef.getDownloadUrl();
                        message.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                            }
                        });
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference(cId).child("Notification_"+cId).child(notificationData.getNotifLocation()).child(a);
                        if ( a.endsWith(".pdf"))
                        {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Intent intent = new Intent(view.getContext(), ViewPDFActivity.class);
                                    intent.putExtra("url", uri.toString());
                                    startActivity(intent);
                                }
                            });
                        }
                        else
                        {
                            //image viewer
                            LinearLayout ll=repeatAttachment.findViewById(R.id.ll);
                            if(ll.getChildCount()==0)
                            {
                                ImageView imageView=new ImageView(repeatAttachment.getContext());
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Glide.with(ViewNotificationDataByAll.this)
                                                .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .error(R.drawable.error_profile_picture)
                                                .placeholder(R.drawable.default_loading_img)
                                                .into(imageView);
                                        imageView.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                ImageView view = (ImageView) v;
                                                view.bringToFront();
                                                viewTransformation(view, event);
                                                return true;
                                            }
                                        });
                                    }
                                });

                                ll.addView(imageView);
                            }
                            else
                                ll.removeAllViews();

                        }


                    }
                });
            }
        }
       senderMail =notificationData.getSenderMail();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(cId).child("Photograph").child(senderMail);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ViewNotificationDataByAll.this)
                        .load(uri).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.error_profile_picture)
                        .placeholder(R.drawable.default_loading_img)
                        .into(profilePic);
            }
        });
    }
    String senderMail;
    public void toSeeSender(View v)
    {
        Intent intent=new Intent(ViewNotificationDataByAll.this,AdminSearchUser.class);
        intent.putExtra("Email",senderMail);
        startActivity(intent);
    }
    private void viewTransformation(View view, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xCoOrdinate = view.getX() - event.getRawX();
                yCoOrdinate = view.getY() - event.getRawY();

                start.set(event.getX(), event.getY());
                isOutSide = false;
                mode = DRAG;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;
            case MotionEvent.ACTION_UP:
                isZoomAndRotate = false;
                if (mode == DRAG) {
                    float x = event.getX();
                    float y = event.getY();
                }
            case MotionEvent.ACTION_OUTSIDE:
                isOutSide = true;
                mode = NONE;
                lastEvent = null;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOutSide) {
                    if (mode == DRAG) {
                        isZoomAndRotate = false;
                        view.animate().x(event.getRawX() + xCoOrdinate).y(event.getRawY() + yCoOrdinate).setDuration(0).start();
                    }
                    if (mode == ZOOM && event.getPointerCount() == 2) {
                        float newDist1 = spacing(event);
                        if (newDist1 > 10f) {
                            float scale = newDist1 / oldDist * view.getScaleX();
                            view.setScaleX(scale);
                            view.setScaleY(scale);
                        }
                        if (lastEvent != null) {
                            newRot = rotation(event);
                            view.setRotation((float) (view.getRotation() + (newRot - d)));
                        }
                    }
                }
                break;
        }
    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (int) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }


}
