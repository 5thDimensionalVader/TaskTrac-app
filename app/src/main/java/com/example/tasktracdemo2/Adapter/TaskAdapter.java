package com.example.tasktracdemo2.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tasktracdemo2.BaseActivity;
import com.example.tasktracdemo2.Class.AddNewTask;
import com.example.tasktracdemo2.Class.Task;
import com.example.tasktracdemo2.HelperAndHandler.DBHandler;
import com.example.tasktracdemo2.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    //variables to be used in the class
    private List<Task> taskList = new ArrayList<>();
    private BaseActivity baseActivity;
    private DBHandler db;

    //create a constructor for the Adapter
    public TaskAdapter(DBHandler db, BaseActivity activity){
        this.db = db;
        this.baseActivity = activity;
    }

    //onCreateViewHolder method
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task_layout,parent,false);
        return new ViewHolder(itemView);
    }
    //onBindViewHolder method
    public void onBindViewHolder(ViewHolder holder, int post){
        db.openDatabase();
        final Task item = taskList.get(post);
        holder.ch_task.setText(item.getTaskName());
        holder.ch_task.setChecked(toBool(item.getStatus()));
        holder.ch_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateTaskStatus(item.getId(), 1);
                } else {
                    db.updateTaskStatus(item.getId(), 0);
                }
            }
        });
    }
    //return the size of the taskList
    public int getItemCount(){
        return taskList.size();
    }

    //helper method for the boolean values conversion
    private boolean toBool(int x){
        return x!=0;
    }

    public Context getContext() {
        return baseActivity;
    }

    //setTasks method to help set the task in BaseActivity
    public void setTasks(List<Task> list)
    {
        this.taskList = list;
        notifyDataSetChanged();
    }

    //method to edit items in the array list
    public void editItem(int post){
        Task item = taskList.get(post);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task", item.getTaskName());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(baseActivity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    //method to delete items from the array list
    public void deleteItem(int post){
        Task item = taskList.get(post);
        db.deleteTask(item.getId()); // delete the item from the id or index
        taskList.remove(post); // remove the item from its former position
        notifyItemRemoved(post); // automatically update the RecyclerView when an item is removed
    }

    //define the ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox ch_task;

        //create a constructor for the class
        ViewHolder(View v){
            super(v);
            ch_task = v.findViewById(R.id.chBox);
        }
    }
}
