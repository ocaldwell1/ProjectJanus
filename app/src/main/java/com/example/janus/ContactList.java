package com.example.janus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactList {
    private ArrayList<Contact> contactList;
    private FireDataReader fireDataReader;
    private static ContactList instance;

    private ContactList() {
        fireDataReader = FireDataReader.getInstance();
        contactList = fireDataReader.getContactList();
    }

    public static ContactList getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && instance == null) {
            instance = new ContactList();
        }
        return instance;
    }

    public void add(String sender) {
        Map<String, Object> contactData = fireDataReader.getContactData(sender);
        String firstName = contactData.get("firstName").toString();
        String lastName = contactData.get("lastName").toString();
        boolean isBlocked = false;
    }
}
