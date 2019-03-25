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

    MockUpEventService mockUpEventService = new MockUpEventService();

    List<Event> eventsList;

    ListView listView;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = getIntent().getExtras().get("beginTime").toString();

                Intent intent = new Intent(EventsActivity.this, AddEventActivity.class);
                intent.putExtra("beginTime", time);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        eventsList = new ArrayList<>();
        eventsList = mockUpEventService.findByDateAndTime("25/03/2019", "18:00");

        listView = (ListView) findViewById(R.id.events);

        DayEventsAdapter adapter = new DayEventsAdapter(this, R.layout.events_list_view, eventsList);

        listView.setAdapter(adapter);
    }
}
