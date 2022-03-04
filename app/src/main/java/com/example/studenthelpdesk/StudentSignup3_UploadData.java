package com.example.studenthelpdesk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StudentSignup3_UploadData extends AppCompatActivity
{
    ArrayList<CollegeRegisterQuestions> uQuestion;
    StudentData studentData;
    LinearLayout ll;
    View currentQView;
    ProgressBar progressbar;

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
        setContentView(R.layout.activity_studentsignup3_upload_data);
        studentData=Signup.studentData;
        uQuestion=new ArrayList<>();
        progressbar=findViewById(R.id.progressBar5);
        ll=findViewById(R.id.ll);
        FirebaseFirestore f=FirebaseFirestore.getInstance();
        DocumentReference persQuestions = f.collection("All Colleges").document(studentData.getCollegeid()).collection("Questions").document("Upload Question");
        persQuestions.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                long noOfQuestions= (long) documentSnapshot.get("Total");
                for(int i=0;i<noOfQuestions;i++)
                {
                    DocumentReference quesDetails = persQuestions.collection(i + "").document(i + "");
                    int finalI = i;
                    quesDetails.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            CollegeRegisterQuestions currQ=new CollegeRegisterQuestions();
                            boolean compulsory1= (boolean) documentSnapshot.get("Compulsory");
                            String question1= (String) documentSnapshot.get("Question");
                            long type1=(long)documentSnapshot.get("Type");
                            currQ.setCompulsory(compulsory1);
                            currQ.setId(finalI);
                            currQ.setQuestion(question1);
                            currQ.setType((int) type1);
                            uQuestion.add(currQ);
                            if(uQuestion.size()==noOfQuestions)
                            {
                                Collections.sort(uQuestion,new Comparator<CollegeRegisterQuestions>() {
                                    @Override
                                    public int compare(CollegeRegisterQuestions o1,CollegeRegisterQuestions o2) {
                                        int i1 = (o1.getId() - (o2.getId()));
                                        return i1;
                                    }
                                });
                                for (CollegeRegisterQuestions a:uQuestion)
                                {
                                    int type=a.getType();
                                    String question=a.getQuestion();
                                    int i=a.getId();
                                    boolean compulsory=a.isCumplolsory();
                                    View addQues=getType(type,question);
                                    if(compulsory==true)
                                    {
                                        if(type==0)
                                            addQues.setId((int)8);
                                        else
                                            addQues.setId((int) type);
                                    }
                                    else
                                    {
                                        if(type==0)
                                            addQues.setId((int)-8);
                                        else
                                            addQues.setId((int) -type);
                                    }
                                    ll.addView(addQues);
                                }
                            }

                        }
                    });
                }
            }
        });
    }
    public void saveAndNext(View view)
    {
        int quesNumber=ll.getChildCount();
        int i;
        for(i=0;i<quesNumber;i++)
        {
            View v=ll.getChildAt(i);
            int id=v.getId();
            if(id>0)
            {
                TextView t=v.findViewById(R.id.Ques);
                String s = t.getText().toString();
                if(empty(v,id))
                {
                    return;
                }
            }
            if(i==quesNumber-1)
                startActivity(new Intent(StudentSignup3_UploadData.this,StudentSignupDone.class));

        }

    }
    boolean empty(View v,int i)
    {
        if(i==6)
        {
            //upload
            View nView=v;
            TextView ans=nView.findViewById(R.id.document_name);
            if(ans.length()==0)
            {
                ans.setError("THIS IS COMPULSORY");
                ans.requestFocus();
                return true;
            }

        }

        return false;
    }
    View getType(long i,String q)
    {
        if(i==6)
        {
            //upload
            View nView=getLayoutInflater().inflate(R.layout.repeatable_upload_document,null);
            TextView ques=nView.findViewById(R.id.Ques);
            ques.setText(q);
            Button upload=nView.findViewById(R.id.upload);
            Button preview=nView.findViewById(R.id.preview);
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentQView=nView;
                    if(q.equalsIgnoreCase("Photograph"))
                        uploadPic();
                    else
                        uploadResume();
                }
            });
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // progressbar.setVisibility(View.VISIBLE);
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference(studentData.getCollegeid()).child(q).child(studentData.getEmail());
                    Task<Uri> message = storageRef.getDownloadUrl();
                    message.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (q.equalsIgnoreCase("Photograph")) {
                                LinearLayout ll=nView.findViewById(R.id.ll);
                                if(ll.getChildCount()==0)
                                {
                                    ImageView imageView=new ImageView(nView.getContext());
                                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Glide.with(StudentSignup3_UploadData.this)
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
                            } else
                            {
                                Intent intent = new Intent(view.getContext(), ViewPDFActivity.class);
                                intent.putExtra("url", uri.toString());
                                startActivity(intent);
                            }

                        }
                    });
                }
            });
            return nView;
        }

        return null;
    }
    public void uploadPic()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    public void uploadResume()
    {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,2);
    }
    ProgressDialog dialog;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
           if(resultCode==RESULT_OK) {
               dialog = new ProgressDialog(this);
               dialog.setMessage("UPLOADING");
               dialog.setCancelable(false);
               dialog.show();
               Uri imageuri = data.getData();
               TextView q = currentQView.findViewById(R.id.Ques);
               uploadPic(imageuri, q.getText().toString());
               TextView ans = currentQView.findViewById(R.id.document_name);
               ans.setText(getNameFromURI(data.getData()));
           }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(StudentSignup3_UploadData.this, "ACTIVITY CANCELLED", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                dialog = new ProgressDialog(this);
                dialog.setMessage("UPLOADING");
                dialog.setCancelable(false);
                dialog.show();
                TextView q=currentQView.findViewById(R.id.Ques);
                uploadResume(data.getData(),q.getText().toString());
                TextView ans=currentQView.findViewById(R.id.document_name);
                ans.setText(getNameFromURI(data.getData()));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(StudentSignup3_UploadData.this,"ACTIVITY CANCELLED",Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("Range")
    public String getNameFromURI(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        String string = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        return string;
    }
    public void uploadResume(Uri imageuri2,String q){
        if(imageuri2!=null)
        {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(studentData.getCollegeid()).child(q);
            final StorageReference filepath = storageReference.child(studentData.getEmail());
            filepath.putFile(imageuri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Upload upload=new Upload(imageuri2.toString()+"."+getFileExtension((imageuri2)));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
                    String uploadid=databaseReference.push().getKey();
                    //SignIn.data.setResume(uploadid);
                    if(dialog.isShowing()) {
                        dialog.setMessage("UPLOADED");

                        Toast.makeText(StudentSignup3_UploadData.this, filepath.getName()+".pdf SAVED", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        Button pdf= currentQView.findViewById(R.id.preview);
                        pdf.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this,"NO pdf SELECTED",Toast.LENGTH_LONG).show();
        }
    }
    public void uploadPic(Uri imageuri,String q) {

        if(imageuri!=null)
        {
            FirebaseStorage storage = FirebaseStorage.getInstance();

            StorageReference storageRef=storage.getReference(studentData.getCollegeid()).child(q);

            StorageReference fileReference =storageRef.child((studentData.getEmail()));
            fileReference.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Upload upload=new Upload(imageuri.toString()+"."+getFileExtension((imageuri)));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile");
                    String uploadid=databaseReference.push().getKey();
                    if(dialog.isShowing())
                        dialog.dismiss();
                    Button img= findViewById(R.id.preview);
                    img.setVisibility(View.VISIBLE);

                    //showPic();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StudentSignup3_UploadData.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else
        {
            Toast.makeText(this,"NO IMAGE SELECTED",Toast.LENGTH_LONG).show();
        }

    }
    private  String getFileExtension(Uri uri)
    {
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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