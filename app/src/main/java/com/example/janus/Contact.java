package com.example.janus;

public class Contact {
    private final String firstName;
    private final String lastName;
    private final String email;
    private boolean blocked;

    public Contact(String firstName, String lastName, String email, boolean isBlocked) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.blocked = isBlocked;
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

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean isBlocked) {
        this.blocked = isBlocked;
    }
}
