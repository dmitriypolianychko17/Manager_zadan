package com.dmytro.manager_zadan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dmytro.manager_zadan.adapter.Zadania;
import com.dmytro.manager_zadan.db.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class TaskEditActivity extends AppCompatActivity {

    private TextInputEditText time, date, task;
    private TextInputLayout taskLayout, timeLayout;
    private Calendar calendar;
    private DatabaseHandler db;
    private String origin, editDate, editTime, editTaskDesc, editColor;
    int editAction, editId;
    private ImageView ivRemind, ivRepeat;
    private TextView tvRemind, tvRepeat;
    private Switch swRemind, swRepeat;
    private View view;
    private String[] color = {"#ffcdd2", "#f8bbd0", "#e1bee7", "#d1c4e9", "#c5cae9", "#bbdefb", "#b3e5fc", "#b2ebf2", "#b2dfdb", "#c8e6c9", "#dcedc8", "#f0f4c3", "#fff9c4", "#ffecb3", "#ffe0b2", "#ffccbc"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);

        view = findViewById(R.id.rootView);
        origin = getIntent().getExtras().getString("origin");
        calendar = Calendar.getInstance();
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvRepeat = (TextView)findViewById(R.id.tv_repeater);
        tvRemind = (TextView)findViewById(R.id.tv_remimder);
        ivRepeat = (ImageView)findViewById(R.id.iv_repeat);
        ivRemind = (ImageView)findViewById(R.id.iv_remind) ;
        swRemind = (Switch)findViewById(R.id.switch_remind);
        swRepeat = (Switch)findViewById(R.id.switch_repeat);

        timeLayout = (TextInputLayout)findViewById(R.id.textInputLayoutTime);
        taskLayout = (TextInputLayout)findViewById(R.id.textInputLayoutTask);
        task = (TextInputEditText)findViewById(R.id.task);
        date = (TextInputEditText)findViewById(R.id.date);
        time = (TextInputEditText)findViewById(R.id.time);

        if (origin.equals("forEdit")){
            editDate = getIntent().getExtras().getString("editDate");
            editTime = getIntent().getExtras().getString("editTime");
            editTaskDesc = getIntent().getExtras().getString("editTaskDesc");
            editColor = getIntent().getExtras().getString("editColor");
            editAction = getIntent().getExtras().getInt("editAction");
            editId = getIntent().getExtras().getInt("editId");
            date.setText(editDate);
            time.setText(editTime);
            task.setText(editTaskDesc);
        }
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dates = year + "-" + (month + 1) + "-" + dayOfMonth;
                        date.setText(dates);
                    }

                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        time.setText(hourOfDay + ":" + minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        swRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swRemind.isChecked()){
                    ivRemind.setImageResource(R.drawable.ic_alarm_on_teal_24dp);
                }
                if (!swRemind.isChecked()){
                    ivRemind.setImageResource(R.drawable.ic_alarm_add_teal_24dp);
                }
            }
        });

        swRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swRepeat.isChecked()){
                    openSetRepeaterDialog();
                }
                if (!swRepeat.isChecked()){
                    tvRepeat.setText("Powtórz");
                    ivRepeat.setImageResource(R.drawable.ic_repeat_lite_24dp);
                }
            }
        });
    }

    private void  openSetRepeaterDialog(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
        final LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_f_repeater, null);
        Button button_dialog_bottom_sheet_ok = (Button) dialogView.findViewById(R.id.btn_dialog_bottom_sheet_ok);
        Button button_dialog_bottom_sheet_cancel = (Button) dialogView.findViewById(R.id.btn_dialog_bottom_sheet_cancel);
        final Spinner spinner = (Spinner)dialogView.findViewById(R.id.spinner_repeat);
        bottomSheetDialog.setContentView(dialogView);

        button_dialog_bottom_sheet_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvRepeat.setText(spinner.getSelectedItem().toString());
                ivRepeat.setImageResource(R.drawable.ic_repeat_teal_24dp);
                bottomSheetDialog.dismiss();
            }
        });
        button_dialog_bottom_sheet_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvRepeat.setText("Powtórz");
                ivRepeat.setImageResource(R.drawable.ic_repeat_lite_24dp);
                swRepeat.setChecked(false);
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
    }
    //funkcja sprawdzania poprawnego formatu czasu
    private String properDateTime (String date, String time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date formated = null;
        String unformated = date + " " +time+":00";
        try {
            formated = dateFormat.parse(unformated);
        } catch (ParseException e){
            e.printStackTrace();
        }
        return  dateFormat.format(formated);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:finish();
            return true;
            case R.id.action_done:
                if(validateTask()){
                    if (validateTime()){
                        String newDate;
                        if (date.getText().toString().trim().equals("Dzisiaj")){
                            newDate = getDateTime();
                        } else {
                            newDate = date.getText().toString().trim();
                        }
                        if (origin.equals("forEdit")){
                            if (editDate.equals(date.getText().toString().trim()) && editTime.equals(time.getText().toString().trim()) && editTaskDesc.equals(task.getText().toString().trim())){
                                finish();
                                Toast.makeText(getApplicationContext(), "Nic do zmian", Toast.LENGTH_SHORT).show();
                            } else {
                                Zadania zadania = new Zadania(properDateTime(newDate, time.getText().toString().trim()), task.getText().toString().trim(), editColor, editAction);
                                db = new DatabaseHandler(getApplicationContext());
                                db.updateTask(zadania, editId);
                                db.closeDB();
                                finish();
                                Toast.makeText(getApplicationContext(), "Pomyślnie aktualizowano", Toast.LENGTH_SHORT).show();
                            }
                        } else if (origin.equals("forNew")){
                            //Generujemy numer od 0 do 16
                            Random random = new Random();
                            int index = random.nextInt(16);
                            Zadania zadania = new Zadania(properDateTime(newDate, time.getText().toString().trim()), task.getText().toString().trim(), color[index], 0);
                            db = new DatabaseHandler(getApplicationContext());
                            long zadanie_id = db.addTask(zadania);
                            db.closeDB();
                            finish();
                        }
                    }
                }
                return true;
            case  R.id.action_delete:
                //Usuwamy zadanie
                if (origin.equals("forEdit")){
                    db = new DatabaseHandler(getApplicationContext());
                    db.deleteTask(editId);
                    db.closeDB();
                    finish();
                    Toast.makeText(getApplicationContext(), "Usunięto", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Co mam usunąć? Jeszcze nic nie dodaleś", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    private boolean validateTask(){
        if (task.getText().toString().trim().isEmpty()){
            taskLayout.setError("Żartujesz?");
            return false;
        } else {
            taskLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateTime(){
        if (time.getText().toString().isEmpty()){
            timeLayout.setError("Prosze podać poprawny czas");
            return false;
        } else {
            timeLayout.setErrorEnabled(false);
        }
        return true;
    }

    //pobieramy czas i date
    private String getDateTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}