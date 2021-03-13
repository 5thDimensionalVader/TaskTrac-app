package com.example.tasktracdemo2.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tasktracdemo2.Activity.CourseworkActivity;
import com.example.tasktracdemo2.Activity.TestActivity;
import com.example.tasktracdemo2.Class.AddNewTask;
import com.example.tasktracdemo2.Class.Task;
import com.example.tasktracdemo2.HelperAndHandler.DBHandler;
import com.example.tasktracdemo2.R;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder>{
    //variables to be used in the class
    private List<Task> taskList = new ArrayList<>();
    private TestActivity testActivity;
    private DBHandler db;


    //create a constructor for the examsActivity
    public TestAdapter(DBHandler db, TestActivity activity){
        this.db = db;
        this.testActivity = activity;
    }
    //onCreateViewHolder method
    public TestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_task_layout,parent,false);
        return new ViewHolder(itemView);
    }


    //onBindViewHolder method
    public void onBindViewHolder(TestAdapter.ViewHolder holder, int post){
        db.openDatabase();
        final Task item = taskList.get(post);
        holder.txt_date.setText(item.getDate());
        holder.txt_time.setText(item.getTime());
        holder.txt_task_name.setText(item.getTaskName());
        holder.ch_task.setChecked(toBool(item.getStatus()));
        holder.ch_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.txt_task_name.setPaintFlags(holder.txt_task_name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.txt_task_name.setTextColor(0xff888888);
                    db.updateTaskStatus(item.getId(), 1);
                } else {
                    holder.txt_task_name.setPaintFlags(holder.txt_task_name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.txt_task_name.setTextColor(0xff000000);
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

    public Context getContextTest() {
        return testActivity;
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
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(testActivity.getSupportFragmentManager(), AddNewTask.TAG);
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
        TextView txt_date;
        TextView txt_time;
        TextView txt_task_name;

        //create a constructor for the class
        ViewHolder(View v){
            super(v);
            ch_task = v.findViewById(R.id.chBox);
            txt_date = v.findViewById(R.id.dueDate);
            txt_time = v.findViewById(R.id.dueTime);
            txt_task_name = v.findViewById(R.id.txt_task_name);
        }
    }
}
