package com.github.royalstorm.android_task_manager.repository;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.github.royalstorm.android_task_manager.dao.EventInstance;
import com.github.royalstorm.android_task_manager.dao.EventPattern;

import java.util.List;

public interface CachedEventRepository {
    //Find all methods
    List<EventInstance> findAllEventInstances();
    List<Event> findAllEvents();
    List<EventPattern> findAllPatterns();

    //Find by id
    EventInstance findEventInstanceById(Long id);
    Event findEventById(Long id);
    EventPattern findEventPatternById(Long id);

    //Find by fields
    List<Event> findByYearAndMonthAndDay(int year, int month, int day);
    List<Event> findByYearAndMonthAndDayAndHoursAndMinutes(int year, int month, int day, int hours, int minutes);

    //Add methods
    void addEventInstance(EventInstance eventInstance);
    void addEvent(Event event);
    void addEventPattern(EventPattern eventPattern);

    //Update methods
    void updateEventInstance(Long id, EventInstance eventInstance);
    void updateEvent(Long id, Event event);
    void updateEventPattern(Long id, EventPattern eventPattern);

    //Delete methods
    void deleteEventInstance(Long id);
    void deleteEvent(Long id);
    void deleteEventPattern(Long id);
}
