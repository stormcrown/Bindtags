package com.blozi.bindtags.util;

/**
 * Created by fly_liu on 2017/6/30.
 */

public class Music {
    private String title;
    private String singer;

    public Music() {
    }

    public Music(String title, String singer) {
        this.title = title;
        this.singer = singer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
