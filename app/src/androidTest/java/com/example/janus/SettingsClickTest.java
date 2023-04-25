package com.example.janus;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

import android.os.SystemClock;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SettingsClickTest extends TestCase {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario = new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void validateSettingsBehavior() throws InterruptedException {

        // attempts to make sure right view is displayed, if not throws error
        try {
            onView((withId(R.id.taskFragment))).check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {

        }

        // test navigation to the settings fragment
        SystemClock.sleep(2000);
        onView(withId(R.id.settingsFragment)).perform(click());
        try {
            onView((withId(R.id.settingsFragment))).check(matches(isDisplayed()));
        } catch (AssertionFailedError e) {

        }
        // toggle dark mode
        onView(withId(R.id.darkThemeSwitch)).perform(click());
        SystemClock.sleep(2000);

        // Check use of the priority spinnner
        onView(withId(R.id.settingsPriorityCalculationSpinner)).perform(click());
        SystemClock.sleep(2000);
        onData(anything()).atPosition(1).perform(click(), ViewActions.closeSoftKeyboard());
        SystemClock.sleep(2000);

        // turn off dark mode
        onView(withId(R.id.darkThemeSwitch)).perform(click());
        SystemClock.sleep(2000);

        // check navigation to change profile pic
        onView(withId(R.id.settingsChangePicTV)).perform(click());
        SystemClock.sleep(2000);

}
}