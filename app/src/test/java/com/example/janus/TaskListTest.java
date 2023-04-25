package com.example.janus;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import junit.framework.TestCase;

import org.junit.Before;

import java.util.ArrayList;

public class TaskListTest extends TestCase {
    private FireDataReader fdr;
    private ArrayList<Task> taskArrayList;
    private ArrayList<Task> completedList;
    private TaskList taskList;

    @Before
    public void setUp() {
        fdr = mock(FireDataReader.class);
        taskArrayList = new ArrayList<>();
        Task task1 = new Task("task1", "", 1, "4/24/2023", "CSCE");
        task1.setId("1");
        Task task2 = new Task("task2", "", 1, "4/30/2023", "BIOL");
        task2.setId("2");
        Task task3 = new Task("task3", "notes", 3, "4/25/2023","CHEM");
        task3.setId("3");
        taskArrayList.add(task1);
        taskArrayList.add(task2);
        taskArrayList.add(task3);
        completedList = new ArrayList<>();
        when(fdr.hasUser()).thenReturn(true);
        when(fdr.getTaskList()).thenReturn(taskArrayList);
        when(fdr.getCompletedTaskList()).thenReturn(completedList);
        taskList = TaskList.getInstanceWithMock(fdr);
    }

    public void testAddTask() {
        User.getInstanceWithMock(fdr);
        assertEquals(3, taskList.getTaskList().size());
        Task task = new Task("name", "note", 2, "2/2/2023", "myself");
        task.setId("4");
        taskList.addTask(task);
        assertEquals(4, taskList.getTaskList().size());
    }

    public void testRemoveTask() {
        int sizeBefore = taskList.getTaskList().size();
        taskList.removeTask("2");
        assertEquals(sizeBefore - 1, taskList.getTaskList().size());
    }
}
