package com.example.janus;

import java.util.ArrayList;

public class FriendRequestList {
    private FireDataReader fireDataReader;
    private ArrayList<FriendRequest> requestList;
    private static FriendRequestList instance;

    public static FriendRequestList getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && instance == null) {
            instance = new FriendRequestList();
        }

        return instance;
    }

    private FriendRequestList() {
        fireDataReader = FireDataReader.getInstance();
        requestList = fireDataReader.getFriendRequests();
    }

    public static FriendRequestList getInstanceWithMock(FireDataReader fdr) {
        if(fdr.hasUser() && instance == null) {
            instance = new FriendRequestList(fdr);
        }

        return instance;
    }

    private FriendRequestList(FireDataReader fdr) {
        fireDataReader = fdr;
        requestList = fireDataReader.getFriendRequests();
    }

    public ArrayList<FriendRequest> getFriendRequests() {
        return requestList;
    }

    public void accept(String sender) {
        FriendRequest found = null;
        for(FriendRequest req : requestList) {
            if(req.getSender().equals(sender)) {
                found = req;
            }
        }
        requestList.remove(found);
        ContactList contactList = ContactList.getInstance();
        contactList.add(sender);
        fireDataReader.acceptFriendRequest(sender);
    }

    public void deny(String sender) {
        FriendRequest found = null;
        for(FriendRequest req : requestList) {
            if(req.getSender().equals(sender)) {
                found = req;
            }
        }
        requestList.remove(found);
        fireDataReader.denyFriendRequest(sender);
    }

    public void signOut() {
        instance = null;
    }
}
