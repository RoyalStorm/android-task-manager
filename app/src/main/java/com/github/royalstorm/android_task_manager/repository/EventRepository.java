package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Event;

import java.util.List;

public interface EventRepository {

    Event getOne(Long id);

    List<Event> getAll();

    void add(Event event);

    Event update(Long id, Event event);

    void delete(Long id);
}
