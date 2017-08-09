package com.example.gkudva.to_do_list.view.activities;

import android.animation.Animator;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.gkudva.to_do_list.R;
import com.example.gkudva.to_do_list.model.Info;
import com.example.gkudva.to_do_list.service.NotifyReceiver;
import com.example.gkudva.to_do_list.utils.sqlite.SQLiteHelper;
import com.example.gkudva.to_do_list.view.adapters.RVListAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {
    FloatingActionButton addTask;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Info> tdd = new ArrayList<>();
    SQLiteHelper mysqlite;
    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout mContainerLayout;
    LinearLayout mUserDateSpinnerContainingLinearLayout;
    SwitchCompat mToDoDateSwitch;
    TextView mReminderTextView;
    Date mUserReminderDate;
    boolean mUserHasReminder;
    EditText mDateEditText;
    EditText mTimeEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            //handle
        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_s);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        addTask = (FloatingActionButton) findViewById(R.id.imageButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        adapter = new RVListAdapter(tdd, getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent), getResources().getColor(R.color.divider));
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                updateCardView();
            }
        });
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_NoActionBar_TranslucentDecor);
                dialog.setContentView(R.layout.custom_dailog);
                dialog.show();

                mContainerLayout = (LinearLayout)dialog.findViewById(R.id.todoReminderAndDateContainerLayout);
                mUserDateSpinnerContainingLinearLayout = (LinearLayout)dialog.findViewById(R.id.toDoEnterDateLinearLayout);
                mToDoDateSwitch = (SwitchCompat)dialog.findViewById(R.id.toDoHasDateSwitchCompat);
                mReminderTextView = (TextView)dialog.findViewById(R.id.newToDoDateTimeReminderTextView);
                Button save = (Button) dialog.findViewById(R.id.btn_save);
                Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                Button delete = (Button) dialog.findViewById(R.id.btn_delete);
                CheckBox cb = (CheckBox) dialog.findViewById(R.id.checkbox);
                TextView tvstatus = (TextView) dialog.findViewById(R.id.status);
                cb.setVisibility(View.GONE);
                tvstatus.setVisibility(View.GONE);

                mContainerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(v);
                    }
                });

                if(mUserHasReminder && (mUserReminderDate!=null)){

                    setReminderTextView();
                    setEnterDateLayoutVisibleWithAnimations(true);
                }
                if(mUserReminderDate==null){
                    mToDoDateSwitch.setChecked(false);
                    mReminderTextView.setVisibility(View.INVISIBLE);
                }

                setEnterDateLayoutVisible(mToDoDateSwitch.isChecked());

                mToDoDateSwitch.setChecked(mUserHasReminder && (mUserReminderDate != null));
                mToDoDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (!isChecked) {
                            mUserReminderDate = null;
                        }
                        mUserHasReminder = isChecked;
                        setEnterDateLayoutVisibleWithAnimations(isChecked);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(buttonView.getWindowToken(), 0);
                    }
                });

                mDateEditText = (EditText)dialog.findViewById(R.id.newTodoDateEditText);
                mTimeEditText = (EditText)dialog.findViewById(R.id.newTodoTimeEditText);

                mDateEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Date date;
                        hideKeyboard(v);

                        date = new Date();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(MainActivity.this, year, month, day);

                        datePickerDialog.show(getFragmentManager(), "DateFragment");

                    }
                });


                mTimeEditText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Date date;
                        hideKeyboard(v);

                        date = new Date();

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);

                        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(MainActivity.this, hour, minute, DateFormat.is24HourFormat(getApplicationContext()));

                        timePickerDialog.show(getFragmentManager(), "TimeFragment");
                    }
                });



                delete.setVisibility(View.GONE);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText todoText = (EditText) dialog.findViewById(R.id.input_task_desc);
                        EditText todoNotes = (EditText) dialog.findViewById(R.id.input_task_notes);
                        TextView todoDate = (TextView) dialog.findViewById(R.id.newToDoDateTimeReminderTextView);
                        if (todoText.getText().length() >= 2) {
                            RadioGroup proritySelection = (RadioGroup) dialog.findViewById(R.id.toDoRG);
                            String RadioSelection = new String();
                            if (proritySelection.getCheckedRadioButtonId() != -1) {
                                int id = proritySelection.getCheckedRadioButtonId();
                                View radiobutton = proritySelection.findViewById(id);
                                int radioId = proritySelection.indexOfChild(radiobutton);
                                RadioButton btn = (RadioButton) proritySelection.getChildAt(radioId);
                                RadioSelection = (String) btn.getText();
                            }

                            Log.d("Ganesh", todoDate.getText().toString().substring(17));
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("ToDoTaskDetails", todoText.getText().toString());
                            contentValues.put("ToDoTaskPrority", RadioSelection);
                            contentValues.put("ToDoTaskStatus", "Incomplete");
                            contentValues.put("ToDoNotes", todoNotes.getText().toString());
                            if (mUserHasReminder == true) {
                                contentValues.put("ToDoDate", todoDate.getText().toString().substring(17));
                            }
                            else
                            {
                                contentValues.put("ToDoDate", "");
                            }
                            int color = 0;
                            color = ColorGenerator.MATERIAL.getRandomColor();
                            contentValues.put("ToDoColor", Integer.toString(color));
                            mysqlite = new SQLiteHelper(getApplicationContext());
                            Boolean b = mysqlite.insertInto(contentValues);
                            if (b) {
                                dialog.hide();
                                updateCardView();
                            } else {
                                Toast.makeText(getApplicationContext(), "Some thing went wrong", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter To Do Task", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }
        });

    }
    public void scheduleNotification(long time, String TaskTitle, String TaskPrority) {
        Calendar Calendar_Object = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final int _id = (int) System.currentTimeMillis();
        Intent myIntent = new Intent(MainActivity.this, NotifyReceiver.class);
        myIntent.putExtra("TaskTitle", TaskTitle);
        myIntent.putExtra("TaskPrority",TaskPrority);
        myIntent.putExtra("id",_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                _id, myIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC, Calendar_Object.getTimeInMillis() + time,
                pendingIntent);

    }
    public void updateCardView() {
        swipeRefreshLayout.setRefreshing(true);
        mysqlite = new SQLiteHelper(getApplicationContext());
        Cursor result = mysqlite.selectAllData();
        if (result.getCount() == 0) {
            tdd.clear();
            adapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "No Tasks", Toast.LENGTH_SHORT).show();
        } else {
            tdd.clear();
            adapter.notifyDataSetChanged();
            while (result.moveToNext()) {
                Info tddObj = new Info();
                tddObj.setToDoID(result.getInt(0));
                tddObj.setToDoTaskDetails(result.getString(1));
                tddObj.setToDoTaskPrority(result.getString(2));
                tddObj.setToDoTaskStatus(result.getString(3));
                tddObj.setToDoNotes(result.getString(4));
                tddObj.setToDoDate(result.getString(5));
                tddObj.setToDoColor(result.getString(6));
                tddObj.setToDoTaskStatus(result.getString(7));
                tdd.add(tddObj);
            }
            adapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        updateCardView();
    }

    public void setReminderTextView(){
        android.text.format.DateFormat df = new android.text.format.DateFormat();

        if(mUserReminderDate!=null){
            mReminderTextView.setVisibility(View.VISIBLE);
            if(mUserReminderDate.before(new Date())){
                //Log.d("OskarSchindler", "DATE is "+mUserReminderDate);
                mReminderTextView.setText(this.getString(R.string.date_error_check_again));
                mReminderTextView.setTextColor(Color.RED);
                return;
            }
            Date date = mUserReminderDate;
            String dateString = df.format("d MMM, yyyy", date).toString() ;
            String timeString;
            String amPmString = "";

            if(DateFormat.is24HourFormat(this)){
                timeString = df.format("k:mm", date).toString();
            }
            else{
                timeString = df.format("h:mm", date).toString();
                amPmString = df.format("a", date).toString();
            }
            String finalString = String.format(getResources().getString(R.string.remind_date_and_time), dateString, timeString, amPmString);
            mReminderTextView.setTextColor(getResources().getColor(R.color.secondary_text));
            mReminderTextView.setText(finalString);
        }
        else{
            mReminderTextView.setVisibility(View.INVISIBLE);

        }
    }

    public void setEnterDateLayoutVisibleWithAnimations(boolean checked){
        if(checked){
            setReminderTextView();
            mUserDateSpinnerContainingLinearLayout.animate().alpha(1.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    }
            );
        }
        else{
            mUserDateSpinnerContainingLinearLayout.animate().alpha(0.0f).setDuration(500).setListener(
                    new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }
            );
        }

    }

    public void setEnterDateLayoutVisible(boolean checked){
        if(checked){
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.VISIBLE);
        }
        else{
            mUserDateSpinnerContainingLinearLayout.setVisibility(View.INVISIBLE);
        }
    }

    public void hideKeyboard(View v)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void setDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        int hour, minute;


        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if(reminderCalendar.before(calendar)){
            //    Toast.makeText(this, "My time-machine is a bit rusty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mUserReminderDate!=null){
            calendar.setTime(mUserReminderDate);
        }

        if(DateFormat.is24HourFormat(this)){
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        }
        else{

            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        mUserReminderDate = calendar.getTime();
        setReminderTextView();
//        setDateAndTimeEditText();
        setDateEditText();
    }

    public void setTime(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        if(mUserReminderDate!=null){
            calendar.setTime(mUserReminderDate);
        }

//        if(DateFormat.is24HourFormat(this) && hour == 0){
//            //done for 24h time
//                hour = 24;
//        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, hour, minute, 0);
        mUserReminderDate = calendar.getTime();

        setReminderTextView();
//        setDateAndTimeEditText();
        setTimeEditText();
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        setTime(hour, minute);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        setDate(year, month, day);
    }

    public void  setDateEditText(){
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String dateFormat = "d MMM, yyyy";
        mDateEditText.setText(df.format(dateFormat, mUserReminderDate).toString());
    }

    public void  setTimeEditText(){
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String dateFormat;
        if(DateFormat.is24HourFormat(this)){
            dateFormat = "k:mm";
        }
        else{
            dateFormat = "h:mm a";

        }
        mTimeEditText.setText(df.format(dateFormat, mUserReminderDate).toString());
    }



}

