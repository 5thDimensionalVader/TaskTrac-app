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

import com.example.tasktracdemo2.Adapter.ExamsTaskAdapter;
import com.example.tasktracdemo2.Adapter.TaskAdapter;
import com.example.tasktracdemo2.Class.AddNewTask;
import com.example.tasktracdemo2.Class.Task;
import com.example.tasktracdemo2.HelperAndHandler.DBHandler;
import com.example.tasktracdemo2.HelperAndHandler.RecyclerItemTouchHelper;
import com.example.tasktracdemo2.HelperAndHandler.RecyclerItemTouchHelperExams;
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

public class ExamsActivity extends AppCompatActivity implements DialogCloseListener {

    //global variables
    private RecyclerView examsRView; //global variable for the recycler view
    private ExamsTaskAdapter examsAdapter;
    public static List<Task> ExamsTaskList; // list to save the Task object and it is made public
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        //set ActionBar title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("TaskTrac");
        actionBar.setDisplayShowHomeEnabled(true);

        //instantiate the db and taskList variables
        db = new DBHandler(this);
        db.openDatabase();

        ExamsTaskList = new ArrayList<>();

        //Instantiate the RecycleView variable created
        examsRView = findViewById(R.id.examsTaskRecyclerView);
        examsRView.setHasFixedSize(true);
        examsRView.setLayoutManager(new LinearLayoutManager(this));

        //Instantiate the taskAdapter
        examsAdapter = new ExamsTaskAdapter(db, this);
        examsRView.setAdapter(examsAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelperExams(examsAdapter));
        itemTouchHelper.attachToRecyclerView(examsRView);

        ExamsTaskList = db.getAllTask();
        Collections.reverse(ExamsTaskList);
        examsAdapter.setTasks(ExamsTaskList);

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        ExamsTaskList = db.getAllTask();
        Collections.reverse(ExamsTaskList); //reverse the task list
        examsAdapter.setTasks(ExamsTaskList);
        //BaseActivity.allTaskList.addAll(ExamsTaskList); // add the task to the allTaskList
        examsAdapter.notifyDataSetChanged();
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
                //create a new task
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
                Log.i("log msg", "creating new task");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}