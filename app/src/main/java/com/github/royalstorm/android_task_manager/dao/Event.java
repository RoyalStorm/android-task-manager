package com.github.royalstorm.android_task_manager.dao;

public class Event {
    private String createdAt;
    private String details;

    private int id;

    private String location;
    private String name;

    private int ownerId;

    private EventPattern[] patterns;

    private String status;
    private String updatedAt;

    public Event(String createdAt, String details, String location, String name, int ownerId, EventPattern[] patterns, String status, String updatedAt) {
        this.createdAt = createdAt;
        this.details = details;
        this.location = location;
        this.name = name;
        this.ownerId = ownerId;
        this.patterns = patterns;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public void setId(int id) {
        this.id = id;
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

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
