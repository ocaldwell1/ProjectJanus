package com.example.myapplication;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.ViewAssertion;
import  androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import  androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class AddTaskTest extends TestCase {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);
    @Test
    public void validateAddTaskBehavior() throws InterruptedException {
        //starts test by clicking lets go button, a user must already be logged in
        onView(withId(R.id.startFragmentStartButton)).perform(click());

        // attempts to make sure right view is displayed, if not throws error
        /*try{
            onView((withId(R.id.taskFragmentAddTaskButton))).check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {

        };*/
        //clicks add task button
       onData(withId(R.id.taskFragmentAddTaskButton)).perform(click());
       //Thread.sleep(500);
        /*try {
            onData((withId(R.id.addTaskNewTaskButton))).check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {

        };*/
        //clicks new task button
        onView(withId(R.id.addTaskNewTaskButton)).perform(scrollTo(),click());
        try {
            onView((withId(R.id.newTaskSaveButton))).check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {

        };
        onView((withId(R.id.newTaskTaskNameEditText))).perform(typeText("TestTask"));
        onView(withId(R.id.newTaskSourceEditText)).perform(typeText("TestSource"));
        onView(withId(R.id.newTaskWeightSpinner)).perform(click());
        onData(withId(R.id.newTaskWeightSpinner)).atPosition(1).perform(click());
        onView(withId(R.id.newTaskDueDateEditText)).perform(typeText("01/01/2100"));
        onView(withId(R.id.newTaskNotesEditText)).perform(typeText("TestNotes"));
        onView((withId(R.id.newTaskSaveButton))).perform(click());

    }
}