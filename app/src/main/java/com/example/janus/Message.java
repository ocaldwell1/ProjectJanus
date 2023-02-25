package com.example.janus;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.sql.Time;

public class Message {
    private String sender;
    private String receiver;
    private String content;
    public Message(){

    }


    public Message(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
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
