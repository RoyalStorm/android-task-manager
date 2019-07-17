package com.github.royalstorm.android_task_manager.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.service.EventService;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class WeekFragment extends Fragment implements EventService.RequestEventCallback {
    private GregorianCalendar gregorianCalendar;

    private TextView currentWeek;
    private ImageButton prevWeek;
    private ImageButton nextWeek;

    private int day;
    private int month;
    private int year;

    private EventService eventService;

    private List<EventInstance> eventInstances = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private String userToken;

    private View view;
    private Context context;

    private LinearLayout scheduleWrapper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_week,
                container, false);

        scheduleWrapper = view.findViewById(R.id.schedule_wrapper);

        EventBus.getDefault().register(this);
        eventService = new EventService(this);

        firebaseAuth = FirebaseAuth.getInstance();
        userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
        context = view.getContext();

        setPrevWeekListener(view);
        setNextWeekListener(view);
        setDays(view);

        return view;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEventsInstancesByInterval(List<EventInstance> eventInstances) {
        this.eventInstances = eventInstances;

        Long[] ids = Stream.of(eventInstances).map(EventInstance::getEventId).toArray(Long[]::new);
        eventService.getEventsById(ids, userToken);
    }

    @Override
    public void requestEventSuccess(boolean success, EventResponse eventResponse) {
        createScheduleGrid(view, Arrays.asList(eventResponse.getData()));

        lockNavigationButtons(true);
    }

    private void setDays(View view) {
        currentWeek = view.findViewById(R.id.current_week);

        Bundle bundle = this.getArguments();

        gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setFirstDayOfWeek(Calendar.MONDAY);
        gregorianCalendar.set(Calendar.DAY_OF_WEEK, gregorianCalendar.getFirstDayOfWeek());

        if (bundle != null) {
            day = bundle.getInt("day");
            month = bundle.getInt("month");
            year = bundle.getInt("year");
        } else {
            day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            month = gregorianCalendar.get(Calendar.MONTH);
            year = gregorianCalendar.get(Calendar.YEAR);
        }

        gregorianCalendar.set(year, month, day);

        currentWeek.setText(new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(gregorianCalendar.getTime()));

        TextView monNumber = view.findViewById(R.id.monNumber);
        TextView tueNumber = view.findViewById(R.id.tueNumber);
        TextView wedNumber = view.findViewById(R.id.wedNumber);
        TextView thuNumber = view.findViewById(R.id.thuNumber);
        TextView friNumber = view.findViewById(R.id.friNumber);
        TextView satNumber = view.findViewById(R.id.satNumber);
        TextView sunNumber = view.findViewById(R.id.sunNumber);

        SimpleDateFormat day = new SimpleDateFormat("d", Locale.getDefault());

        gregorianCalendar.set(Calendar.SECOND, 0);
        gregorianCalendar.set(Calendar.HOUR_OF_DAY, 0);
        gregorianCalendar.set(Calendar.MINUTE, 0);
        Long from = gregorianCalendar.getTimeInMillis();

        monNumber.setText(day.format(gregorianCalendar.getTime()));

        gregorianCalendar.add(Calendar.DAY_OF_WEEK, 1);
        tueNumber.setText(day.format(gregorianCalendar.getTime()));

        gregorianCalendar.add(Calendar.DAY_OF_WEEK, 1);
        wedNumber.setText(day.format(gregorianCalendar.getTime()));

        gregorianCalendar.add(Calendar.DAY_OF_WEEK, 1);
        thuNumber.setText(day.format(gregorianCalendar.getTime()));

        gregorianCalendar.add(Calendar.DAY_OF_WEEK, 1);
        friNumber.setText(day.format(gregorianCalendar.getTime()));

        gregorianCalendar.add(Calendar.DAY_OF_WEEK, 1);
        satNumber.setText(day.format(gregorianCalendar.getTime()));

        gregorianCalendar.add(Calendar.DAY_OF_WEEK, 1);
        sunNumber.setText(day.format(gregorianCalendar.getTime()));

        gregorianCalendar.set(Calendar.HOUR_OF_DAY, 23);
        gregorianCalendar.set(Calendar.MINUTE, 59);
        gregorianCalendar.set(Calendar.SECOND, 59);
        Long to = gregorianCalendar.getTimeInMillis();

        lockNavigationButtons(false);

        if (userToken == null) {
            firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                userToken = task.getResult().getToken();
                eventService.getEventInstancesByInterval(from, to, userToken);
            });
        } else {
            userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
            eventService.getEventInstancesByInterval(from, to, userToken);
        }
    }

    private void setPrevWeekListener(View view) {
        prevWeek = view.findViewById(R.id.prev_week);
        prevWeek.setOnClickListener(v -> {
            gregorianCalendar.add(Calendar.DAY_OF_MONTH, -13);

            day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            month = gregorianCalendar.get(Calendar.MONTH);
            year = gregorianCalendar.get(Calendar.YEAR);

            Bundle bundle = new Bundle();
            bundle.putInt("day", day);
            bundle.putInt("month", month);
            bundle.putInt("year", year);

            WeekFragment weekFragment = new WeekFragment();
            weekFragment.setArguments(bundle);

            prevWeek.setClickable(false);

            getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                    weekFragment).commit();
        });
    }

    private void setNextWeekListener(View view) {
        nextWeek = view.findViewById(R.id.next_week);
        nextWeek.setOnClickListener(v -> {
            gregorianCalendar.add(Calendar.DAY_OF_MONTH, 1);

            day = gregorianCalendar.get(Calendar.DAY_OF_MONTH);
            month = gregorianCalendar.get(Calendar.MONTH);
            year = gregorianCalendar.get(Calendar.YEAR);

            Bundle bundle = new Bundle();
            bundle.putInt("day", day);
            bundle.putInt("month", month);
            bundle.putInt("year", year);

            WeekFragment weekFragment = new WeekFragment();
            weekFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                    weekFragment).commit();
        });
    }

    private void createScheduleGrid(View view, List<Event> events) {
        TableLayout tableLayout = view.findViewById(R.id.schedule_table);
        TableRow row;

        for (int i = 0; i < 24; i++) {
            row = new TableRow(context);

            LinearLayout[] days = new LinearLayout[7];

            for (int j = 0; j < 7; j++) {
                days[j] = new LinearLayout(context);
                days[j].setOrientation(LinearLayout.HORIZONTAL);
                days[j].setMinimumHeight(dpToPix(60));
                days[j].setId(j * 100 + i);

                days[j].setBackground(context.getDrawable(R.drawable.text_view_border));

                row.addView(days[j]);

                List<Event> foundedEvents = findByMoment(events, year, month, day + j, i);
                if (foundedEvents.size() > 0) {
                    int eventCount = 1;
                    for (Event event : foundedEvents) {
                        if (eventCount > 4)
                            break;

                        TextView eventName = new TextView(context);

                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                dpToPix(2),
                                dpToPix(60),
                                0.2f);
                        layoutParams.setMargins(-1, -1, -1, -1);
                        eventName.setLayoutParams(layoutParams);

                        if (event != null) {
                            eventName.setText(event.getName());
                            eventName.setTextColor(context.getResources().getColor(R.color.white));

                            if (eventCount == 1)
                                eventName.setBackgroundColor(context.getResources().getColor(R.color.purple));
                            if (eventCount == 2)
                                eventName.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                            if (eventCount == 3)
                                eventName.setBackgroundColor(context.getResources().getColor(R.color.pink));
                            if (eventCount == 4) {
                                eventName.setTextColor(context.getResources().getColor(R.color.elegant_color));
                                eventName.setText("......");
                            }
                        }

                        days[j].addView(eventName);
                        days[j].setBackground(context.getDrawable(R.drawable.text_view_border));

                        eventCount++;
                    }
                }

                days[j].setOnClickListener(v -> {
                    day += ((v.getId()) / 100);

                    Bundle bundle = new Bundle();
                    bundle.putInt("day", day);
                    bundle.putInt("month", month);
                    bundle.putInt("year", year);

                    DayFragment dayFragment = new DayFragment();
                    dayFragment.setArguments(bundle);

                    getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                            dayFragment).commit();

                    NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
                    navigationView.setCheckedItem(R.id.nav_day);
                });
            }

            tableLayout.addView(row);
        }
    }

    private int dpToPix(int dp) {
        return dp * (int) context.getResources().getDisplayMetrics().density;
    }

    private List<Event> findByMoment(List<Event> events, int year, int month, int day, int hour) {
        List<Event> foundEvents = new ArrayList<>();

        GregorianCalendar start = new GregorianCalendar(year, month, day, hour, 0);
        GregorianCalendar end = new GregorianCalendar(year, month, day, hour, 59);

        for (EventInstance eventInstance : eventInstances) {
            if (eventInstance.getStartedAt() <= end.getTimeInMillis() && eventInstance.getEndedAt() >= start.getTimeInMillis())
                foundEvents.add(
                        Stream.of(events)
                                .filter(e -> e.getId().equals(eventInstance.getEventId()))
                                .findFirst()
                                .orElse(null)
                );
        }

        return foundEvents;
    }

    private void lockNavigationButtons(boolean lock) {
        prevWeek.setClickable(lock);
        prevWeek.setEnabled(lock);
        nextWeek.setClickable(lock);
        nextWeek.setEnabled(lock);

        if (lock)
            scheduleWrapper.setVisibility(View.VISIBLE);
        else
            scheduleWrapper.setVisibility(View.GONE);
    }
}
