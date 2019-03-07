package com.github.royalstorm.android_task_manager.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;

import java.util.Calendar;
import java.util.Locale;

public class WeekActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_activity);

        setCurrentDay();
    }

    private void setCurrentDay() {
        Calendar calendar = Calendar.getInstance();

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
                day = findViewById(R.id.thu);
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
}
