package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.EventDay;
import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.service.EventProxyService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MonthFragment extends Fragment {
    private com.applandeo.materialcalendarview.CalendarView calendar;

    private EventProxyService eventProxyService;

    private List<EventDay> events = new ArrayList<>();

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
        });

        eventProxyService = new EventProxyService();

        EventBus.getDefault().register(this);

        return view;
    }

    @Subscribe
    public void onEventInstanceResponse(EventInstanceResponse eventInstanceResponse) {
        EventInstance[] eventInstances = eventInstanceResponse.getData();

        for (EventInstance eventInstance : eventInstances) {
            GregorianCalendar begin = new GregorianCalendar();
            begin.setTimeInMillis(eventInstance.getStartedAt());
            GregorianCalendar now = begin;
            GregorianCalendar end = new GregorianCalendar();
            end.setTimeInMillis(eventInstance.getEndedAt());

            while (!(now.before(begin) || now.after(end))) {
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
}
