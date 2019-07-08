package com.github.royalstorm.android_task_manager.shared;

public interface Frequency {
    static final String NEVER = null;
    static final String DAILY = "FREQ=DAILY;INTERVAL=1";
    static final String WEEKLY = "FREQ=WEEKLY;INTERVAL=1";
    static final String MONTHLY = "FREQ=MONTHLY;INTERVAL=1";
    static final String YEARLY = "FREQ=YEARLY;INTERVAL=1";
}
