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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.dto.EventPatternResponse;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.fragment.dialog.AccessConfigurationDialog;
import com.github.royalstorm.android_task_manager.fragment.dialog.SelectRepeatModeDialog;
import com.github.royalstorm.android_task_manager.fragment.picker.DatePickerFragment;
import com.github.royalstorm.android_task_manager.fragment.picker.TimePickerFragment;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.royalstorm.android_task_manager.shared.Frequency.DAILY;
import static com.github.royalstorm.android_task_manager.shared.Frequency.MONTHLY;
import static com.github.royalstorm.android_task_manager.shared.Frequency.NEVER;
import static com.github.royalstorm.android_task_manager.shared.Frequency.WEEKLY;
import static com.github.royalstorm.android_task_manager.shared.Frequency.YEARLY;

public class EditEventActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        SelectRepeatModeDialog.SelectRepeatModeDialogListener,
        AccessConfigurationDialog.ApplyAccessConfiguration {

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
    @BindView(R.id.time_zone_spinner)
    Spinner timeZoneSpinner;
    @BindView(R.id.task_repeat_mode)
    TextView eventRepeatMode;
    @BindView(R.id.generate_sharing_link)
    TextView generateSharingLink;

    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private FirebaseAuth firebaseAuth;
    private String userToken;

    private EventInstance eventInstance;
    private Event event;
    private EventPattern eventPattern;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault());

    private GregorianCalendar start;
    private GregorianCalendar end;

    private String[] timeZones;

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
        eventPattern = new EventPattern();
        timeZones = TimeZone.getAvailableIDs();

        firebaseAuth = FirebaseAuth.getInstance();
        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();

        initFields(eventInstance);
        setListeners();
    }

    private void initFields(EventInstance eventInstance) {
        timeZoneSpinner.setAdapter(
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        timeZones
                )
        );

        updateToken();
        initialRequest(eventInstance, userToken);
    }

    private void deleteEvent(Long id) {
        updateToken();
        deleteRequest(id, userToken);
    }

    private void updateEvent() {
        if (taskName.getText().toString().trim().isEmpty()) {
            showSnackBar("Заголовок не может быть пустым");
            return;
        } else {
            if (start.after(end)) {
                showSnackBar("Событие не может завершиться раньше, чем начаться");
                return;
            }

            eventPattern.setStartedAt(start.getTimeInMillis());
            //If selected never repeat
            if (eventPattern.getRrule() == null)
                eventPattern.setEndedAt(end.getTimeInMillis());
            eventPattern.setTimezone(timeZoneSpinner.getSelectedItem().toString());
            eventPattern.setDuration(end.getTimeInMillis() - start.getTimeInMillis());
        }

        event.setName(taskName.getText().toString().trim());
        event.setDetails(taskDetails.getText().toString().trim());

        updateToken();
        updateRequest(eventInstance.getPatternId(), eventInstance.getEventId(), userToken);
    }

    private void setListeners() {
        taskBeginDate.setOnClickListener(dateListener);
        taskEndDate.setOnClickListener(dateListener);
        taskBeginTime.setOnClickListener(timeListener);
        taskEndTime.setOnClickListener(timeListener);

        eventRepeatMode.setOnClickListener(v -> {
            SelectRepeatModeDialog selectRepeatModeDialog = new SelectRepeatModeDialog();

            Bundle eventPatternBundle = new Bundle();
            eventPatternBundle.putSerializable(EventPattern.class.getSimpleName(), eventPattern);

            selectRepeatModeDialog.setArguments(eventPatternBundle);
            selectRepeatModeDialog.show(getSupportFragmentManager(), "Select repeat mode dialog");
        });

        generateSharingLink.setOnClickListener(v -> {
            AccessConfigurationDialog accessConfigurationDialog = new AccessConfigurationDialog();

            Bundle accessConfigurationBundle = new Bundle();
            accessConfigurationBundle.putSerializable(EventInstance.class.getSimpleName(), eventInstance);

            accessConfigurationDialog.setArguments(accessConfigurationBundle);
            accessConfigurationDialog.show(getSupportFragmentManager(), "Access config dialog");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_event_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_event:
                updateEvent();
                return true;

            case R.id.delete_event:
                deleteEvent(eventInstance.getEventId());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (IS_BEGIN_DATE) {
            start.set(Calendar.YEAR, year);
            start.set(Calendar.MONTH, month);
            start.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String date = simpleDateFormat.format(start.getTime());

            taskBeginDate.setText(date);
        } else {
            end.set(Calendar.YEAR, year);
            end.set(Calendar.MONTH, month);
            end.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String date = simpleDateFormat.format(end.getTime());

            taskEndDate.setText(date);
        }

        recountStartAndEnd();
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

        recountStartAndEnd();
    }

    @Override
    public void applySharingLink(Intent sharingIntent) {
        startActivity(Intent.createChooser(sharingIntent, "Выберите пользователя"));
    }

    @Override
    public void applyMode(String mode, String rRule, Long endedAt) {
        eventRepeatMode.setText(mode);
        eventPattern.setRrule(rRule);
        if (rRule != null)
            eventPattern.setEndedAt(endedAt);
    }

    private void updateRequest(Long patternId, Long eventId, String userToken) {
        retrofitClient.getPatternsRepository().update(patternId, eventPattern, userToken).enqueue(new Callback<EventPatternResponse>() {
            @Override
            public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
                if (response.isSuccessful())
                    retrofitClient.getEventsRepository().update(eventId, event, userToken).enqueue(new Callback<EventResponse>() {
                        @Override
                        public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                            if (response.isSuccessful()) {
                                setResult(RESULT_OK, new Intent());
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<EventResponse> call, Throwable t) {

                        }
                    });
            }

            @Override
            public void onFailure(Call<EventPatternResponse> call, Throwable t) {

            }
        });
    }

    private void deleteRequest(Long id, String userToken) {
        retrofitClient.getEventsRepository().delete(id, userToken).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    setResult(RESULT_OK, new Intent());
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
            }
        });
    }

    private void initialRequest(EventInstance eventInstance, String userToken) {
        retrofitClient.getEventsRepository().getEventsById(new Long[]{eventInstance.getEventId()}, userToken).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    taskName.setText(response.body().getData()[0].getName());
                    taskDetails.setText(response.body().getData()[0].getDetails());

                    start = new GregorianCalendar();
                    start.setTimeInMillis(eventInstance.getStartedAt());
                    end = new GregorianCalendar();
                    end.setTimeInMillis(eventInstance.getEndedAt());

                    taskBeginDate.setText(simpleDateFormat.format(start.getTime()));
                    taskEndDate.setText(simpleDateFormat.format(end.getTime()));

                    taskBeginTime.setText(getTimeFormat(start.getTime().getHours(), start.getTime().getMinutes()));
                    taskEndTime.setText(getTimeFormat(end.getTime().getHours(), end.getTime().getMinutes()));

                    retrofitClient.getPatternsRepository().getPatternsById(eventInstance.getPatternId(), userToken).enqueue(new Callback<EventPatternResponse>() {
                        @Override
                        public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                eventPattern = response.body().getData()[0];

                                for (int i = 0; i < timeZones.length; i++)
                                    if (timeZones[i].equals(eventPattern.getTimezone())) {
                                        timeZoneSpinner.setSelection(i);
                                        break;
                                    }

                                if (eventPattern.getRrule() == NEVER)
                                    eventRepeatMode.setText(R.string.no_repeat);
                                else
                                    switch (eventPattern.getRrule()) {
                                        case DAILY:
                                            eventRepeatMode.setText(R.string.daily);
                                            break;
                                        case WEEKLY:
                                            eventRepeatMode.setText(R.string.weekly);
                                            break;
                                        case MONTHLY:
                                            eventRepeatMode.setText(R.string.monthly);
                                            break;
                                        case YEARLY:
                                            eventRepeatMode.setText(R.string.yearly);
                                            break;
                                        default:
                                            eventRepeatMode.setText(R.string.other);
                                    }
                            }
                        }

                        @Override
                        public void onFailure(Call<EventPatternResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });
    }

    private String getTimeFormat(int hourOfDay, int minutes) {
        return String.format("%d:%02d", hourOfDay, minutes);
    }

    private void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void updateToken() {
        if (userToken == null)
            firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                userToken = task.getResult().getToken();
            });
        else
            userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
    }

    private void recountStartAndEnd() {
        eventPattern.setStartedAt(start.getTimeInMillis());
        eventPattern.setDuration(end.getTimeInMillis() - start.getTimeInMillis());
        eventPattern.setEndedAt(start.getTimeInMillis() + eventPattern.getDuration());
    }
}
