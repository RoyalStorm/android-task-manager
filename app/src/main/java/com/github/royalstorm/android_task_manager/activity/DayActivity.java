package com.github.royalstorm.android_task_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.activity.dao.Event;

public class DayActivity extends AppCompatActivity {

    ListView hoursList;

    Event event;

    ArrayAdapter<String> adapter;

    //TODO: send time in AddEventActivity
    private static String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        hoursList = findViewById(R.id.hours);

        hoursList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View itemClicked,
                                    int position,
                                    long id) {
                time = adapter.getItem(position);

                createEvent();
            }
        });

        showHours();
    }

    private void showHours() {
        String[] hours = getResources().getStringArray(R.array.hours);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, hours);

        hoursList.setAdapter(adapter);
    }

    public void createEvent() {
        Intent intent = new Intent(this, AddEventActivity.class);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
            return;

        event = (Event) data.getExtras().getSerializable(Event.class.getSimpleName());

        Toast.makeText(getApplicationContext(), "Событие \"" + event.getEventTitle() + "\" было создано. Время: " + time,
                Toast.LENGTH_SHORT).show();
    }
}
