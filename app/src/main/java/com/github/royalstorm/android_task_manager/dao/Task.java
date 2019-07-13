package com.github.royalstorm.android_task_manager.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {
    /*Readonly fields*/
    @SerializedName("created_at")
    private Long createdAt;
    @SerializedName("updated_at")
    private Long updatedAt;
    @Expose
    private Long eventId;

    @Expose
    @SerializedName("deadline_at")
    private Long deadlineAt;
    @Expose
    private String details;
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    @SerializedName("parent_id")
    private Long parentId;
    @Expose
    private String status;

    /*Default constructor*/
    public Task() {
    }

    public Task(Long deadlineAt, String details, Long id, String name, Long parentId, String status) {
        this.deadlineAt = deadlineAt;
        this.details = details;
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.status = status;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public Long getEventId() {
        return eventId;
    }

    public Long getDeadlineAt() {
        return deadlineAt;
    }

    public void setDeadlineAt(Long deadlineAt) {
        this.deadlineAt = deadlineAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
