package com.example.janus;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class Message {
    private String sender;
    private String receiver;
    private String content;
    private Timestamp timestamp;
    public Message(){

    }
    public Message(String sender, String receiver, String content, Timestamp timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
