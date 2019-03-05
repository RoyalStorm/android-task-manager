package com.github.royalstorm.android_task_manager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.royalstorm.android_task_manager.R;

public class DayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        showHours();
    }

    private void showHours() {
        ListView hoursList = findViewById(R.id.hours);

        String[] hours = getResources().getStringArray(R.array.hours);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, hours);

        hoursList.setAdapter(adapter);
    }
}
