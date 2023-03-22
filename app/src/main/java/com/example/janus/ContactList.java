package com.example.janus;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactList {
    private ArrayList<Contact> contactList;
    private FireDataReader fireDataReader;
    private static ContactList instance;
    private static MutableLiveData<Boolean> isLoaded;

    private ContactList() {
        fireDataReader = FireDataReader.getInstance();
        isLoaded = new MutableLiveData<>(false);
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
        fireDataReader.getContactData(sender);
    }

    public void addContactFromData(Map<String, Object> contactData) {
        String firstName = contactData.get("firstName").toString();
        String lastName = contactData.get("lastName").toString();
        String email = contactData.get("email").toString();
        boolean isBlocked = false;
        contactList.add(new Contact(firstName, lastName, email, isBlocked));
    }

    public void addGroupChat(String groupChatName, ArrayList<String> memberEmails) {
        contactList.add(new Contact("Group", "Chat", groupChatName, false));
        memberEmails.add(User.getInstance().getEmail());
        fireDataReader.addGroupChatToMembers(groupChatName, memberEmails);
    }

    public MutableLiveData<Boolean> isLoaded() {
        if (isLoaded == null) {
            isLoaded = new MutableLiveData<>(false);
        }
        return isLoaded;
    }

    public static void setIsLoaded() {
        isLoaded.setValue(true);
    }
}
