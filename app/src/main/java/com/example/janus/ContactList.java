package com.example.janus;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactList {
    private MutableLiveData<ArrayList<Contact>> contactList;
    private FireDataReader fireDataReader;
    private static ContactList instance;

    private ContactList() {
        fireDataReader = FireDataReader.getInstance();
        contactList = new MutableLiveData<>();
        contactList.setValue(fireDataReader.getContactList());
    }

    public static ContactList getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && instance == null) {
            instance = new ContactList();
        }
        return instance;
    }

    public void add(String sender) {
        fireDataReader.getContactData(sender);
    }

    public void addContactFromData(Map<String, Object> contactData) {
        String firstName = contactData.get("firstName").toString();
        String lastName = contactData.get("lastName").toString();
        String email = contactData.get("email").toString();
        boolean isBlocked = false;
        contactList.getValue().add(new Contact(firstName, lastName, email, isBlocked));
    }

    public void addGroupChat(String groupChatName, ArrayList<String> memberEmails) {
        contactList.getValue().add(new Contact("Group", "Chat", groupChatName, false));
        memberEmails.add(User.getInstance().getEmail());
        fireDataReader.addGroupChatToMembers(groupChatName, memberEmails);
    }

    public MutableLiveData<ArrayList<Contact>> getContactList() {
        if (contactList == null) {
            contactList = new MutableLiveData<>();
        }
        return contactList;
    }
}
