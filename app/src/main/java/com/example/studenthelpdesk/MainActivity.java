package com.example.studenthelpdesk;

//package com.example.hp.splashscreen;

import android.app.AlertDialog;
        import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.Settings;

        import androidx.appcompat.app.AppCompatActivity;

        import android.view.View;
/*
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;*/

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    static boolean isadmin=true,done=true;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(MainActivity.this, Signup.class));
        finish();
        /*TextView text = (TextView) findViewById(R.id.textView);
        if(!isConnected(this)){
            showCustomDialog();
        }
        else {
            int SPLASH_SCREEN = 4000;
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
                            DocumentReference dref = fstore.collection("AllowedUser").document(email);
                            dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Map<String, Object> m = documentSnapshot.getData();
                                    isadmin = (boolean) m.get("Admin");
                                    if (isadmin) {
                                        startActivity(new Intent(MainActivity.this, Admin_page.class));
                                        finish();
                                    } else {

                                        startActivity(new Intent(MainActivity.this, Student_page.class));
                                        finish();
                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        done = true;
                    }


                }

            }, 4000, 8000);
            done = false;
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    done = true;
                    FirebaseUser firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
                    if (firebaseAuth != null) {
                        String email = firebaseAuth.getEmail();
                        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                        DocumentReference dref = fstore.collection("AllowedUser").document(email);
                        dref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Map<String, Object> m = documentSnapshot.getData();
                                isadmin = (boolean) m.get("Admin");
                                if (isadmin) {
                                    startActivity(new Intent(MainActivity.this, Admin_page.class));
                                    finish();
                                } else {

                                    startActivity(new Intent(MainActivity.this, Student_page.class));
                                    finish();
                                }
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        SPLASH_SCREEN_TIME_OUT = -1;
                        finish();
                    }
                }

            });
        }*/
    }

    public void letTheUserLoggedIn(View view)
    {


    }
   /*
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

    }*/
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Please Connect to the Internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}



