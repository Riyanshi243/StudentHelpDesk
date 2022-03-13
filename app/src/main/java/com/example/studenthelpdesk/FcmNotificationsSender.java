package com.example.studenthelpdesk;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FcmNotificationsSender  {

    String userFcmToken;
    String title;
    String body;
    Activity mActivity;
    String goTo;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAAoVvWBrA:APA91bGXmq0-_dlpHMXARrobQ5PcITPOFfeCSoCjZokSD_VVYxxMYxD3_h1oglXadDuOkqJZP2CwoWNmNJ_S4VwdtwErqE8mPBXn18DO0qrMU5ehO2gCKBKQfV3hDKTOeMrTP0evhVeQ";
    public FcmNotificationsSender(String userFcmToken, String title, String body, Activity mActivity,String goTo) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mActivity = mActivity;
        this.goTo=goTo;

    }

    public void SendNotifications() {

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", "app_logo"); //  icon that exists in drawable only
            JSONObject extra=new JSONObject();
            extra.put("activity",goTo);
            mainObj.put("data",extra);
            mainObj.put("notification", notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    // code run is got response

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // code run is got error

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {


                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;


                }
            };
            requestQueue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
