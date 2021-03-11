package com.example.tasktracdemo2.HelperAndHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Telephony;

import com.example.tasktracdemo2.Class.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    //define global variables
    private static final int VERSION = 3; //version of the database
    private static final String NAME = "taskTracDB"; //name of the database
    private static final String TASKTRAC_TABLE = "todo"; //name of the table
    private static final String ID = "id"; //table columnName
    private static final String TASK = "taskName"; //table taskName
    private static final String STATUS = "status"; // table status
    private static final String DATE = "date"; // table date
    private static final String TIME = "time"; //table time
    private static final String QUERY = "CREATE TABLE " + TASKTRAC_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER, " + DATE + " TEXT, " + TIME +" TEXT)"; //query to create the new table
    private SQLiteDatabase db; // reference of the SQLite DB used.

    public DBHandler(Context context){super(context, NAME, null, VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(QUERY);}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + TASKTRAC_TABLE);
        //Create tables again
        onCreate(db);
    }
    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    //create a method to insert the task into the db
    public void insertTask(Task task){
        ContentValues conVal = new ContentValues();
        conVal.put(TASK, task.getTaskName()); //get the name of the task.
        conVal.put(STATUS, 0); //get the status of the task.
        conVal.put(DATE, task.getDate());//get the due date of the task.
        conVal.put(TIME, task.getTime());//get the reminder time of the task.
        db.insert(TASKTRAC_TABLE, null, conVal); //insert data into the table in db
    }
    //create a method to get all task from db, save to List to display in the RecyclerView
    public List<Task> getAllTask(){
        List<Task> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TASKTRAC_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        Task task = new Task();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTaskName(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setTime(cur.getString(cur.getColumnIndex(TIME)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    //create a method to update the status of the task
    public void updateTaskStatus(int id, int status){
        ContentValues conVal = new ContentValues();
        conVal.put(STATUS, status);
        db.update(TASKTRAC_TABLE, conVal, ID + "=?", new String[] {String.valueOf(id)});
    }

    //create method to update the task name
    public void updateTaskName(int id, String taskName){
        ContentValues conVal = new ContentValues();
        conVal.put(TASK, taskName);
        db.update(TASKTRAC_TABLE, conVal, ID + "=?", new String[] {String.valueOf(id)});
    }

    //create method to update the task due date
    public void updateDate(int id, String date){
        ContentValues conVal = new ContentValues();
        conVal.put(DATE, date);
        db.update(TASKTRAC_TABLE, conVal, ID + "=?", new String[] {String.valueOf(id)});
    }

    //create method to update the task reminder time
    public void updateTime(int id, String time){
        ContentValues conVal = new ContentValues();
        conVal.put(DATE, time);
        db.update(TASKTRAC_TABLE, conVal, ID + "=?", new String[] {String.valueOf(id)});
    }

    //create method to delete tasks
    public void deleteTask(int id){
        db.delete(TASKTRAC_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }
    //finish
}
