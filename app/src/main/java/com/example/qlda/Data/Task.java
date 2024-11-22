package com.example.qlda.Data;

import java.io.Serializable;

public class Task implements Serializable  {
    private int taskId;
    private String taskName;
    private String description;
    private int assignedTo;
    private int reporter;
    private int projectId;
    private TaskType taskType; // story, bug, task
    private Priority priority; // Low, Medium, High
    private StatusType status; // Todo, InProgress, Done
    private String createDate;
    private String updatedDate;

    public Task() {

    }
    // Constructor
    public Task(int taskId, String taskName, String description, int reporter, int assignedTo, int projectId, TaskType taskType, Priority priority, StatusType status, String createDate, String dueDate) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.assignedTo = assignedTo;
        this.reporter = reporter;
        this.projectId = projectId;
        this.taskType = taskType;
        this.priority = priority;
        this.status = status;
        this.createDate = createDate;
        this.updatedDate = dueDate;
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



    public int getReporter() {
        return reporter;
    }

    public void setReporter(int reporter) {
        this.reporter = reporter;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
