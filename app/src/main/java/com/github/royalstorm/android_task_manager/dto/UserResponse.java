package com.github.royalstorm.android_task_manager.dto;

import com.google.gson.annotations.Expose;

public class UserResponse {

    @Expose
    private String id;
    @Expose
    private String username;
    @Expose
    private String photo;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoto() {
        return photo;
    }
}
