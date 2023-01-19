package com.example.janus;

import junit.framework.TestCase;

import org.junit.Test;

public class logScreenFragmentTest extends TestCase {

    @Test
    public void testLoginNow() {
        String message = "Logged In!";
        logScreenFragment user = new logScreenFragment();
        assertEquals("Success", user.getLogInMessage(message));
    }

    @Test
    public void testLoginNowFail() {
        String message = "Error! Invalid Credentials!";
        logScreenFragment user = new logScreenFragment();
        assertEquals("Failed", user.getLogInMessage(message));
    }

}