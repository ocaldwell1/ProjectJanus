package com.example.janus;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactList {
    private ArrayList<Contact> contactList;
    private FireDataReader fireDataReader;
    Map<String, Object> tmpData;
    private static ContactList instance;
    private static MutableLiveData<Boolean> isLoaded;

    private ContactList() {
        fireDataReader = FireDataReader.getInstance();
        isLoaded = new MutableLiveData<>(false);
        contactList = fireDataReader.getContactList();
    }

    public static ContactList getInstance() {
        if(instance != null) {
            return instance;
        }
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && instance == null) {
            instance = new ContactList();
        }
        return instance;
    }

    // [wmenkus] use for testing
    public static ContactList getInstanceWithMock(FireDataReader fdr) {
        if(fdr.hasUser() && instance == null) {
            instance = new ContactList(fdr);
        }
        return instance;
    }

    private ContactList(FireDataReader fdr) {
        fireDataReader = fdr;
        isLoaded = new MutableLiveData<>(false);
        contactList = fireDataReader.getContactList();
    }

    public void add(String sender) {
        fireDataReader.getContactData(sender);
    }

    public void remove(Contact contact) { fireDataReader.removeContactFromFirestore(contact);}

    public void addContactFromData(Map<String, Object> contactData) {
        String firstName = contactData.get("firstName").toString();
        String lastName = contactData.get("lastName").toString();
        String email = contactData.get("email").toString();
        boolean isBlocked = false;
        contactList.add(new Contact(firstName, lastName, email, isBlocked));
    }

    public void  addTmpDisplay(Map<String, Object> contactData) {
        tmpData = contactData;
        //return contactData;
    }

    public Map<String, Object> getTmpDisplay() {
        if (tmpData != null) {
            return tmpData;
        } else {
            return new HashMap<>();
        }
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

    public void signOut() {
        isLoaded.setValue(false);
        instance = null;
    }
}
