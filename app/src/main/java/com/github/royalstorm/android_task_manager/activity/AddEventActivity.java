package com.github.royalstorm.android_task_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.adapter.DayEventsAdapter;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.service.MockUpEventService;

import java.util.ArrayList;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    private MockUpEventService mockUpEventService = new MockUpEventService();

    List<Event> eventsList;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        eventsList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.events);

        eventsList.add(new Event(0, "Лекция"));
        eventsList.add(new Event(1, "Кино"));

        DayEventsAdapter adapter = new DayEventsAdapter(this, R.layout.events_list_view, eventsList);

        listView.setAdapter(adapter);
    }

    public void createEvent(View view) {
        Intent intent = new Intent();

        // mockUpEventService.create(new Event(MockUpEventService.counter, this.eventTitle.getText().toString()));

        ++MockUpEventService.counter;

        setResult(RESULT_OK, intent);

        finish();
    }
}
