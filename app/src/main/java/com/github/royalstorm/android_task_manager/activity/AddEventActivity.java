package com.github.royalstorm.android_task_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.activity.dao.Event;

public class AddEventActivity extends AppCompatActivity {

    EditText eventTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_activity);

        eventTitle = (EditText) findViewById(R.id.eventTitle);
    }

    public void createEvent(View view) {
        Intent intent = new Intent();

        intent.putExtra(Event.class.getSimpleName(), new Event(eventTitle.getText().toString()));

        setResult(RESULT_OK, intent);

        Log.d("Event log", eventTitle.getText().toString());

        finish();
    }
}
