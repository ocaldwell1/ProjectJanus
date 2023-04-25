package com.example.janus;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;

public class TaskList {
    private ArrayList<Task> taskList;
    private ArrayList<Task> completedTaskList,currentTaskList;
    private FireDataReader fireDataReader;
    private static TaskList instance;
    private static MutableLiveData<Boolean> isLoaded;

    public static TaskList getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && instance == null) {
            instance = new TaskList();
        }

        return instance;
    }

    private TaskList() {
        fireDataReader = FireDataReader.getInstance();
        isLoaded = new MutableLiveData<>(false);
        taskList = fireDataReader.getTaskList();
        completedTaskList = fireDataReader.getCompletedTaskList();
    }

    public static TaskList getInstanceWithMock(FireDataReader fdr) {;
        if(fdr.hasUser() && instance == null) {
            instance = new TaskList(fdr);
        }

        return instance;
    }

    private TaskList(FireDataReader fdr) {
        fireDataReader = fdr;
        isLoaded = new MutableLiveData<>(false);
        taskList = fireDataReader.getTaskList();
        completedTaskList = fireDataReader.getCompletedTaskList();
    }

    public void sort() {
        Collections.sort(taskList, Collections.reverseOrder());
    }

    public void addTask(Task task) {
        taskList.add(task);
        sort();
        fireDataReader.addTaskToFireStore(task);
    }

    public void removeTask(String id) {
        Task found = getTaskById(id);
        taskList.remove(found);
        fireDataReader.removeTaskFromFireStore(found);
        found.setCompleted();
        fireDataReader.addTaskToFireStore(found);
        completedTaskList.add(found);
        sort();
    }

    public void deleteTask(String id) {
        Task found = getTaskById(id);
        taskList.remove(found);
        fireDataReader.removeTaskFromFireStore(found);
    }

    public void deleteCompletedTask(String id) {
        Task found = getCompletedTaskById(id);
        completedTaskList.remove(found);
        fireDataReader.removeTaskFromFireStore(found);
    }

    public int size()
    {
        return taskList.size();
    }

    public Task get(int pos) {
        return taskList.get(pos);
    }

    public Task getCompletedTask(int pos) {
        return completedTaskList.get(pos);
    }

    public Task getTaskById(String id) {
        Task foundTask = null;
        for (Task t : taskList) {
            if(t.getId().equals(id)) {
                foundTask = t;
            }
        }
        return foundTask;
    }

    public Task getCompletedTaskById(String id) {
        Task foundTask = null;
        for (Task t : completedTaskList) {
            if(t.getId().equals(id)) {
                foundTask = t;
            }
        }
        return foundTask;
    }

    public void editTask(String taskId, String newName, String newSource, int newWeight, String newDueDate, String newNote) {
        Task task = getTaskById(taskId);
        task.edit(newName, newSource, newWeight, newDueDate, newNote);
        fireDataReader.removeTaskFromFireStore(task);
        fireDataReader.addTaskToFireStore(task);
        sort();
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public ArrayList<Task> getCompletedTaskList() {
        return completedTaskList;
    }

    public MutableLiveData<Boolean> isLoaded() {
        if(isLoaded == null) {
            isLoaded = new MutableLiveData<>(false);
        }
        return isLoaded;
    }

    public static void setIsLoaded() {
        isLoaded.setValue(true);
    }

    public void signOut() {
        isLoaded.setValue(false);
        instance = null;
    }
}
