package com.example.qlda.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
    public void setTaskId(int taskId) {
        this.taskId = taskId;
        setUpdatedDate((new Date()).toString());
    }

    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
        setUpdatedDate((new Date()).toString());
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
        setUpdatedDate((new Date()).toString());
    }

    public int getAssignedTo() { return assignedTo; }
    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
        setUpdatedDate((new Date()).toString());
    }

    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
        setUpdatedDate((new Date()).toString());
    }



    public int getReporter() {
        return reporter;
    }

    public void setReporter(int reporter) {
        this.reporter = reporter;
        setUpdatedDate((new Date()).toString());
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
        setUpdatedDate((new Date()).toString());
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        setUpdatedDate((new Date()).toString());
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
        setUpdatedDate((new Date()).toString());
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    private void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public LocalDate getCreateDateAsLocalDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(createDate, formatter);
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
        setUpdatedDate((new Date()).toString());
    }
}
