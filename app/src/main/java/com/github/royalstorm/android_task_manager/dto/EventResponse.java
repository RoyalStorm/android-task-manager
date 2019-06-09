package com.github.royalstorm.android_task_manager.dto;

import com.github.royalstorm.android_task_manager.dao.Event;
import com.google.gson.annotations.Expose;

public class EventResponse {
    @Expose
    private int count;
    @Expose
    private Event[] data;
    @Expose
    private String message;
    @Expose
    private int offset;
    @Expose
    private int status;
    @Expose
    private boolean success;

    public int getCount() {
        return count;
    }

    public Event[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getOffset() {
        return offset;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }
}
