package com.example.qlda.Data;

import java.io.Serializable;

public class Task implements Serializable  {
    private int taskId;
    private String taskName;
    private String description;
    private int assignedTo;
    private int reporter;
    private int projectId;
    private String taskType; // story, bug, task
    private String priority; // Low, Medium, High
    private String status; // Todo, InProgress, Done
    private String dueDate;

    public Task() {

    }
    // Constructor
    public Task(int taskId, String taskName, String description, int assignedTo, int projectId, String taskType, String priority, String status, String dueDate) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.assignedTo = assignedTo;
        this.reporter = 3;
        this.projectId = projectId;
        this.taskType = taskType;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
    }

    // Getters and Setters
    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getAssignedTo() { return assignedTo; }
    public void setAssignedTo(int assignedTo) { this.assignedTo = assignedTo; }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getReporter() {
        return reporter;
    }

    public void setReporter(int reporter) {
        this.reporter = reporter;
    }
}
