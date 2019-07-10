package com.github.royalstorm.android_task_manager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.activity.AddTaskActivity;
import com.github.royalstorm.android_task_manager.activity.EditTaskActivity;
import com.github.royalstorm.android_task_manager.adapter.EventAdapter;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.dto.EventPatternResponse;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.fragment.ui.dialog.SelectDateDialog;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DayFragment extends Fragment implements SelectDateDialog.SelectDayDialogListener, EventAdapter.OnEventListener {
    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    private List<EventInstance> eventInstances;

    private GregorianCalendar gregorianCalendar;

    private TextView prevDay;
    private TextView currentDay;
    private TextView nextDay;

    private int day;
    private int month;
    private int year;

    private static final int ADD = 1;
    private static final int EDIT = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        setDate(view);
        setCurrentDayListener(view);
        setPrevDayListener(view);
        setNextDayListener(view);

        FloatingActionButton fab = view.findViewById(R.id.add_event);
        fab.setOnClickListener(v -> createTask());

        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventAdapter = new EventAdapter(new ArrayList<>(), this);

        return view;
    }

    private void setDate(View view) {
        currentDay = view.findViewById(R.id.current_day);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            day = bundle.getInt("day");
            month = bundle.getInt("month");
            year = bundle.getInt("year");
        } else {
            day = new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
            month = new GregorianCalendar().get(Calendar.MONTH);
            year = new GregorianCalendar().get(Calendar.YEAR);
        }

        gregorianCalendar = new GregorianCalendar(year, month, day);

        currentDay.setText(new SimpleDateFormat("d MMMM (E) yyyy", Locale.getDefault()).format(gregorianCalendar.getTime()));
    }

    private void setCurrentDayListener(View view) {
        currentDay = view.findViewById(R.id.current_day);
        currentDay.setOnClickListener(v -> {
            SelectDateDialog selectDateDialog = new SelectDateDialog();

            Bundle bundle = new Bundle();
            bundle.putInt("day", day);
            bundle.putInt("month", month);
            bundle.putInt("year", year);

            selectDateDialog.setTargetFragment(DayFragment.this, 1);
            selectDateDialog.setArguments(bundle);
            selectDateDialog.show(getFragmentManager(), "Select day dialog");
        });
    }

    private void setPrevDayListener(View view) {
        prevDay = view.findViewById(R.id.prev_day);
        prevDay.setOnClickListener(v -> {
            gregorianCalendar.add(Calendar.DAY_OF_MONTH, -1);

            day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            month = gregorianCalendar.get(Calendar.MONTH);
            year = gregorianCalendar.get(Calendar.YEAR);

            Bundle bundle = new Bundle();
            bundle.putInt("day", day);
            bundle.putInt("month", month);
            bundle.putInt("year", year);

            DayFragment dayFragment = new DayFragment();
            dayFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                    dayFragment).commit();
        });
    }

    private void setNextDayListener(View view) {
        nextDay = view.findViewById(R.id.next_day);
        nextDay.setOnClickListener(v -> {
            gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

            day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            month = gregorianCalendar.get(Calendar.MONTH);
            year = gregorianCalendar.get(Calendar.YEAR);

            Bundle bundle = new Bundle();
            bundle.putInt("day", day);
            bundle.putInt("month", month);
            bundle.putInt("year", year);

            DayFragment dayFragment = new DayFragment();
            dayFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                    dayFragment).commit();
        });
    }

    private void createTask() {
        EventInstance eventInstance = new EventInstance();
        eventInstance.setStartedAt(calendarToMillis(new GregorianCalendar(year, month, day, new GregorianCalendar().getTime().getHours(), new GregorianCalendar().getTime().getMinutes())));
        eventInstance.setEndedAt(calendarToMillis(new GregorianCalendar(year, month, day, new GregorianCalendar().getTime().getHours(), new GregorianCalendar().getTime().getMinutes())));

        Intent intent = new Intent(getActivity(), AddTaskActivity.class);
        intent.putExtra(EventInstance.class.getSimpleName(), eventInstance);
        startActivityForResult(intent, ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        String userToken = FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).getResult().getToken();

        if (requestCode == ADD) {
            retrofitClient.getEventRepository().save((Event) data.getSerializableExtra(Event.class.getSimpleName()), userToken).enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            retrofitClient.getEventPatternRepository().save(response.body().getData()[0].getId(), (EventPattern) data.getSerializableExtra(EventPattern.class.getSimpleName())).enqueue(new Callback<EventPatternResponse>() {
                                @Override
                                public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
                                    getEvents(
                                            new GregorianCalendar(year, month, day, 0, 0),
                                            new GregorianCalendar(year, month, day, 23, 59)
                                    );
                                }

                                @Override
                                public void onFailure(Call<EventPatternResponse> call, Throwable t) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {

                }
            });
        } else {
            if (data.getStringExtra("Action").equals("Delete"))
                retrofitClient.getEventRepository().delete(data.getLongExtra("id", 0)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful())
                            getEvents(
                                    new GregorianCalendar(year, month, day, 0, 0),
                                    new GregorianCalendar(year, month, day, 23, 59)
                            );
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                    }
                });
            else {
                Event event = (Event) data.getSerializableExtra(Event.class.getSimpleName());
                EventPattern eventPattern = (EventPattern) data.getSerializableExtra(EventPattern.class.getSimpleName());
                Long eventId = data.getLongExtra("eventId", 0);
                Long patternId = data.getLongExtra("patternId", 0);

                retrofitClient.getEventPatternRepository().update(patternId, eventPattern).enqueue(new Callback<EventPatternResponse>() {
                    @Override
                    public void onResponse(Call<EventPatternResponse> call, Response<EventPatternResponse> response) {
                        if (response.isSuccessful())
                            retrofitClient.getEventRepository().update(eventId, event).enqueue(new Callback<EventResponse>() {
                                @Override
                                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                                    if (response.isSuccessful())
                                        getEvents(
                                                new GregorianCalendar(year, month, day, 0, 0),
                                                new GregorianCalendar(year, month, day, 23, 59)
                                        );
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
        }
    }

    private void getEvents(GregorianCalendar from, GregorianCalendar to) {
        retrofitClient.getEventRepository().getEventInstancesByInterval(from.getTimeInMillis(), to.getTimeInMillis()).enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        eventInstances = Arrays.asList(response.body().getData());
                        Collections.sort(eventInstances, (a, b) -> a.getStartedAt().compareTo(b.getStartedAt()));
                        eventAdapter.setItems(eventInstances);
                        recyclerView.setAdapter(eventAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getEvents(
                new GregorianCalendar(year, month, day, 0, 0),
                new GregorianCalendar(year, month, day, 23, 59)
        );
    }

    @Override
    public void setDay(int year, int month, int day) {
        Bundle bundle = new Bundle();
        bundle.putInt("day", day);
        bundle.putInt("month", month);
        bundle.putInt("year", year);

        DayFragment dayFragment = new DayFragment();
        dayFragment.setArguments(bundle);

        getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                dayFragment).commit();
    }

    @Override
    public void onEventClick(int position) {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);
        intent.putExtra(EventInstance.class.getSimpleName(), eventInstances.get(position));
        startActivityForResult(intent, EDIT);
    }

    private Long calendarToMillis(GregorianCalendar calendar) {
        return calendar.getTimeInMillis();
    }
}
