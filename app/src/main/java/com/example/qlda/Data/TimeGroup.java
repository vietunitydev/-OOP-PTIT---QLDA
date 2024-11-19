package com.example.qlda.Data;

import java.util.List;

public class TimeGroup {
    private final String timeLabel;
    private final List<Task> task;

    public TimeGroup(String timeLabel, List<Task> task) {
        this.timeLabel = timeLabel;
        this.task = task;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public List<Task> getProjects() {
        return task;
    }
}
