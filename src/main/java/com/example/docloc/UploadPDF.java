package com.example.docloc;

public class UploadPDF {
    public String name1;
    public String name;
    public String url;
    public UploadPDF(){}
    public UploadPDF(String name,String url)
    {
        this.name=name;
        this.url = url;
    }
    public void setName1(String name1)
    {
        this.name1=name1;
    }
    public String getname1(){
        return name1;
    }
    public String getName()
    {
        return name;
    }
    public String getUrl()
    {
        return url;
    }
}

