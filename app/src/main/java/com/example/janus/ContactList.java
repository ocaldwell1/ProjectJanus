package com.example.janus;

import java.util.ArrayList;

public class ContactList {
    private ArrayList<Contact> contactList;
    private FireDataReader fireDataReader;
    private static ContactList instance;

    private ContactList() {
        //TODO make FireDataReader call to fill in contactList
    }

    public static ContactList getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && instance == null) {
            instance = new ContactList();
        }
        return instance;
    }
}
