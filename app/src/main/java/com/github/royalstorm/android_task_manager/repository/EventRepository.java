package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Event;

import java.util.List;

public interface EventRepository {

    List<Event> findAll();

    List<Event> findByDate(String date);

    List<Event> findByDateAndTime(String date, String time);

    void create(Event event);

    void update(int id, Event event);

    void delete(int id);
}
