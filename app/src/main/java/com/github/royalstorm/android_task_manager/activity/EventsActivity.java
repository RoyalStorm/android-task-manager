package com.github.royalstorm.android_task_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.adapter.DayEventsAdapter;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.service.MockUpEventService;

import java.util.ArrayList;
import java.util.List;

public class EventsActivity extends AppCompatActivity {

    private MockUpEventService mockUpEventService = new MockUpEventService();

    private List<Event> eventsList;

    private ListView listView;

    private FloatingActionButton floatingActionButton;

    private String time;

    private int day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        day = getIntent().getExtras().getInt("day");
        month = getIntent().getExtras().getInt("month");
        year = getIntent().getExtras().getInt("year");
        time = getIntent().getExtras().get("beginTime").toString();

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventsActivity.this, AddEventActivity.class);
                intent.putExtra("beginTime", time);
                intent.putExtra("day", day);
                intent.putExtra("month", month);
                intent.putExtra("year", year);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        eventsList = new ArrayList<>();
        eventsList = mockUpEventService.findByDateAndTime(day + "/" + month + "/" + year, time);

        listView = (ListView) findViewById(R.id.events);

        DayEventsAdapter adapter = new DayEventsAdapter(this, R.layout.events_list_view, eventsList);

        listView.setAdapter(adapter);
    }
}
