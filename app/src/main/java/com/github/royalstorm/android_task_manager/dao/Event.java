package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;

public class Event {
    /*Readonly fields*/
    private int id;
    private Long createdAt;
    private Long updatedAt;
    private Long startedAt;
    private Long endedAt;

    @Expose
    private String details;
    @Expose
    private String location;
    @Expose
    private String name;
    @Expose
    private int ownerId;
    @Expose
    private String status;

    /*Default constructor*/
    public Event() {
    }

    public Event(String details, String location, String name, int ownerId, String status) {
        this.details = details;
        this.location = location;
        this.name = name;
        this.ownerId = ownerId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public Long getEndedAt() {
        return endedAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
