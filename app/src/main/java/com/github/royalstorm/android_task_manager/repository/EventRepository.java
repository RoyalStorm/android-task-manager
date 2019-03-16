package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Event;

import java.util.List;

public interface EventRepository {

    Event getOne(int id);

    List<Event> getAll();

    void add(Event event);

    void update(int id, Event event);

    void delete(int id);
}
