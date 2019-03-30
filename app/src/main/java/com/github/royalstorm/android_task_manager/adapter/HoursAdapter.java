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

public class HoursAdapter extends ArrayAdapter<String> {

    private String[] hoursList;

    private Context context;

    private int resource;

    public HoursAdapter(Context context, int resource, String[] hoursList) {
        super(context, resource, hoursList);
        this.context = context;
        this.resource = resource;
        this.hoursList = hoursList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(resource, null, false);

        TextView hour = view.findViewById(R.id.hour);

        String time = hoursList[position];

        hour.setText(time);

        return view;
    }
}
