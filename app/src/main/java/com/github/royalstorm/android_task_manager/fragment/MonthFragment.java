package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.EventDay;
import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.service.EventService;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MonthFragment extends Fragment {
    private com.applandeo.materialcalendarview.CalendarView calendar;

    private EventService eventService = new EventService();
    private List<EventDay> events = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private String userToken = null;

    private ArrayList<Integer> iconsIDs = new ArrayList<Integer>() {{
        add(R.drawable.looks_9_plus);
        add(R.drawable.looks_1);
        add(R.drawable.looks_2);
        add(R.drawable.looks_3);
        add(R.drawable.looks_4);
        add(R.drawable.looks_5);
        add(R.drawable.looks_6);
        add(R.drawable.looks_7);
        add(R.drawable.looks_8);
        add(R.drawable.looks_9);
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        calendar = view.findViewById(R.id.calendar);

        calendar.setOnDayClickListener(eventDay -> {
            Bundle bundle = new Bundle();

            bundle.putInt("day", eventDay.getCalendar().get(Calendar.DAY_OF_MONTH));
            bundle.putInt("month", eventDay.getCalendar().get(Calendar.MONTH));
            bundle.putInt("year", eventDay.getCalendar().get(Calendar.YEAR));

            DayFragment dayFragment = new DayFragment();
            dayFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                    dayFragment).commit();

            NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_day);
        });

        firebaseAuth = FirebaseAuth.getInstance();
        /*Get to crash in this place*/
        //userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();

        EventBus.getDefault().register(this);

        GregorianCalendar beginOfYear = new GregorianCalendar();
        beginOfYear.set(Calendar.MONTH, Calendar.JANUARY);
        beginOfYear.set(Calendar.DAY_OF_MONTH, 1);
        beginOfYear.set(Calendar.HOUR_OF_DAY, 0);
        beginOfYear.set(Calendar.MINUTE, 0);

        GregorianCalendar now = new GregorianCalendar();

        GregorianCalendar endOfYear = new GregorianCalendar();
        endOfYear.set(Calendar.MONTH, Calendar.DECEMBER);
        endOfYear.set(Calendar.DAY_OF_MONTH, 31);
        endOfYear.set(Calendar.HOUR_OF_DAY, 23);
        endOfYear.set(Calendar.MINUTE, 59);

        if (userToken == null)
            firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                userToken = task.getResult().getToken();
                eventService.getEventInstancesByInterval(beginOfYear.getTimeInMillis(), endOfYear.getTimeInMillis(), userToken);
            });
        else {
            userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
            eventService.getEventInstancesByInterval(beginOfYear.getTimeInMillis(), endOfYear.getTimeInMillis(), userToken);
        }

        calendar.setOnPreviousPageChangeListener(() -> {
            now.add(Calendar.MONTH, -1);
            if (now.get(Calendar.YEAR) != beginOfYear.get(Calendar.YEAR)) {
                beginOfYear.add(Calendar.YEAR, -1);
                endOfYear.add(Calendar.YEAR, -1);

                if (userToken == null) {
                    firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                        userToken = task.getResult().getToken();
                        eventService.getEventInstancesByInterval(beginOfYear.getTimeInMillis(), endOfYear.getTimeInMillis(), userToken);
                    });
                } else {
                    userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
                    eventService.getEventInstancesByInterval(beginOfYear.getTimeInMillis(), endOfYear.getTimeInMillis(), userToken);
                }
            }
        });

        calendar.setOnForwardPageChangeListener(() -> {
            now.add(Calendar.MONTH, 1);
            if (now.get(Calendar.YEAR) != endOfYear.get(Calendar.YEAR)) {
                beginOfYear.add(Calendar.YEAR, 1);
                endOfYear.add(Calendar.YEAR, 1);

                if (userToken == null) {
                    firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                        userToken = task.getResult().getToken();
                        eventService.getEventInstancesByInterval(beginOfYear.getTimeInMillis(), endOfYear.getTimeInMillis(), userToken);
                    });
                } else {
                    userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
                    eventService.getEventInstancesByInterval(beginOfYear.getTimeInMillis(), endOfYear.getTimeInMillis(), userToken);
                }
            }
        });

        return view;
    }

    @Subscribe
    public void onEventInstanceResponse(List<EventInstance> eventInstances) {
        for (EventInstance eventInstance : eventInstances) {
            GregorianCalendar start = new GregorianCalendar();
            start.setTimeInMillis(eventInstance.getStartedAt());
            GregorianCalendar now = start;
            start.set(Calendar.HOUR_OF_DAY, 0);
            start.set(Calendar.MINUTE, 0);
            GregorianCalendar end = new GregorianCalendar();
            end.setTimeInMillis(eventInstance.getEndedAt());
            end.set(Calendar.HOUR_OF_DAY, 0);
            end.set(Calendar.MINUTE, 0);

            //Stream.of(eventInstances).filter()

            while (isWithinRange(start, now, end)) {
                events.add(new EventDay(new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)), R.drawable.ic_star));
                now.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        calendar.setEvents(events);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private boolean isWithinRange(GregorianCalendar start, GregorianCalendar now, GregorianCalendar end) {
        return !(now.before(start) || now.after(end));
    }
}
