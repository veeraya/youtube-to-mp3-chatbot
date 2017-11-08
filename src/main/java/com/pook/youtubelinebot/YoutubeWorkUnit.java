package com.pook.youtubelinebot;

/**
 * Created by pook on 8/11/2017.
 */
public class YoutubeWorkUnit {
    private String user;
    private String text;

    public YoutubeWorkUnit(String user, String text) {
        this.user = user;
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return user + " : " + text;
    }
}
