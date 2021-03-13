package com.example.tasktracdemo2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tasktracdemo2.Adapter.TaskAdapter;
import com.example.tasktracdemo2.Class.AddNewTask;
import com.example.tasktracdemo2.Class.Task;
import com.example.tasktracdemo2.HelperAndHandler.DBHandler;
import com.example.tasktracdemo2.HelperAndHandler.RecyclerItemTouchHelper;
import com.example.tasktracdemo2.Interfaces.DialogCloseListener;
import com.example.tasktracdemo2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements DialogCloseListener {

    //global variables
    private RecyclerView taskRView; //global variable for the recycler view
    private TaskAdapter task_adapter;
    public static List<Task> allTaskList; // list to save the Task object and it is made public
    private FloatingActionButton fabBtn;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        //set ActionBar title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TaskTrac");
        actionBar.setDisplayShowHomeEnabled(true);

        //instantiate the db and taskList variables
        db = new DBHandler(this);
        db.openDatabase();

        allTaskList = new ArrayList<>();

        //Instantiate the RecycleView variable created
        taskRView = findViewById(R.id.taskRecyclerView);
        taskRView.setHasFixedSize(true);
        taskRView.setLayoutManager(new LinearLayoutManager(this));

        //Instantiate the taskAdapter
        task_adapter = new TaskAdapter(db, this);
        taskRView.setAdapter(task_adapter);

        allTaskList = db.getAllTask();
        Collections.reverse(allTaskList);
        task_adapter.setTasks(allTaskList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(task_adapter));
        itemTouchHelper.attachToRecyclerView(taskRView);

        //FloatingActionButton
        fabBtn = findViewById(R.id.fabBtn);

        fabBtn.setOnClickListener(v -> AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG));
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        allTaskList = db.getAllTask();
        Collections.reverse(allTaskList); //reverse the task list
        task_adapter.setTasks(allTaskList);
        task_adapter.notifyDataSetChanged();
    }

    // view the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }



    // handle the action bar events
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.createTask:
                //create a new list
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
                Log.i("log msg", "creating new task");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}