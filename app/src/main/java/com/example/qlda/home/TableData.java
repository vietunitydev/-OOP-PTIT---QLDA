package com.example.qlda.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableData implements Serializable {
    private String id;
    private String title;
    private String color;
    private List<String> workListPageIds;
    private List<WorkListPageData> workListPages;
    private Date createdAt;
    private Date updatedAt;
    private boolean destroy;

    public TableData(){
        this.id = "";
        this.title = "";
        this.color = "";
        this.workListPageIds = new ArrayList<>();
        this.workListPages = new ArrayList<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.destroy = false;
    }

    // Constructor
    public TableData(String id, String title, String color, Date createdAt) {
        this.id = id;
        this.title = title;
        this.color = color;
        this.workListPageIds = new ArrayList<>();
        this.workListPages = new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = new Date();
        this.destroy = false;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<String> getWorkListPageIds() {
        return workListPageIds;
    }

    public void setWorkListPageIds(List<String> workListPageIds) {
        this.workListPageIds = workListPageIds;
    }

    public List<WorkListPageData> getWorkListPages() {
        return workListPages;
    }

    public void setWorkListPages(List<WorkListPageData> workListPages) {
        this.workListPages = workListPages;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDestroy() {
        return destroy;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }

    // Method to add WorkListPage
    public void addWorkListPage(WorkListPageData workListPage) {
        this.workListPages.add(workListPage);
        this.workListPageIds.add(workListPage.getId());
        this.updatedAt = new Date();
    }
}
