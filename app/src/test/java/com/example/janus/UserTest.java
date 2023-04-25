package com.example.janus;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class UserTest extends TestCase {

    private FireDataReader fdr;
    private Map<String, Object> userData;

    @Before
    public void setUp() {
        fdr = mock(FireDataReader.class);
        userData = new HashMap<>();
        userData.put("firstName", "Holly");
        userData.put("lastName", "Bush");
        userData.put("email", "hbush@email.com");
        when(fdr.hasUser()).thenReturn(true);
        when(fdr.getUserData()).thenReturn(userData);
    }

    public void testUserCreated() {
        User user = User.getInstanceWithMock(fdr);
        assertEquals("Holly", user.getFirstName());
    }

    public void testSetPriorityCalculatorBlank() {
        User user = User.getInstanceWithMock(fdr);
        user.setPriorityCalculator("");
        assertEquals(user.getPriorityCalculator().getClass(), StandardPriorityCalculator.class);
    }

    public void testSetPriorityCalculatorLongTasks() {
        User user = User.getInstanceWithMock(fdr);
        user.setPriorityCalculator("Long Tasks");
        assertEquals(user.getPriorityCalculator().getClass(), LongTasksPriorityCalculator.class);
    }

    public void testGetPriorityIndex() {
        User user = User.getInstanceWithMock(fdr);
        user.setPriorityCalculator("Long Tasks");
        assertEquals(1, user.getPriorityMethodIndex());
    }

    public void testGetPriorityIndexBadInput() {
        User user = User.getInstanceWithMock(fdr);
        user.setPriorityCalculator("Nothing");
        assertEquals(0, user.getPriorityMethodIndex());
    }
}