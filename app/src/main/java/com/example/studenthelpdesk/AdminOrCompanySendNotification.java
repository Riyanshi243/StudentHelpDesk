package com.example.studenthelpdesk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessaging;

public class AdminOrCompanySendNotification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_or_company_send_notification);
    }
    public void sendNotif(View v)
    {
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        String title="test";
        String msg="Hello world";
        String token="eyJhbGciOiJSUzI1NiIsImtpZCI6IjI3ZGRlMTAyMDAyMGI3OGZiODc2ZDdiMjVlZDhmMGE5Y2UwNmRiNGQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vc3R1ZGVudGhlbHBkZXNrLWZlYzM4IiwiYXVkIjoic3R1ZGVudGhlbHBkZXNrLWZlYzM4IiwiYXV0aF90aW1lIjoxNjQ0OTk1MzE5LCJ1c2VyX2lkIjoiTVlOZjd3UE1QWWU4SktaenZuMGRIY0tPTXl5MiIsInN1YiI6Ik1ZTmY3d1BNUFllOEpLWnp2bjBkSGNLT015eTIiLCJpYXQiOjE2NDQ5OTUzMjEsImV4cCI6MTY0NDk5ODkyMSwiZW1haWwiOiJzdHVkZW50MkBiaXQuY29tIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7ImVtYWlsIjpbInN0dWRlbnQyQGJpdC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.c1m3_E2GdY0HnsbQSCiRTl4do5lPWQduRrVhMSMzcKvYZbAjHLGedIwUrivXY6tRbpkWUdb95mZCU8P4gUpuF28y3eR6iQRGjGVWpHPBb3pO_2i4cjQuUxker4WL841j3_R8inljyKGRnmh81qf5m34EIMc0guMZapT9VTUfpj9v88bYPLXCE2O8or0OUkb5EmTfhHLBRMwS8xJzj1_0Vhn7Aakj9jf7N4ZxAHTETjNi_cxvhRPnkaC4Q6sZpoK7PUjZsNMhkhg_I_5TzjyDwBf1H1LxVIe6NZ0nhF81JnnAV8KzhBHCkdUO6l_VdZRM8H8N1kXkmUUVct1c2ylQ\n";
        FcmNotificationsSender notificationsSender=new FcmNotificationsSender(token,title,msg,getApplicationContext(),this);
        notificationsSender.SendNotifications();
    }
}