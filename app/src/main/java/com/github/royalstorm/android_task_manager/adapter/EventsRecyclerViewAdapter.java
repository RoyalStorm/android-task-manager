package com.github.royalstorm.android_task_manager.adapter;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.royalstorm.android_task_manager.R;
import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.EventViewHolder> {

    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private FirebaseAuth firebaseAuth;
    private String userToken;

    private List<EventInstance> eventInstances;

    private OnEventListener onEventListener;

    private View view;

    private static final int MAX_LENGTH = 30;
    private static final int MAX_LENGTH_WITH_ELLIPSIS = 27;

    public EventsRecyclerViewAdapter(List<EventInstance> eventInstances, OnEventListener onEventListener) {
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

            firebaseAuth = FirebaseAuth.getInstance();
            userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();

            this.onEventListener = onEventListener;

            itemView.setOnClickListener(this);
        }

        void bind(EventInstance eventInstance) {
            eventStart.setText(timestampToDate(eventInstance.getStartedAt()));
            eventEnd.setText(timestampToDate(eventInstance.getEndedAt()));

            if (userToken == null)
                firebaseAuth.getCurrentUser().getIdToken(true).addOnCompleteListener(task -> {
                    userToken = task.getResult().getToken();
                    getEventRequest(eventInstance, userToken);
                });
            else {
                userToken = firebaseAuth.getCurrentUser().getIdToken(false).getResult().getToken();
                getEventRequest(eventInstance, userToken);
            }
        }

        @Override
        public void onClick(View view) {
            onEventListener.onEventClick(getAdapterPosition());
        }

        private boolean isPortraitOrientation() {
            return view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        }

        private String timestampToDate(Long millis) {
            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(millis);
        }

        private void getEventRequest(EventInstance eventInstance, String userToken) {
            retrofitClient.getEventsRepository().getEventsById(new Long[]{eventInstance.getEventId()}, userToken).enqueue(new Callback<EventResponse>() {
                @Override
                public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                    if (response.isSuccessful() && response.body().getCount() != 0) {
                        /*Get by 0 index, cause in data array only 1 object*/
                        Event event = response.body().getData()[0];

                        if (isPortraitOrientation()) {
                            eventName.setText(event.getName().length() < MAX_LENGTH ? event.getName() : (event.getName().substring(0, MAX_LENGTH_WITH_ELLIPSIS) + "..."));
                            eventOwner.setText(event.getOwnerId());
                            eventDetails.setText(event.getDetails().length() < MAX_LENGTH ? event.getDetails() : (event.getDetails().substring(0, MAX_LENGTH_WITH_ELLIPSIS) + "..."));
                        } else {
                            eventName.setText(event.getName().length() < (MAX_LENGTH * 2) ? event.getName() : (event.getName().substring(0, MAX_LENGTH_WITH_ELLIPSIS * 2) + "..."));
                            eventOwner.setText(event.getOwnerId());
                            eventDetails.setText(event.getDetails().length() < (MAX_LENGTH * 2) ? event.getDetails() : (event.getDetails().substring(0, MAX_LENGTH_WITH_ELLIPSIS * 2) + "..."));
                        }
                    }
                }

                @Override
                public void onFailure(Call<EventResponse> call, Throwable t) {

                }
            });
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

    private void clearItems() {
        eventInstances.clear();
    }
}
