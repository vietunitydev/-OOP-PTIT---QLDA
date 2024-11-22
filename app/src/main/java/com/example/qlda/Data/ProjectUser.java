package com.example.qlda.Data;

public class ProjectUser {
    private int projectID;
    private int userID;
    private String role; // Admin, member

    public ProjectUser(int projectID, int userID, String role){
        this.projectID = projectID;
        this.userID = userID;
        this.role = role;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
