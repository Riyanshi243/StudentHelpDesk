package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class AdminPage extends AppCompatActivity {

     ImageView create_new_user,search_user,view_all_data,see_req,send_notif,faq,lockdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
         create_new_user=(ImageView)findViewById(R.id. create_new_user);
         search_user=(ImageView)findViewById(R.id. search_user);
         view_all_data=(ImageView)findViewById(R.id. view_all_data);
         send_notif=(ImageView)findViewById(R.id. send_notif);
         faq=(ImageView)findViewById(R.id. faq);
        lockdatabase=(ImageView)findViewById(R.id. send_notif);//ye send notif kaise h?
        //ye sab delete hone wale h




    }
}