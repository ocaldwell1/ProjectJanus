package com.example.janus;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.ArrayList;

public class FriendRequestListTest extends TestCase {
    private FriendRequestList frList;
    private ContactList contactList;

    @Before
    public void setUp() {
        FireDataReader fdr = mock(FireDataReader.class);
        FriendRequest request1 = new FriendRequest("Alice", "Bob");
        FriendRequest request2 = new FriendRequest("Charlie", "Bob");
        Contact contact1 = new Contact("Dorian", "Test", "dtest@email.com", false);
        ArrayList<Contact> contactArrayList = new ArrayList<>();
        contactArrayList.add(contact1);
        ArrayList<FriendRequest> frArrayList = new ArrayList<>();
        frArrayList.add(request1);
        frArrayList.add(request2);
        when(fdr.hasUser()).thenReturn(true);
        when(fdr.getFriendRequests()).thenReturn(frArrayList);
        when(fdr.getContactList()).thenReturn(contactArrayList);

        frList = FriendRequestList.getInstanceWithMock(fdr);
        contactList = ContactList.getInstanceWithMock(fdr);
    }

    public void testAccept() {
        int sizeBefore = frList.getFriendRequests().size();
        frList.accept("Alice");
        assertEquals(sizeBefore - 1, frList.getFriendRequests().size());
    }

    public void testDeny() {
        int sizeBefore = frList.getFriendRequests().size();
        frList.deny("Charlie");
        assertEquals(sizeBefore - 1, frList.getFriendRequests().size());
    }

    public void testAcceptRequestNotExist() {
        int sizeBefore = frList.getFriendRequests().size();
        frList.accept("Steve");
        assertEquals(sizeBefore, frList.getFriendRequests().size());
    }
}
