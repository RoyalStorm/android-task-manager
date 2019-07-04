package com.github.royalstorm.android_task_manager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.google.ical.values.RRule;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepeatModeActivity extends AppCompatActivity {

    @BindView(R.id.date)
    TextView date;

    private String[] modes = {"День", "Неделя", "Месяц", "Год"};

    private RRule rRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repeat_mode);
        setTitle("Повтор события");

        ButterKnife.bind(this);

        Spinner spinner = findViewById(R.id.repeat_mode);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        rRule = new RRule();
        rRule.setCount(1);

        //Bundle bundle = getIntent().getExtras();
        //EventPattern eventPattern = (EventPattern) bundle.getSerializable(EventPattern.class.getSimpleName());
        //date.setText(new SimpleDateFormat("d MMMM yyyy (E)", Locale.getDefault()).format(eventPattern.getEndedAt()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_event:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
