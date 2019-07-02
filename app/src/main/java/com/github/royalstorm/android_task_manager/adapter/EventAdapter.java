package com.github.royalstorm.android_task_manager.adapter;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private List<EventInstance> eventInstances;

    private OnEventListener onEventListener;

    private View view;

    public EventAdapter(List<EventInstance> eventInstances, OnEventListener onEventListener) {
        this.eventInstances = eventInstances;
        this.onEventListener = onEventListener;
    }

    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView eventStart;
        TextView eventEnd;
        TextView eventName;
        TextView eventOwner;
        TextView eventDetails;

        OnEventListener onEventListener;

        EventViewHolder(View itemView, OnEventListener onEventListener) {
            super(itemView);

            eventStart = itemView.findViewById(R.id.event_start);
            eventEnd = itemView.findViewById(R.id.event_end);
            eventName = itemView.findViewById(R.id.event_name);
            eventOwner = itemView.findViewById(R.id.event_owner);
            eventDetails = itemView.findViewById(R.id.event_details);

            this.onEventListener = onEventListener;

            itemView.setOnClickListener(this);
        }

        void bind(EventInstance eventInstance) {
            eventStart.setText(timestampToDate(eventInstance.getStartedAt()));
            eventEnd.setText(timestampToDate(eventInstance.getEndedAt()));

            retrofitClient.getEventRepository().getEventsById(new Long[]{eventInstance.getEventId()}).enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    if (response.isSuccessful() && response.body().getCount() != 0) {
                        /*Get by 0 index, cause in data array only 1 object*/
                        if (isPortraitOrientation()) {
                            eventName.setText(response.body().getData()[0].getName().length() < 29 ? response.body().getData()[0].getName() : (response.body().getData()[0].getName().substring(0, 27) + "..."));
                            eventOwner.setText(response.body().getData()[0].getOwnerId().toString());
                            eventDetails.setText(response.body().getData()[0].getDetails().length() < 29 ? response.body().getData()[0].getDetails() : (response.body().getData()[0].getDetails().substring(0, 27) + "..."));
                        } else {
                            eventName.setText(response.body().getData()[0].getName().length() < 58 ? response.body().getData()[0].getName() : (response.body().getData()[0].getName().substring(0, 58) + "..."));
                            eventOwner.setText(response.body().getData()[0].getOwnerId().toString());
                            eventDetails.setText(response.body().getData()[0].getDetails().length() < 58 ? response.body().getData()[0].getDetails() : (response.body().getData()[0].getDetails().substring(0, 58) + "..."));
                        }
                    }
                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {

                }
            });
        }

        private boolean isPortraitOrientation() {
            return view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        }

        private String timestampToDate(Long millis) {
            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(millis);
        }

        @Override
        public void onClick(View view) {
            onEventListener.onEventClick(getAdapterPosition());
        }
    }

    public interface OnEventListener {
        void onEventClick(int position);
    }

    public void setItems(Collection<EventInstance> eventInstances) {
        clearItems();
        this.eventInstances.addAll(eventInstances);
        notifyDataSetChanged();
    }

    private void clearItems() {
        eventInstances.clear();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_item, viewGroup, false);
        return new EventViewHolder(view, onEventListener);
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
