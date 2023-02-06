package com.example.janus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class User {

    private final String firstName, lastName, email;
    private final FireDataReader fireDataReader;
    private final ArrayList <Task> taskList;
    private int taskPosition;
    private static User user;

    private User() {
        fireDataReader = FireDataReader.getInstance();
        Map<String, Object> userData = fireDataReader.getUserData();
        firstName = (String) userData.get("firstName");
        lastName = (String) userData.get("firstName");
        email = (String) userData.get("email");
        taskList = fireDataReader.getTaskList();
    }

    public static User getInstance() {
        FireDataReader fireDataReader = FireDataReader.getInstance();
        if(fireDataReader.hasUser()) {
            user = new User();
        }
        return user;
    }

    public static void sortTaskList(ArrayList<Task> taskList) {
        Collections.sort(taskList, Collections.reverseOrder());
    }

    // get task list [JMS]
    public ArrayList<Task> getTaskList() {
        return this.taskList;
    }
    public void setPosition(int position){this.taskPosition = position;}
    public int getPosition(){return this.taskPosition;}
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getEmail(){
        return this.email;
    }

    public static boolean isNotLoggedIn(){
        return !FireDataReader.getInstance().hasUser();
    }

    public void addTask(Task task) {
        taskList.add(task);
        sortTaskList(taskList);
        task.addTaskToFireStore();
    }

    public void removeTask(String id) {
        Task found = null;
        for(Task task : taskList){
            if(task.getTaskID().equals(id)) {
                found = task;
                task.removeTaskFromFireStore();
            }
        }
        taskList.remove(found);
        sortTaskList(taskList);
    }
}