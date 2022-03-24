package com.example.studenthelpdesk;

public class FAQData {
    private String content,sender,senderEmail, time, taggedAdmin;

    public String getContentPost() {
        return content;
    }

    public void setContentPost(String content) {
        this.content = content;
    }

    public String getSenderEmail() {
        return sender;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return sender;
    }

    public void setSenderName(String sender) {
        this.sender = sender;
    }

    public String getTimeOfPost() { return time; }

    public void setTimeOfPost(String time) { this.time = time; }

    public String getTaggedAdmin() { return taggedAdmin; }

    public void setTaggedAdmin(String taggedAdmin) {
        this.taggedAdmin = taggedAdmin;
    }

}
