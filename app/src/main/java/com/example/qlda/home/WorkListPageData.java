package com.example.qlda.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkListPageData implements Serializable {
    private String id;
    private String tableId;
    private String title;
    private List<String> elementIds;
    private List<ElementData> elements;
    private Date createdAt;
    private Date updatedAt;
    private boolean destroy;

    public WorkListPageData(){
        this.id = "";
        this.tableId = "";
        this.title = "";
        this.elementIds = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.destroy = false;
    }

    // Constructor
    public WorkListPageData(String id, String tableId, String title, Date createdAt) {
        this.id = id;
        this.tableId = tableId;
        this.title = title;
        this.elementIds = new ArrayList<>(0);
        this.elements = new ArrayList<>(0);
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

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getElementIds() {
        return elementIds;
    }

    public void setElementIds(List<String> elementIds) {
        this.elementIds = elementIds;
    }

    public List<ElementData> getElements() {
        return elements;
    }

    public void setElements(List<ElementData> elements) {
        this.elements = elements;
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

    // Method to add Element
    public void addElement(ElementData element) {
        this.elements.add(element);
        this.elementIds.add(element.getId());
        this.updatedAt = new Date();
    }
}


