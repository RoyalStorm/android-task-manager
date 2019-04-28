package com.github.royalstorm.android_task_manager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Task;

import java.util.List;

public class TasksAdapter extends ArrayAdapter<Task> {
    private List<Task> tasksList;

    private Context context;

    private int resource;

    public TasksAdapter(Context context, int resource, List<Task> tasksList) {
        super(context, resource, tasksList);
        this.context = context;
        this.resource = resource;
        this.tasksList = tasksList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        TextView beginTime = view.findViewById(R.id.begin_time);
        TextView endTime = view.findViewById(R.id.end_time);
        TextView name = view.findViewById(R.id.name);
        TextView details = view.findViewById(R.id.details);

        Task task = tasksList.get(position);

        beginTime.setText(getTimeFormat(task.getBeginHour(), task.getBeginMinute()));
        endTime.setText(getTimeFormat(task.getEndHour(), task.getEndMinute()));
        name.setText(task.getName());
        details.setText(task.getDetails());

        return view;
    }

    private String getTimeFormat(int hourOfDay, int minute) {
        return hourOfDay + ":" + (minute < 10 ? ("0" + minute) : minute);
    }
}
