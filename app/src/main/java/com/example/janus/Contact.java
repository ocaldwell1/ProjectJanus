package com.example.janus;

public class Contact {
    private final String firstName;
    private final String lastName;
    private final String email;
    private boolean friend;

    public Contact(String firstName, String lastName, String email, boolean isFriend) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.friend = isFriend;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean isFriend) {
        this.friend = isFriend;
    }
}
