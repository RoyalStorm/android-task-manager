package com.github.royalstorm.android_task_manager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WeekActivity extends AppCompatActivity {

    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.week_activity);

        setCurrentDay();
        setDaysNumbers();
    }

    private void setCurrentDay() {
        String currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        String color = "#ff6f00";

        TextView day;

        switch (currentDay) {
            case "Sunday":
                day = findViewById(R.id.sun);
                day.setTextColor(Color.parseColor(color));
                break;

            case "Monday":
                day = findViewById(R.id.mon);
                day.setTextColor(Color.parseColor(color));
                break;

            case "Tuesday":
                day = findViewById(R.id.tue);
                day.setTextColor(Color.parseColor(color));
                break;

            case "Wednesday":
                day = findViewById(R.id.wed);
                day.setTextColor(Color.parseColor(color));
                break;

            case "Thursday":
                day = findViewById(R.id.thu);
                day.setTextColor(Color.parseColor(color));
                break;

            case "Friday":
                day = findViewById(R.id.fri);
                day.setTextColor(Color.parseColor(color));
                break;

            case "Saturday":
                day = findViewById(R.id.sat);
                day.setTextColor(Color.parseColor(color));
                break;
        }
    }

    private void setDaysNumbers() {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        SimpleDateFormat dayNumber = new SimpleDateFormat("dd");

        TextView number;

        for (int i = 0; i < 7; i++) {
            switch (i) {
                case 0:
                    number = findViewById(R.id.monNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 1:
                    number = findViewById(R.id.tueNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 2:
                    number = findViewById(R.id.wedNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 3:
                    number = findViewById(R.id.thuNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 4:
                    number = findViewById(R.id.friNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 5:
                    number = findViewById(R.id.satNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
                case 6:
                    number = findViewById(R.id.sunNumber);
                    number.setText(dayNumber.format(calendar.getTime()));
                    break;
            }

            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
    }
}
