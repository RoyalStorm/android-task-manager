package com.github.royalstorm.android_task_manager.dto;

import com.github.royalstorm.android_task_manager.dao.Task;
import com.google.gson.annotations.Expose;

public class TaskResponse {
    @Expose
    private int count;
    @Expose
    private Task[] data;
    @Expose
    private String message;
    @Expose
    private Long offset;
    @Expose
    private int status;
    @Expose
    private boolean success;

    public int getCount() {
        return count;
    }

    public Task[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public Long getOffset() {
        return offset;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSuccess() {
        return success;
    }
}
