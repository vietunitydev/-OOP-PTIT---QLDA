package com.example.qlda.Data;

import java.io.Serializable;

public class Project implements Serializable {
    private int projectId;
    private String projectName;
    private String description;
    private String startDate;
    private String endDate;
    private String status; // Planned, Ongoing, Completed
    private int avatarID;

    // Constructor
    public Project(){

    }
    public Project(int projectId, String projectName, String description, String startDate, String endDate, String status, int avatarID) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.avatarID = avatarID;
    }

    // Getters and Setters
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getAvatarID() {
        return avatarID;
    }

    public void setAvatarID(int avatarID) {
        this.avatarID = avatarID;
    }

    public Project Clone(){
        return new Project(projectId, projectName, description, startDate, endDate, status, avatarID);
    }
    public void SetClone(Project other){
        this.projectId = other.projectId;
        this.projectName = other.projectName;
        this.description = other.description;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.status = other.status;
        this.avatarID = other.avatarID;
    }
}