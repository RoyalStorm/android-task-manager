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
import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.fragment.ui.DatePickerFragment;
import com.github.royalstorm.android_task_manager.fragment.ui.TimePickerFragment;
import com.github.royalstorm.android_task_manager.service.EventService;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private EventService eventService = new EventService();

    private EventInstance eventInstance;
    private Event event;
    private EventPattern eventPattern;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

    private GregorianCalendar begin;
    private GregorianCalendar end;

    private Task task;

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

    private DialogFragment picker;

    private boolean IS_BEGIN_DATE = true;
    private boolean IS_BEGIN_TIME = true;

    private View.OnClickListener dateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.task_begin_date:
                    IS_BEGIN_DATE = true;
                    break;
                case R.id.task_end_date:
                    IS_BEGIN_DATE = false;
                    break;
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable(Task.class.getSimpleName(), task);
            bundle.putSerializable(EventInstance.class.getSimpleName(), eventInstance);
            bundle.putBoolean("IS_BEGIN_DATE", IS_BEGIN_DATE);
            picker = new DatePickerFragment();
            picker.setArguments(bundle);
            picker.show(getSupportFragmentManager(), "Date picker");
        }
    };

    private View.OnClickListener timeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.task_begin_time:
                    IS_BEGIN_TIME = true;
                    break;
                case R.id.task_end_time:
                    IS_BEGIN_TIME = false;
                    break;
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable(Task.class.getSimpleName(), task);
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

        ButterKnife.bind(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Редактирование события");

        Bundle bundle = getIntent().getExtras();
        eventInstance = (EventInstance) bundle.getSerializable(EventInstance.class.getSimpleName());
        event = new Event();

        initFields(eventInstance);
        setListeners();
    }

    private void initFields(EventInstance eventInstance) {
        retrofitClient.getEventRepository().getEventsById(new Long[]{eventInstance.getEventId()}).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        taskName.setText(response.body().getData()[0].getName());
                        taskDetails.setText(response.body().getData()[0].getDetails());

                        begin = new GregorianCalendar();
                        begin.setTimeInMillis(eventInstance.getStartedAt());
                        end = new GregorianCalendar();
                        end.setTimeInMillis(eventInstance.getEndedAt());

                        taskBeginDate.setText(simpleDateFormat.format(begin.getTime()));
                        taskEndDate.setText(simpleDateFormat.format(end.getTime()));

                        taskBeginTime.setText(getTimeFormat(begin.getTime().getHours(), begin.getTime().getMinutes()));
                        taskEndTime.setText(getTimeFormat(end.getTime().getHours(), end.getTime().getMinutes()));
                    }
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }

    private void deleteTask(Long id) {
        eventService.delete(id);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void editTask() {
        if (taskName.getText().toString().trim().isEmpty()) {
            Snackbar.make(getWindow().getDecorView().
                    getRootView(), "Заголовок не может быть пустым", Snackbar.LENGTH_LONG).show();
            return;
        } else {
            if (begin.after(end)) {
                Snackbar.make(getWindow().getDecorView().
                        getRootView(), "Событие не может завершиться раньше, чем начаться", Snackbar.LENGTH_LONG).show();
                return;
            }
        }

        event.setName(taskName.getText().toString().trim());
        event.setDetails(taskDetails.getText().toString().trim());
        eventService.update(eventInstance.getEventId(), event);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setListeners() {
        taskBeginDate.setOnClickListener(dateListener);
        taskEndDate.setOnClickListener(dateListener);
        taskBeginTime.setOnClickListener(timeListener);
        taskEndTime.setOnClickListener(timeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_event:
                editTask();
                return true;

            case R.id.delete_event:
                deleteTask(eventInstance.getEventId());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (IS_BEGIN_DATE) {
            task.setBeginYear(year);
            task.setBeginMonth(month);
            task.setBeginDay(dayOfMonth);

            begin.set(Calendar.YEAR, year);
            begin.set(Calendar.MONTH, month);
            begin.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String date = simpleDateFormat.format(begin.getTime());

            taskBeginDate.setText(date);
        } else {
            task.setEndYear(year);
            task.setEndMonth(month);
            task.setEndDay(dayOfMonth);

            end.set(Calendar.YEAR, year);
            end.set(Calendar.MONTH, month);
            end.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String date = simpleDateFormat.format(end.getTime());

            taskEndDate.setText(date);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (IS_BEGIN_TIME) {
            begin.set(Calendar.HOUR, hourOfDay);
            begin.set(Calendar.MINUTE, minute);

            task.setBeginHour(hourOfDay);
            task.setBeginMinute(minute);

            taskBeginTime.setText(getTimeFormat(hourOfDay, minute));
        } else {
            end.set(Calendar.HOUR, hourOfDay);
            end.set(Calendar.MINUTE, minute);

            task.setEndHour(hourOfDay);
            task.setEndMinute(minute);

            taskEndTime.setText(getTimeFormat(hourOfDay, minute));
        }
    }

    private String getTimeFormat(int hourOfDay, int minute) {
        return hourOfDay + ":" + (minute < 10 ? ("0" + minute) : minute);
    }
}
