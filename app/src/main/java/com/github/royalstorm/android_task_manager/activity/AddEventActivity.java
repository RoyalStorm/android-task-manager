package com.github.royalstorm.android_task_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.service.MockUpEventService;

public class AddEventActivity extends AppCompatActivity {

    private static MockUpEventService mockUpEventService = new MockUpEventService();

    EditText eventTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_activity);

        eventTitle = findViewById(R.id.eventTitle);
    }

    public void createEvent(View view) {
        Intent intent = new Intent();

        int id = mockUpEventService.getCounter();

        Log.d("______Event", Integer.toString(id));

        mockUpEventService.add(new Event(id, this.eventTitle.getText().toString()));

        mockUpEventService.setCounter(++id);

        setResult(RESULT_OK, intent);

        finish();
    }
}
