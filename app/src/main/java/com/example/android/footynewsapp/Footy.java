package com.example.android.footynewsapp;

public class Footy {

    private String type;
    private String webSectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;

    public Footy(String type, String webTitle, String webPublicationDate, String webSectionName, String webUrl) {
        this.type = type;
        this.webTitle = webTitle;
        this.webPublicationDate = webPublicationDate;
        this.webSectionName = webSectionName;
        this.webUrl = webUrl;
    }

    public String getType() {
        return type;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getWebSectionName() {
        return webSectionName;
    }

    public String getWebUrl() {
        return webUrl;
    }
}

