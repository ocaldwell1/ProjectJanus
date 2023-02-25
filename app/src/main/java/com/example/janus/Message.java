package com.example.janus;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class Message {
    private String sender;
    private String content;
    private Timestamp timestamp;
    public Message(){

    }
    public Message(String sender, String content, Timestamp timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
