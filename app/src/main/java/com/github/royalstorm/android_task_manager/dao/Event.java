package com.github.royalstorm.android_task_manager.dao;

import java.io.Serializable;

public class Event implements Serializable {

    private String eventTitle;

    public Event(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }
}
