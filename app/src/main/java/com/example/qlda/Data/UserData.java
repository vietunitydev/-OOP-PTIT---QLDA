package com.example.qlda.Data;

import java.util.Date;
import java.util.ArrayList;

public class UserData {
    private String id;
    private String email;
    private String displayName;
    private String avatar;
    private String role;
    private Date createAt;
    private Date updatedAt;
    private ArrayList<String> ownedId;
    private ArrayList<String> managedId;

    // Constructor
    UserData(){
        this.id = "";
        this.email = "";
        this.displayName = "";
        this.avatar = "";
        this.role = "";
        this.createAt = new Date();
        this.updatedAt = new Date();
        this.ownedId = new ArrayList<>();
        this.managedId = new ArrayList<>();
    }
    public UserData(String id, String email, String displayName, String avatar, String role, Date createAt, Date updatedAt) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.avatar = avatar;
        this.role = role;
        this.createAt = createAt;
        this.updatedAt = updatedAt;
        this.ownedId = new ArrayList<>();
        this.managedId = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ArrayList<String> getOwnedId() {
        return ownedId;
    }

    public void setOwnedId(ArrayList<String> ownedId) {
        this.ownedId = ownedId;
    }

    public ArrayList<String> getManagedId() {
        return managedId;
    }

    public void setManagedId(ArrayList<String> managedId) {
        this.managedId = managedId;
    }

    public void addOwnedProject(String projectId) {
        if (!ownedId.contains(projectId)) {
            ownedId.add(projectId);
        }
    }

    public void addManagedProject(String projectId) {
        if (!managedId.contains(projectId)) {
            managedId.add(projectId);
        }
    }
}