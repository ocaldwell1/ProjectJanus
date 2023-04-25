package com.example.janus;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.hamcrest.CoreMatchers.anything;

import android.os.SystemClock;

//import androidx.fragment.app.testing.FragmentScenario;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import  androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import  androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
//PRECONDITIONS: ACCOUNT IS SIGNED IN AND YOU HAVE HAVE AT LEAST TWO FRIENDS
public class AddGroupChatTest extends TestCase {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void validateAddValidGroupChat() throws InterruptedException {

        ;
        //clicks chat
        SystemClock.sleep(2000);
        onView(withId(R.id.chatPageFragment)).perform(click());
        ;
        //clicks add friend
        onView(withId(R.id.createGroupChat)).perform(click());
        onView(withId(R.id.typeFriendEmail)).perform(click()).perform(typeText("test2@test2.com"));
        onView(withId(R.id.sendIconForChatPage)).perform(click());
        onView(withId(R.id.typeFriendEmail)).perform(click()).perform(typeText("test3@test3.com"));
        onView(withId(R.id.sendIconForChatPage)).perform(click());
        onView(withId(R.id.Done)).perform(click());

        SystemClock.sleep(4000);

    }
}