package com.example.android.footynewsapp;

public class Footy {

    private String type;
    private String webSectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;
    private String webAuthor;

    public Footy(String type, String webTitle, String webPublicationDate, String webSectionName, String webUrl, String webAuthor) {
        this.type = type;
        this.webTitle = webTitle;
        this.webPublicationDate = webPublicationDate;
        this.webSectionName = webSectionName;
        this.webUrl = webUrl;
        this.webAuthor = webAuthor;
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

    public String getWebAuthor() {
        return webAuthor;
    }
}

