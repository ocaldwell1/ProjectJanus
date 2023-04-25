package com.example.janus;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.graphics.Color;

import junit.framework.TestCase;

import org.junit.Before;

import java.text.ParseException;

public class TaskTest extends TestCase {

    private Task task;
    private Task taskLow;

    @Before
    public void setUp() {
        task = new Task("JUnit Testing", "unit tests for data classes", 3, "4/24/2023", "CSCE 492");
        taskLow = new Task("JUnit Testing", "unit tests for data classes", 1, "4/24/3023", "CSCE 492");
        task.setId("1");
        FireDataReader fdr = mock(FireDataReader.class);
        when(fdr.hasUser()).thenReturn(true);
        User.getInstanceWithMock(fdr);
    }

    public void testGetPriorityHighPriority() {
        double priority = 0;
        try {
            priority = task.getPriority();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals(1.0, priority);
    }

    public void testGetPriorityLowPriority() {
        double priority = 0;
        try {
            priority = taskLow.getPriority();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertTrue(priority < 0.25);
    }

    public void testCompareTo() {
        assertEquals(-1, taskLow.compareTo(task));
    }

    public void testGetHighPriorityColor() {
        assertEquals(Color.RED, task.getPriorityColor());
    }

    public void testGetLowPriorityColor() {
        assertEquals(Color.GREEN, taskLow.getPriorityColor());
    }
}
