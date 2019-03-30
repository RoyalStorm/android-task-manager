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
import com.github.royalstorm.android_task_manager.dao.Event;

import java.util.List;

public class DayEventsAdapter extends ArrayAdapter<Event> {

    private List<Event> eventsList;

    private Context context;

    private int resource;

    public DayEventsAdapter(Context context, int resource, List<Event> eventsList) {
        super(context, resource, eventsList);
        this.context = context;
        this.resource = resource;
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        TextView createdTime = view.findViewById(R.id.createdTime);
        TextView endTime = view.findViewById(R.id.endTime);
        TextView name = view.findViewById(R.id.name);
        TextView details = view.findViewById(R.id.details);

        Event event = eventsList.get(position);

        createdTime.setText(event.getBeginTime());
        endTime.setText(event.getEndTime());
        name.setText(event.getEventTitle());
        details.setText("Описание");

        return view;
    }
}
