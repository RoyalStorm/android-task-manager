package com.github.royalstorm.android_task_manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Task;
import com.github.royalstorm.android_task_manager.service.MockUpTaskService;

public class EditTaskActivity extends AppCompatActivity {
    private MockUpTaskService mockUpTaskService = new MockUpTaskService();

    private Task task;

    private EditText taskName;
    private EditText taskDetails;

    private TextView taskBeginDate;
    private TextView taskEndDate;
    private TextView taskBeginTime;
    private TextView taskEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Редактирование события");

        Bundle bundle = getIntent().getExtras();
        task = (Task) bundle.getSerializable(Task.class.getSimpleName());

        findComponents();
        initFields();
    }

    private void findComponents() {
        taskName = findViewById(R.id.task_name);
        taskDetails = findViewById(R.id.task_details);
        taskBeginDate = findViewById(R.id.task_begin_date);
        taskEndDate = findViewById(R.id.task_end_date);
        taskBeginTime = findViewById(R.id.task_begin_time);
        taskEndTime = findViewById(R.id.task_end_time);
    }

    private void initFields() {
        taskName.setText(task.getName());
        taskDetails.setText(task.getDetails());
    }

    private void deleteTask(int id) {
        mockUpTaskService.delete(id);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void editTask(Task task) {
        updateTask();
        mockUpTaskService.update(task.getId(), task);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateTask() {
        task.setName(taskName.getText().toString());
        task.setDetails(taskDetails.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_event:
                editTask(task);
                return true;

            case R.id.delete_event:
                deleteTask(task.getId());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
