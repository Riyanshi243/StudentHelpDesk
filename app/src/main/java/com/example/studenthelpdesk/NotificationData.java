package com.example.studenthelpdesk;

import android.text.format.DateFormat;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NotificationData implements Serializable {
    private String title,content,sentBy,notifLocation,senderMail,timeS;
    private transient Timestamp sentTime;
    private ArrayList<String> attachment;

    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
    }

    public String getSenderMail() {
        return senderMail;
    }

    public ArrayList<String> getAttachment() {
        return attachment;
    }

    public String getContent() {
        return content;
    }

    public String getTimeS() {
        return timeS;
    }

    public String getNotifLocation() {
        return notifLocation;
    }

    public String getSentBy() {
        return sentBy;
    }

    public String getTitle() {
        return title;
    }

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setAttachment(ArrayList<String> attachment) {
        this.attachment = attachment;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNotifLocation(String notifLocation) {
        this.notifLocation = notifLocation;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(sentTime.getSeconds() * 1000L);
        timeS= DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();


    }

    public void setTitle(String title) {
        this.title = title;
    }
}
