package com.github.royalstorm.android_task_manager.dao;

import java.io.Serializable;

public class Event implements Serializable {

    private Long id;

    private String eventTitle;

    //Deprecated, will be removed
    public Event(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Event(Long id, String eventTitle) {
        this.id = id;
        this.eventTitle = eventTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}
