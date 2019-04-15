package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.service.MockUpTaskService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class WeekFragment extends Fragment {
    private GregorianCalendar gregorianCalendar;

    private TextView currentWeek;
    private TextView prevWeek;
    private TextView nextWeek;

    private int day;
    private int month;
    private int year;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week,
                container, false);

        setDays(view);
        setPrevWeekListener(view);
        setNextWeekListener(view);
        createScheduleGrid(view);

        return view;
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
            month = new GregorianCalendar().get(Calendar.MONTH);
            year = new GregorianCalendar().get(Calendar.YEAR);
        }

        gregorianCalendar.set(year, month, day);

        SimpleDateFormat month = new SimpleDateFormat("MMM", Locale.getDefault());
        String date = month.format(gregorianCalendar.getTime());

        TextView monNumber = view.findViewById(R.id.monNumber);
        TextView tueNumber = view.findViewById(R.id.tueNumber);
        TextView wedNumber = view.findViewById(R.id.wedNumber);
        TextView thuNumber = view.findViewById(R.id.thuNumber);
        TextView friNumber = view.findViewById(R.id.friNumber);
        TextView satNumber = view.findViewById(R.id.satNumber);
        TextView sunNumber = view.findViewById(R.id.sunNumber);

        SimpleDateFormat day = new SimpleDateFormat("d", Locale.getDefault());

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

        currentWeek.setText(date);
    }

    private void setPrevWeekListener(View view) {
        prevWeek = view.findViewById(R.id.prev_week);
        prevWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                        weekFragment).commit();
            }
        });
    }

    private void setNextWeekListener(View view) {
        nextWeek = view.findViewById(R.id.next_week);
        nextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }

    private void createScheduleGrid(View view) {
        MockUpTaskService mockUpTaskService = new MockUpTaskService();
        TableLayout tableLayout = view.findViewById(R.id.scheduleTable);
        TableRow row;

        for (int i = 0; i < 24; i++) {
            row = new TableRow(getContext());

            TextView[] days = new TextView[7];

            for (int j = 0; j < 7; j++) {
                days[j] = new TextView(getContext());
                days[j].setHeight(dpToPix());
                days[j].setId(j * 100 + i);

                days[j].setBackground(days[j].getContext().getDrawable(R.drawable.text_view_border));

                row.addView(days[j]);

                if (mockUpTaskService.findByDateAndTime(year, month, day + j, i, 0).size() > 0)
                    days[j].setBackground(days[j].getContext().getDrawable(R.drawable.side_nav_bar));

                days[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        day += ((v.getId()) / 100);

                        Bundle bundle = new Bundle();
                        bundle.putInt("day", day);
                        bundle.putInt("month", month);
                        bundle.putInt("year", year);

                        DayFragment dayFragment = new DayFragment();
                        dayFragment.setArguments(bundle);

                        getFragmentManager().beginTransaction().replace(R.id.calendarContainer,
                                dayFragment).commit();
                    }
                });
            }

            tableLayout.addView(row);
        }
    }

    private int dpToPix() {
        return 60 * (int) getContext().getResources().getDisplayMetrics().density;
    }
}
