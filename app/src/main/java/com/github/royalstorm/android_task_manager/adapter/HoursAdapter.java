package com.github.royalstorm.android_task_manager.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Event;

import java.util.List;

public class HoursAdapter extends ArrayAdapter<String> {

    private List<Event> events;

    private String[] hoursList;

    private Context context;

    private int resource;

    public HoursAdapter(Context context, int resource, String[] hoursList, List<Event> events) {
        super(context, resource, hoursList);
        this.context = context;
        this.resource = resource;
        this.hoursList = hoursList;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        TextView hour = view.findViewById(R.id.hour);
        String time = hoursList[position];
        hour.setText(time);

        LinearLayout linearLayout = view.findViewById(R.id.eventsContainer);

        TextView textView;
        for (Event event : events) {
            textView = new TextView(getContext());
            textView.setTextColor(Color.WHITE);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            if (event.getBeginTime().equals(position + ":00")) {
                textView.setBackground(textView.getContext().getDrawable(R.drawable.side_nav_bar));
                textView.setText(event.getEventTitle());

                linearLayout.addView(textView);
            }
        }

        return view;
    }
}
