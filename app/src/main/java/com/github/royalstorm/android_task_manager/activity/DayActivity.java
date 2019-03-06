package com.github.royalstorm.android_task_manager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.royalstorm.android_task_manager.R;

public class DayActivity extends AppCompatActivity {

    ListView hoursList;

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
                selectTime(itemClicked);
            }
        });

        showHours();
    }

    private void showHours() {
        String[] hours = getResources().getStringArray(R.array.hours);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, hours);

        hoursList.setAdapter(adapter);
    }

    private void selectTime(View itemClicked) {
        Toast.makeText(getApplicationContext(), ((TextView) itemClicked).getText(),
                Toast.LENGTH_SHORT).show();
    }
}
