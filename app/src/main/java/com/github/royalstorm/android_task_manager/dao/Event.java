package com.github.royalstorm.android_task_manager.dao;

import java.io.Serializable;

public class Event implements Serializable {

    private int id;

    private String eventTitle;

    /*
     * Constructor with id need only for mock service.
     * In true service, server itself assigns an id to each event.
     */
    public Event(int id, String eventTitle) {
        this.id = id;
        this.eventTitle = eventTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}
