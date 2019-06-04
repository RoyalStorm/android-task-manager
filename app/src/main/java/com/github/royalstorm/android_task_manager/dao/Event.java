package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;

public class Event {
    @Expose
    private Long createdAt;
    @Expose
    private String details;

    private int id;

    @Expose
    private String location;
    @Expose
    private String name;

    @Expose
    private int ownerId;

    @Expose
    private EventPattern[] patterns;

    @Expose
    private String status;
    @Expose
    private Long updatedAt;

    public Event() {
    }

    public Event(String details, String location, String name, int ownerId, EventPattern[] patterns, String status) {
        this.details = details;
        this.location = location;
        this.name = name;
        this.ownerId = ownerId;
        this.patterns = patterns;
        this.status = status;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getId() {
        return id;
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

    public EventPattern[] getPatterns() {
        return patterns;
    }

    public void setPatterns(EventPattern[] patterns) {
        this.patterns = patterns;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }
}
