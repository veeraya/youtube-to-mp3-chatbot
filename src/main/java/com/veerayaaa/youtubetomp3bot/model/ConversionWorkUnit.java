package com.veerayaaa.youtubetomp3bot.model;

public class ConversionWorkUnit {
    private String replyTo;
    private String youtubeLink;
    private Source source;


    public ConversionWorkUnit(String replyTo, String youtubeLink, Source source) {
        this.replyTo = replyTo;
        this.youtubeLink = youtubeLink;
        this.source = source;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String toString() {
        return replyTo + " : " + youtubeLink;
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
