package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;

public class Event {
    /*Readonly fields*/
    @Expose
    private Long id;
    @Expose
    private Long ownerId;
    @Expose
    private Long createdAt;
    @Expose
    private Long updatedAt;

    @Expose
    private String details;
    @Expose
    private String location;
    @Expose
    private String name;
    @Expose
    private String status;

    /*Default constructor*/
    public Event() {
    }

    public Event(String details, String location, String name, String status) {
        this.details = details;
        this.location = location;
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
