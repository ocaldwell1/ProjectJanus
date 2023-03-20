package com.example.janus;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;

public class TaskList {
    private MutableLiveData<ArrayList<Task>> taskList;
    private FireDataReader fireDataReader;
    private static TaskList instance;

    public static TaskList getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser() && instance == null) {
            instance = new TaskList();
        }

        return instance;
    }

    private TaskList() {
        fireDataReader = FireDataReader.getInstance();
        taskList = new MutableLiveData<>();
        taskList.setValue(fireDataReader.getTaskList());
    }

    public void sort() {
        Collections.sort(taskList.getValue(), Collections.reverseOrder());
    }

    public void addTask(Task task) {
        taskList.getValue().add(task);
        sort();
        fireDataReader.addTaskToFireStore(task);
    }

    public void removeTask(String id) {
        Task found = getTaskById(id);
        fireDataReader.removeTaskFromFireStore(found);
        taskList.getValue().remove(found);
        sort();
    }

    public int size()
    {
        return taskList.getValue().size();
    }

    public Task get(int pos) {
        return taskList.getValue().get(pos);
    }

    public Task getTaskById(String id) {
        Task foundTask = null;
        for (Task t : taskList.getValue()) {
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

    public MutableLiveData<ArrayList<Task>> getTaskList() {
        if(taskList == null) {
            taskList = new MutableLiveData<>();
        }
        return taskList;
    }
}
