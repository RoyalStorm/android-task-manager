package com.github.royalstorm.android_task_manager.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeekFragment extends Fragment {

    private Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week,
                container, false);

        setCurrentDay(view);
        setDaysNumbers(view);

        return view;
    }

    private void setCurrentDay(View view) {
        String currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        int color = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        TextView day;

        switch (currentDay) {
            case "Sunday":
                day = view.findViewById(R.id.sun);
                day.setTextColor(color);
                break;

            case "Monday":
                day = view.findViewById(R.id.mon);
                day.setTextColor(color);
                break;

            case "Tuesday":
                day = view.findViewById(R.id.tue);
                day.setTextColor(color);
                break;

            case "Wednesday":
                day = view.findViewById(R.id.wed);
                day.setTextColor(color);
                break;

            case "Thursday":
                day = view.findViewById(R.id.thu);
                day.setTextColor(color);
                break;

            case "Friday":
                day = view.findViewById(R.id.fri);
                day.setTextColor(color);
                break;

            case "Saturday":
                day = view.findViewById(R.id.sat);
                day.setTextColor(color);
                break;
        }
    }

    private void setDaysNumbers(View view) {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        SimpleDateFormat dayNumber = new SimpleDateFormat("dd");

        TextView number;

        for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0:
                    number = view.findViewById(R.id.monNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 1:
                    number = view.findViewById(R.id.tueNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 2:
                    number = view.findViewById(R.id.wedNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 3:
                    number = view.findViewById(R.id.thuNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 4:
                    number = view.findViewById(R.id.friNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 5:
                    number = view.findViewById(R.id.satNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 6:
                    number = view.findViewById(R.id.sunNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
            }

            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
    }
}
