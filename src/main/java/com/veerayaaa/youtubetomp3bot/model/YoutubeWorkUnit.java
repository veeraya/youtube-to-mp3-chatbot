package com.veerayaaa.youtubetomp3bot.model;

public class YoutubeWorkUnit {
    private String replyTo;
    private String text;
    private Source source;


    public YoutubeWorkUnit(String replyTo, String text, Source source) {
        this.replyTo = replyTo;
        this.text = text;
        this.source = source;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String toString() {
        return replyTo + " : " + text;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public enum Source {
        LINE, TELEGRAM
    }
}
