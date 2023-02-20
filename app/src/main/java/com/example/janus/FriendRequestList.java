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

    public ArrayList<FriendRequest> getFriendRequests() {
        return requestList;
    }
}
