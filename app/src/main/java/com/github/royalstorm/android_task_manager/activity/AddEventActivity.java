package com.github.royalstorm.android_task_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.service.MockUpEventService;

public class AddEventActivity extends AppCompatActivity {

    private MockUpEventService mockUpEventService = new MockUpEventService();

    EditText eventName;
    EditText eventDetails;
    EditText eventEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        eventName = findViewById(R.id.eventName);
        eventDetails = findViewById(R.id.eventDetails);
        eventEndTime = findViewById(R.id.eventEndTime);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Новое событие");
    }

    private void createEvent() {
        if (eventName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Заголовок не может быть пустым", Toast.LENGTH_LONG).show();
            return;
        }


        Intent intent = new Intent();

        String beginTime = getIntent().getExtras().get("beginTime").toString();

        int day = getIntent().getExtras().getInt("day");
        int month = getIntent().getExtras().getInt("month");
        int year = getIntent().getExtras().getInt("year");

        mockUpEventService.create(
                new Event(
                        MockUpEventService.counter,
                        "Me",
                        this.eventName.getText().toString(),
                        day + "/" + month + "/" + year,
                        beginTime,
                        this.eventEndTime.getText().toString())
        );

        ++MockUpEventService.counter;

        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_event_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveEvent:
                createEvent();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
