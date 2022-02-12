package com.example.studenthelpdesk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
        import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    static boolean done=true;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = (TextView) findViewById(R.id.textView);
        if(!isConnected(this)){
            showCustomDialog();
        }
        else {
            int SPLASH_SCREEN = 2000;
            if (done == false)
                new Handler().postDelayed(() -> {
                }, SPLASH_SCREEN);
            Timer t = new Timer();
            t.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    if (done == false) {
                        FirebaseUser firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseAuth != null) {
                            String email = firebaseAuth.getEmail();
                            FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                            DocumentReference dref = fstore.collection("All Users On App").document(email);
                            dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    String category= (String) documentSnapshot.get("Category");
                                    //Log.e("Hi","Hi");
                                    if(category==null)
                                    {
                                        startActivity(new Intent(MainActivity.this,Login.class));
                                        finish();
                                    }
                                    else if(category.equalsIgnoreCase("Admin"))
                                    {
                                        startActivity(new Intent(MainActivity.this,AdminPage.class));
                                        finish();
                                    }
                                    else if(category.equalsIgnoreCase("Student"))
                                    {
                                        startActivity(new Intent(MainActivity.this,StudentPage.class));
                                        finish();

                                    }
                                    else if(category.equalsIgnoreCase("Company"))
                                    {

                                        startActivity(new Intent(MainActivity.this,CompanyPage.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Intent intent = new Intent(MainActivity.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });


                        } else {
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            startActivity(intent);
                            finish();
                        }
                        done = true;
                    }


                }

            }, 2000, 2000);
            done = false;
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    done = true;
                    FirebaseUser firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseAuth != null) {
                        String email = firebaseAuth.getEmail();
                        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                        DocumentReference dref = fstore.collection("All Users On App").document(email);
                        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String category= (String) documentSnapshot.get("Category");
                                //Log.e("Hi","Hi");
                                if(category==null)
                                {
                                    startActivity(new Intent(MainActivity.this,Login.class));
                                    finish();
                                }
                                else if(category.equalsIgnoreCase("Admin"))
                                {
                                    startActivity(new Intent(MainActivity.this,AdminPage.class));
                                    finish();
                                }
                                else if(category.equalsIgnoreCase("Student"))
                                {
                                    startActivity(new Intent(MainActivity.this,StudentPage.class));
                                    finish();

                                }
                                else if(category.equalsIgnoreCase("Company"))
                                {

                                    startActivity(new Intent(MainActivity.this,CompanyPage.class));
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                            }
                        });


                    } else {
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        SPLASH_SCREEN_TIME_OUT = -1;
                        finish();
                    }
                }

            });
        }
    }



    private boolean isConnected(MainActivity mainActivity) {
        ConnectivityManager connectivityManager= (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileC=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifiConn!=null && wifiConn.isConnected() || mobileC!=null && mobileC.isConnected()){
            return true;
        }
        else
        {
            return false;
        }

    }
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("PLEASE CONNECT OT THE INTERNET TO PROCEED FURTHER")
                .setCancelable(false)
                .setPositiveButton("CONNECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}



