package com.example.qlda.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ElementData implements Serializable {
    private String id;
    private String workListPageID;
    private String tableID;
    private String title;
    private String description;
    private List<Comment> comments;  // Danh sách các comment
    private Date createdAt;
    private Date updatedAt;
    private boolean destroy;

    public static class Comment {
        private String userID;
        private String userEmail;
        private String userAvatar;
        private String userDisplayName;
        private String content;
        private Date createdAt;

        public Comment(String userID, String userEmail, String userAvatar, String userDisplayName, String content, Date createdAt) {
            this.userID = userID;
            this.userEmail = userEmail;
            this.userAvatar = userAvatar;
            this.userDisplayName = userDisplayName;
            this.content = content;
            this.createdAt = createdAt;
        }

        // Getters
        public String getUserID() {
            return userID;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public String getUserAvatar() {
            return userAvatar;
        }

        public String getUserDisplayName() {
            return userDisplayName;
        }

        public String getContent() {
            return content;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        // Setters
        public void setUserID(String userID) {
            this.userID = userID;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public void setUserAvatar(String userAvatar) {
            this.userAvatar = userAvatar;
        }

        public void setUserDisplayName(String userDisplayName) {
            this.userDisplayName = userDisplayName;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
    }

    public ElementData(){
        this.id = "";
        this.workListPageID = "";
        this.tableID = "";
        this.title = "";
        this.description = "";
        this.comments = new ArrayList<>();
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.destroy = false;
    }

    public ElementData(String id, String wlId, String tID, String tit, String description, List<Comment> cmt, Date create, Date update, Boolean des){
        this.id = id;
        this.workListPageID = wlId;
        this.tableID = tID;
        this.title = tit;
        this.description = description;
        this.comments = cmt;
        this.createdAt = create;
        this.updatedAt = update;
        this.destroy = des;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getWorkListPageID() {
        return workListPageID;
    }

    public String getTableID() {
        return tableID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDestroy() {
        return destroy;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setWorkListPageID(String workListPageID) {
        this.workListPageID = workListPageID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComments(Comment com){
        this.comments.add(com);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDestroy(boolean destroy) {
        this.destroy = destroy;
    }
}

