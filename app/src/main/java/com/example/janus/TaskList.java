package com.example.janus;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;

public class TaskList {
    private ArrayList<Task> taskList;
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
        fireDataReader.removeTaskFromFireStore(found);
        taskList.remove(found);
        sort();
    }

    public int size()
    {
        return taskList.size();
    }

    public Task get(int pos) {
        return taskList.get(pos);
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

    public MutableLiveData<Boolean> isLoaded() {
        if(isLoaded == null) {
            isLoaded = new MutableLiveData<>(false);
        }
        return isLoaded;
    }

    public static void setIsLoaded() {
        isLoaded.setValue(true);
    }
}
