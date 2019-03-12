package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.exception.NotFoundException;
import com.github.royalstorm.android_task_manager.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;

public class MockUpEventService implements EventRepository {

    private List<Event> events = new ArrayList<>();
    private Long counter = 0L;

    private Event getEvent(Long id) {
        for (Event event : events)
            if (event.getId().equals(id))
                return event;

        throw new NotFoundException("Object not found");
    }

    @Override
    public Event getOne(Long id) {
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
    public Event update(Long id, Event event) {
        Event eventFromMockUp = getEvent(id);

        eventFromMockUp = event;
        eventFromMockUp.setId(id);

        return eventFromMockUp;
    }

    @Override
    public void delete(Long id) {
        events.remove(getEvent(id));
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }
}
