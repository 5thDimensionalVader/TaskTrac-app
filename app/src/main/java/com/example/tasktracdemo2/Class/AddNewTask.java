package com.example.tasktracdemo2.Class;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.tasktracdemo2.HelperAndHandler.AlertReceiverHandler;
import com.example.tasktracdemo2.HelperAndHandler.DBHandler;
import com.example.tasktracdemo2.Interfaces.DialogCloseListener;
import com.example.tasktracdemo2.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static androidx.core.content.ContextCompat.getSystemService;

//define global variables
    public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText edtTaskName;
    private Button btnCreateTask;
    private ImageView btnDatePicker;
    private ImageView btnTimePicker;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private String date, time;

    //define the db variable
    private DBHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(STYLE_NORMAL, R.style.DialogStyle);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.new_task, container, false);
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            return view;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            edtTaskName = getView().findViewById(R.id.edtTaskName); //taskname
            btnCreateTask = getView().findViewById(R.id.btnCreateTask); // button to create the task
            btnDatePicker = getView().findViewById(R.id.date); // date picker
            btnTimePicker = getView().findViewById(R.id.time); // time picker

            db = new DBHandler(getActivity());
            db.openDatabase();

            boolean isUpdate = false;
            final Bundle bundle = getArguments();
            if(bundle != null){
                isUpdate = true;
                String task = bundle.getString("task");
                edtTaskName.setText(task);
                assert task !=null;
                if (task.length()>0){
                    btnCreateTask.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.purple_500));
                }
            }

            edtTaskName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.toString().equals("")){
                            btnCreateTask.setEnabled(false);
                            btnCreateTask.setTextColor(Color.GRAY);
                        } else {
                            btnCreateTask.setEnabled(true);
                            btnCreateTask.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.purple_500));
                        }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            //button to select the date
           btnDatePicker.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //get the current date
                   Calendar c = Calendar.getInstance();
                   int year = c.get(Calendar.YEAR);     //get the year
                   int month = c.get(Calendar.MONTH);   //get the month
                   int day = c.get(Calendar.DAY_OF_MONTH); //get the day of the month

                   //create the DatePickerDialog object
                   DatePickerDialog dialog = new DatePickerDialog(
                           getActivity(),
                           mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                               @Override
                               public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                   month = month + 1;
                                   Log.d(TAG, "setDate: "+day+"-"+month+"-"+year);
                                   date = day + "/" + month + "/"+ year; // store the selected date
                                   btnDatePicker.setImageResource(R.drawable.calendar_selected);
                               }
                           },
                           year,month,day);
                   dialog.show();
               }
           });
            //button to select the time
           btnTimePicker.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    //initialize time picker dialog
                   TimePickerDialog dialog = new TimePickerDialog(
                           getActivity(),
                           mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                               @Override
                               public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                                   //initialize the calendar
                                   Calendar c = Calendar.getInstance();
                                   //set the hour and minutes
                                   c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                   c.set(Calendar.MINUTE, minutes);
                                   c.set(Calendar.SECOND, 0);
                                    startAlarm(c);
                                   time = (String) DateFormat.format("hh:mm aa", c);
                                   btnTimePicker.setImageResource(R.drawable.time_selected);
                               }
                           },12, 0, false
                   );
                   //show the time dialog
                   dialog.show();
               }
               private void startAlarm(Calendar c){
                   //set up alarm manager
                   AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                   Intent intent = new Intent(getActivity(), AlertReceiverHandler.class);
                   PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, 0);
                   alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                   if (c.before(Calendar.getInstance())) {
                       c.add(Calendar.DATE, 1);
                   }
               }

           });

            final boolean finalIsUpdate = isUpdate;
            btnCreateTask.setOnClickListener(v -> {
                String text = edtTaskName.getText().toString();
                if(finalIsUpdate){
                    db.updateTaskName(bundle.getInt("id"), text);
                } else {
                    Task task = new Task();
                    task.setTaskName(text);//set the task name
                    task.setStatus(0);//set the default status, which is unchecked
                    task.setDate(date);//set the due date
                    task.setTime(time);//set the reminder time
                    db.insertTask(task);//insert the task into db
                }
                dismiss();
            });
        }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
