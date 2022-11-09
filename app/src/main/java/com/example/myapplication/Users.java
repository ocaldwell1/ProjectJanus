package com.example.myapplication;

public class Users {
    public String first, last, email, pass;
    public int id;

    public Users(){

    }
    public Users(String first, String last, String email, String pass, int id){
        this.first = first;
        this.last = last;
        this.email = email;
        this.pass = pass;
        this.id = id;
    }
}
