package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Event {
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("details")
    @Expose
    private String details;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("owner_id")
    @Expose
    private int ownerId;

    @SerializedName("patterns")
    @Expose
    private EventPattern[] patterns;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Event() {
    }

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

    @Override
    public String toString() {
        return "Event{" +
                "createdAt='" + createdAt + '\'' +
                ", details='" + details + '\'' +
                ", id=" + id +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", patterns=" + Arrays.toString(patterns) +
                ", status='" + status + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
