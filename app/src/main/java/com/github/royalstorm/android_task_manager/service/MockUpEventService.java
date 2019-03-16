package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.exception.NotFoundException;
import com.github.royalstorm.android_task_manager.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

public class MockUpEventService implements EventRepository {

    private List<Event> events = new ArrayList<>();
    private int counter = 0;

    private Event getEvent(int id) {
        for (Event event : events)
            if (event.getId() == id)
                return event;

        throw new NotFoundException("Object not found");
    }

    @Override
    public Event getOne(int id) {
        return getEvent(id);
    }

    @Override
    public List<Event> getAll() {
        return events;
    }

    @Override
    public void add(Event event) {
        events.add(event);
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
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
