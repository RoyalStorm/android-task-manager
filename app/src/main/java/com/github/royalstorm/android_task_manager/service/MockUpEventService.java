package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

public class MockUpEventService implements EventRepository {

    private static List<Event> events = new ArrayList<>();
    private List<Event> foundEvents;

    private static int counter = 0;

    private Event getEvent(int id) {
        for (Event event : events)
            if (event.getId() == id)
                return event;

        return null;
    }

    @Override
    public List<Event> findAll() {
        return events;
    }

    @Override
    public List<Event> findByDate(String date) {
        foundEvents = new ArrayList<>();

        for (Event event : events)
            if (event.getDate().equals(date))
                foundEvents.add(event);

        return foundEvents;
    }

    @Override
    public List<Event> findByDateAndTime(String date, String time) {
        foundEvents = new ArrayList<>();

        for (Event event : events)
            if (event.getDate().equals(date) && event.getBeginTime().equals(time))
                foundEvents.add(event);

        return foundEvents;
    }

    @Override
    public void create(Event event) {
        events.add(event);

        ++counter;
    }

    @Override
    public void update(int id, Event event) {
        Event eventFromDB = getEvent(id);

        eventFromDB = event;
        eventFromDB.setId(id);

        events.set(id, eventFromDB);
    }

    @Override
    public void delete(int id) {
        events.remove(getEvent(id));

        --counter;
    }

    public static int getCounter() {
        return counter;
    }
}
