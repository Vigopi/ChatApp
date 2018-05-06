package com.example.vinothgopigraj.chatapp;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class Message {
    private String content;
    private String username;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUsername() {
        return username;
    }

    public Message(String content, String username, String time) {
        this.content = content;
        this.username = username;
        this.time = time;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Message()
    { }

    public Message(String content) {
        this.content = content;
    }

    public Message(String content, String username) {
        this.content = content;
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
