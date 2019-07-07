package com.github.royalstorm.android_task_manager.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.fragment.ui.DatePickerFragment;
import com.github.royalstorm.android_task_manager.fragment.ui.TimePickerFragment;
import com.github.royalstorm.android_task_manager.fragment.ui.dialog.SelectRepeatModeDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener, SelectRepeatModeDialog.SelectRepeatModeDialogListener {
    @BindView(R.id.task_name)
    EditText taskName;
    @BindView(R.id.task_details)
    EditText taskDetails;

    @BindView(R.id.task_begin_date)
    TextView taskBeginDate;
    @BindView(R.id.task_end_date)
    TextView taskEndDate;
    @BindView(R.id.task_begin_time)
    TextView taskBeginTime;
    @BindView(R.id.task_end_time)
    TextView taskEndTime;
    @BindView(R.id.task_repeat_mode)
    TextView eventRepeatMode;

    private Event event = new Event();
    private EventPattern eventPattern = new EventPattern();
    private EventInstance eventInstance = new EventInstance();

    private GregorianCalendar start;
    private GregorianCalendar end;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

    private DialogFragment picker;

    private boolean IS_BEGIN_DATE = true;
    private boolean IS_BEGIN_TIME = true;

    private View.OnClickListener dateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.task_begin_date:
                    IS_BEGIN_DATE = true;
                    bundle.putSerializable(GregorianCalendar.class.getSimpleName(), start);
                    break;
                case R.id.task_end_date:
                    IS_BEGIN_DATE = false;
                    bundle.putSerializable(GregorianCalendar.class.getSimpleName(), end);
                    break;
            }

            bundle.putBoolean("IS_BEGIN_DATE", IS_BEGIN_DATE);
            picker = new DatePickerFragment();
            picker.setArguments(bundle);
            picker.show(getSupportFragmentManager(), "Date picker");
        }
    };

    private View.OnClickListener timeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            switch (v.getId()) {
                case R.id.task_begin_time:
                    IS_BEGIN_TIME = true;
                    bundle.putSerializable(GregorianCalendar.class.getSimpleName(), start);
                    break;
                case R.id.task_end_time:
                    IS_BEGIN_TIME = false;
                    bundle.putSerializable(GregorianCalendar.class.getSimpleName(), end);
                    break;
            }

            bundle.putBoolean("IS_BEGIN_TIME", IS_BEGIN_TIME);
            picker = new TimePickerFragment();
            picker.setArguments(bundle);
            picker.show(getSupportFragmentManager(), "Time picker");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Новое событие");

        ButterKnife.bind(this);

        initActivity();
        setListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_event) {
            createTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (IS_BEGIN_DATE) {
            start.set(Calendar.YEAR, year);
            start.set(Calendar.MONTH, month);
            start.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            taskBeginDate.setText(simpleDateFormat.format(start.getTime()));
        } else {
            end.set(Calendar.YEAR, year);
            end.set(Calendar.MONTH, month);
            end.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            taskEndDate.setText(simpleDateFormat.format(end.getTime()));
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (IS_BEGIN_TIME) {
            start.set(Calendar.HOUR_OF_DAY, hourOfDay);
            start.set(Calendar.MINUTE, minute);

            taskBeginTime.setText(getTimeFormat(hourOfDay, minute));
        } else {
            end.set(Calendar.HOUR_OF_DAY, hourOfDay);
            end.set(Calendar.MINUTE, minute);

            taskEndTime.setText(getTimeFormat(hourOfDay, minute));
        }
    }

    @Override
    public void applyMode(String mode, String rRule, Long endedAt) {
        eventRepeatMode.setText(mode);
        eventPattern.setRrule(rRule);
        if (rRule != null)
            eventPattern.setEndedAt(endedAt);
    }

    private void initActivity() {
        Bundle bundle = getIntent().getExtras();
        eventInstance = (EventInstance) bundle.getSerializable(EventInstance.class.getSimpleName());

        start = timestampToGregorian(eventInstance.getStartedAt());
        end = timestampToGregorian(eventInstance.getEndedAt());

        start.add(Calendar.HOUR, 1);

        taskBeginDate.setText(simpleDateFormat.format(start.getTime()));
        taskBeginTime.setText(getTimeFormat(start.getTime().getHours(), 0));

        start.set(Calendar.MINUTE, 0);

        end.add(Calendar.HOUR_OF_DAY, 2);

        taskEndDate.setText(simpleDateFormat.format(end.getTime()));
        taskEndTime.setText(getTimeFormat(end.getTime().getHours(), 0));

        end.set(Calendar.MINUTE, 0);

        eventRepeatMode.setText("Не повторяется");

        //Init event pattern
        eventPattern.setStartedAt(start.getTimeInMillis());
        eventPattern.setEndedAt(end.getTimeInMillis());
        //Default never repeat
        eventPattern.setRrule(null);
        eventPattern.setTimezone(TimeZone.getDefault().getID());
        eventPattern.setDuration(end.getTimeInMillis() - start.getTimeInMillis());
    }

    private void setListeners() {
        taskBeginDate.setOnClickListener(dateListener);
        taskEndDate.setOnClickListener(dateListener);
        taskBeginTime.setOnClickListener(timeListener);
        taskEndTime.setOnClickListener(timeListener);
        eventRepeatMode.setOnClickListener(v -> {
            SelectRepeatModeDialog selectRepeatModeDialog = new SelectRepeatModeDialog();

            Bundle bundle = new Bundle();
            bundle.putSerializable(EventPattern.class.getSimpleName(), eventPattern);

            selectRepeatModeDialog.setArguments(bundle);
            selectRepeatModeDialog.show(getSupportFragmentManager(), "Select repeat mode dialog");
        });
    }

    private void createTask() {
        if (taskName.getText().toString().trim().isEmpty()) {
            Snackbar.make(getWindow().getDecorView().
                    getRootView(), "Заголовок не может быть пустым", Snackbar.LENGTH_LONG).show();
            return;
        } else {
            if (start.after(end)) {
                Snackbar.make(getWindow().getDecorView().
                        getRootView(), "Событие не может завершиться раньше, чем начаться", Snackbar.LENGTH_LONG).show();
                return;
            }

            eventPattern.setStartedAt(start.getTimeInMillis());
            //If selected never repeat
            if (eventPattern.getRrule() == null)
                eventPattern.setEndedAt(end.getTimeInMillis());
            eventPattern.setTimezone(TimeZone.getDefault().getID());
            eventPattern.setDuration(end.getTimeInMillis() - start.getTimeInMillis());
        }

        event.setDetails(taskDetails.getText().toString().trim());
        event.setName(taskName.getText().toString().trim());

        Intent intent = new Intent();
        intent.putExtra(Event.class.getSimpleName(), event);
        intent.putExtra(EventPattern.class.getSimpleName(), eventPattern);
        setResult(RESULT_OK, intent);
        finish();
    }

    private String getTimeFormat(int hourOfDay, int minute) {
        return hourOfDay + ":" + (minute < 10 ? ("0" + minute) : minute);
    }

    private GregorianCalendar timestampToGregorian(Long millis) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(millis);

        return gregorianCalendar;
    }
}
