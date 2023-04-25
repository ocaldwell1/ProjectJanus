package com.example.janus;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.sql.Time;

public class Message {
    private String sender;
    private String content;
    private Timestamp timestamp;
    public Message(){

    }


  //  public Message(String sender, String content) {
       // this.sender = sender;
        //this.content = content;
    //}



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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}
