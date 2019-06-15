package com.github.royalstorm.android_task_manager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<EventInstanceResponse> eventInstanceResponses = new ArrayList<>();

    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventStart;
        TextView eventEnd;
        TextView eventName;
        TextView eventOwner;
        TextView eventDetails;

        public EventViewHolder(View itemView) {
            super(itemView);

            eventStart = itemView.findViewById(R.id.event_start);
            eventEnd = itemView.findViewById(R.id.event_end);
            eventName = itemView.findViewById(R.id.event_name);
            eventOwner = itemView.findViewById(R.id.event_owner);
            eventDetails = itemView.findViewById(R.id.event_details);
        }

        public void bind(EventInstanceResponse eventInstanceResponse) {
            eventStart.setText("12:00");
            eventEnd.setText("21:30");
            eventName.setText("Шашлыки");
            eventOwner.setText("Колот Юрий");
            eventDetails.setText("Описание происходящего события");
        }
    }

    public void setItems(Collection<EventInstanceResponse> eventInstanceResponses) {
        this.eventInstanceResponses.addAll(eventInstanceResponses);
        notifyDataSetChanged();
    }

    public void clearItems() {
        eventInstanceResponses.clear();
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
        eventViewHolder.bind(eventInstanceResponses.get(i));
    }

    @Override
    public int getItemCount() {
        return eventInstanceResponses.size();
    }
}
