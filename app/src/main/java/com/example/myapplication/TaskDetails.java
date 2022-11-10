package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

//import android.content.Intent;
import android.os.Bundle;

public class TaskDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task_details);
        /**
         Button addTaskNewTaskButton = (Button) findViewById(R.id.addTaskNewTaskButton);
         addTaskNewTaskButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        showNewTaskScreen(view);
        }
        });
         Button addTaskImportTaskButton = (Button) findViewById(R.id.addTaskImportTaskButton);
         addTaskImportTaskButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        showImportTaskScreen(view);
        }
        });     **/
    }

    /**
     public void showNewTaskScreen(View view) {
     Intent intent = new Intent(this, NewTask.class);
     //TODO pass relevant information into the intent?
     startActivity(intent);
     }

     public void showImportTaskScreen(View view) {
     Intent intent = new Intent(this, ImportTask.class);
     //TODO pass relevant information into the intent?
     startActivity(intent);
     }   **/
}