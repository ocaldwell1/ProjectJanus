package com.example.janus;

public class Contact {
    private final String firstName;
    private final String lastName;
    private final String email;

    public Contact() {
        firstName = "none";
        lastName = "none";
        email = "none";
    }

    public Contact(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
}
