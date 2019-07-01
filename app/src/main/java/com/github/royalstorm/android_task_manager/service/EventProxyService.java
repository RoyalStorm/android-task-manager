package com.github.royalstorm.android_task_manager.service;

import android.util.Log;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dao.EventPattern;
import com.github.royalstorm.android_task_manager.dto.EventInstanceResponse;
import com.github.royalstorm.android_task_manager.dto.EventResponse;
import com.github.royalstorm.android_task_manager.repository.CachedEventRepository;
import com.github.royalstorm.android_task_manager.shared.RetrofitClient;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventProxyService implements CachedEventRepository {

    private RetrofitClient retrofitClient = RetrofitClient.getInstance();

    private static List<EventInstance> eventInstances = new ArrayList<>();
    private static List<Event> events = new ArrayList<>();
    private static List<EventPattern> eventPatterns = new ArrayList<>();
    private final static List<Event> foundEvents = new ArrayList<>();

    private EventService eventService = new EventService();

    public EventProxyService() {
        retrofitClient.getEventRepository().getAllEventInstances().enqueue(new Callback<EventInstanceResponse>() {
            @Override
            public void onResponse(Call<EventInstanceResponse> call, Response<EventInstanceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventInstances.addAll(Arrays.asList(response.body().getData()));
                    EventBus.getDefault().post(response.body());
                }
            }

            @Override
            public void onFailure(Call<EventInstanceResponse> call, Throwable t) {
                Log.d("___GET ALL INSTANCES", t.toString());
            }
        });

        retrofitClient.getEventRepository().getAllEvents().enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if (response.isSuccessful() && response.body() != null)
                    events.addAll(Arrays.asList(response.body().getData()));
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Log.d("___GET ALL EVENTS", t.toString());
            }
        });
    }

    @Override
    public List<EventInstance> findAllEventInstances() {
        return eventInstances;
    }

    @Override
    public List<Event> findAllEvents() {
        return events;
    }

    @Override
    public List<EventPattern> findAllPatterns() {
        return eventPatterns;
    }

    @Override
    public EventInstance findEventInstanceById(Long id) {
        return null;
    }

    @Override
    public Event findEventById(Long id) {
        return null;
    }

    @Override
    public EventPattern findEventPatternById(Long id) {
        return null;
    }

    @Override
    public List<Event> findByYearAndMonthAndDay(int year, int month, int day) {
        return null;
    }

    @Override
    public List<Event> findByYearAndMonthAndDayAndHoursAndMinutes(int year, int month, int day, int hours, int minutes) {
        return null;
    }

    @Override
    public void addEventInstance(EventInstance eventInstance) {
        eventInstances.add(eventInstance);
    }

    @Override
    public void addEvent(Event event) {
        events.add(event);
        eventService.save(event);
        //TODO: update cache
    }

    @Override
    public void addEventPattern(EventPattern eventPattern) {

    }

    @Override
    public void updateEventInstance(Long id, EventInstance eventInstance) {

    }

    @Override
    public void updateEvent(Long id, Event event) {

    }

    @Override
    public void updateEventPattern(Long id, EventPattern eventPattern) {

    }

    @Override
    public void deleteEventInstance(Long id) {
    }

    @Override
    public void deleteEvent(Long id) {
        events.remove(getEvent(id));
        eventService.delete(id);
        //TODO: update cache
    }

    @Override
    public void deleteEventPattern(Long id) {

    }

    public int getEventCount() {
        return events.size();
    }

    public int getPatternCount() {
        return eventPatterns.size();
    }

    public int getEventInstanceCount() {
        return eventInstances.size();
    }

    private Event getEvent(Long id) {
        for (Event event : events)
            if (event.getId().equals(id))
                return event;

        return null;
    }

    private boolean isWithinRange(GregorianCalendar begin, GregorianCalendar now, GregorianCalendar end) {
        return !(now.before(begin) || now.after(end));
    }
}
