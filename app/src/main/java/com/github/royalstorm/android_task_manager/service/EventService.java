package com.github.royalstorm.android_task_manager.service;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.repository.EventRepository;

import java.util.List;

public class EventService implements EventRepository {

    @Override
    public Event findById(int id) {
        return null;
    }

    @Override
    public List<Event> findAll() {
        return null;
    }

    @Override
    public List<Event> findByDate(String date) {
        return null;
    }

    @Override
    public List<Event> findByDateAndTime(String date, String time) {
        return null;
    }

    @Override
    public void create(Event event) {

    }

    @Override
    public void update(int id, Event event) {

    }

    @Override
    public void delete(int id) {

    }
}
