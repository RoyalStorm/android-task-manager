package com.github.royalstorm.android_task_manager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.EventInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<EventInstance> eventInstances = new ArrayList<>();

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventStart;
        TextView eventEnd;
        TextView eventName;
        TextView eventOwner;
        TextView eventDetails;

        EventViewHolder(View itemView) {
            super(itemView);

            eventStart = itemView.findViewById(R.id.event_start);
            eventEnd = itemView.findViewById(R.id.event_end);
            eventName = itemView.findViewById(R.id.event_name);
            eventOwner = itemView.findViewById(R.id.event_owner);
            eventDetails = itemView.findViewById(R.id.event_details);
        }

        void bind(EventInstance eventInstance) {
            eventStart.setText("12:00");
            eventEnd.setText("21:30");
            eventName.setText("Шашлыки");
            eventOwner.setText("Колот Юрий");
            eventDetails.setText("Описание происходящего события");
        }

        private GregorianCalendar timestampToDate(Long millis) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(millis);

            return calendar;
        }
    }

    public void setItems(Collection<EventInstance> eventInstances) {
        this.eventInstances.addAll(eventInstances);
        notifyDataSetChanged();
    }

    public void clearItems() {
        eventInstances.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item, viewGroup, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i) {
        eventViewHolder.bind(eventInstances.get(i));
    }

    @Override
    public int getItemCount() {
        return eventInstances.size();
    }
}
