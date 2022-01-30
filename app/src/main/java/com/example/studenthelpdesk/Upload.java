package com.example.studenthelpdesk;

public class Upload {
    private String mimageurl;
    public  Upload()
    {
        //empty
    }
    public Upload(String u)
    {

        mimageurl=u;
    }
    public  String getMimageurl()
    {
        return mimageurl;
    }
    public void setMimageurl(String url)
    {
        mimageurl=url;
    }

}

