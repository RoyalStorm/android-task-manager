package com.github.royalstorm.android_task_manager.dao;

import java.io.Serializable;

public class Event implements Serializable {

    private int id;

    private String owner;
    private String eventTitle;
    private String date;
    private String beginTime;
    private String endTime;

    /*
     * Constructor with id need only for mock service.
     * In true service, server itself assigns an id to each event.
     */
    public Event(int id, String eventTitle) {
        this.id = id;
        this.eventTitle = eventTitle;
    }

    public Event(int id, String owner, String eventTitle, String date, String beginTime, String endTime) {
        this.id = id;
        this.owner = owner;
        this.eventTitle = eventTitle;
        this.date = date;
        this.beginTime = beginTime;
        this.endTime = endTime;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
