package com.example.qlda.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Data {
    // Static instance (Singleton)
    private static Data instance;

    // Instance fields
    private List<User> users;
    private List<Project> projects;
    private List<Task> tasks;
    private List<Comment> comments;

    // Private constructor
    private Data() {
        users = new ArrayList<>();
        projects = new ArrayList<>();
        tasks = new ArrayList<>();
        comments = new ArrayList<>();

        initializeSampleData();
    }

    // Public method to access the singleton instance
    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    // Initialize sample data
    private void initializeSampleData() {
        // Users
        for (int i = 1; i <= 10; i++) {
            users.add(new User(i, "User " + i, "user" + i + "@example.com", "password" + i,
                    i <= 3 ? "Admin" : (i <= 7 ? "Member" : "Client"), "2024-01-01"));
        }

        // Projects
        for (int i = 1; i <= 15; i++) {
            projects.add(new Project(i, "Project " + i, "Description for Project " + i,
                    "2024-01-" + (i % 28 + 1), "2024-02-" + (i % 28 + 1),
                    i % 2 == 0 ? "Ongoing" : "Planned", (i % 10) + 1));
        }

        // Tasks
        for (int i = 1; i <= 15; i++) {
            String date = (i % 28 + 1 >= 10) ? String.valueOf(i % 28 + 1) : "0" + String.valueOf((i % 28 + 1));
            tasks.add(new Task(i, "Task " + i, "Details for Task " + i,
                    (i % 10) + 1, (i % 15) + 1,
                    i % 3 == 0 ? "High" : (i % 3 == 1 ? "Medium" : "Low"),
                    i % 2 == 0 ? "Done" : "InProgress", "2024-03-" + date));
        }

        // Comments
        for (int i = 1; i <= 100; i++) {
            comments.add(new Comment(i, (i % 50) + 1, (i % 10) + 1,
                    "Comment " + i + " on Task " + ((i % 50) + 1), "2024-04-" + (i % 28 + 1)));
        }
    }

    // CRUD Methods

    public User getUserById(int userId) {
        return users.stream().filter(user -> user.getUserId() == userId).findFirst().orElse(null);
    }

    public List<Task> getTasksByProjectId(int projectId) {
        return tasks.stream().filter(task -> task.getProjectId() == projectId).collect(Collectors.toList());
    }

    public List<Task> getListTask(){
        return this.tasks;
    }

    public List<Comment> getCommentsByTaskId(int taskId) {
        return comments.stream().filter(comment -> comment.getTaskId() == taskId).collect(Collectors.toList());
    }

    public boolean updateTaskStatus(int taskId, String newStatus) {
        Task task = tasks.stream().filter(t -> t.getTaskId() == taskId).findFirst().orElse(null);
        if (task != null) {
            task.setStatus(newStatus);
            return true;
        }
        return false;
    }

    public void deleteIssueById(int taskId){
        tasks.removeIf(task -> task.getTaskId() == taskId);
    }

    public boolean deleteProjectById(int projectId) {
        boolean removed = projects.removeIf(project -> project.getProjectId() == projectId);
        if (removed) {
            tasks.removeIf(task -> task.getProjectId() == projectId);
        }
        return removed;
    }

    public void addCommentToTask(int taskId, int userId, String content, String createdAt) {
        int newCommentId = comments.size() + 1;
        comments.add(new Comment(newCommentId, taskId, userId, content, createdAt));
    }

    public List<Task> getTasksAssignedToUser(int userId) {
        return tasks.stream().filter(task -> task.getAssignedTo() == userId).collect(Collectors.toList());
    }

    public Project getProjectById(int projectId) {
        return projects.stream()
                .filter(project -> project.getProjectId() == projectId)
                .findFirst()
                .orElse(null);
    }

    public Task getIssueById(int taskId) {
        return tasks.stream()
                .filter(task -> task.getTaskId() == taskId)
                .findFirst()
                .orElse(null);
    }

    public void printAllData() {
        System.out.println("Users:");
        users.forEach(System.out::println);

        System.out.println("\nProjects:");
        projects.forEach(System.out::println);

        System.out.println("\nTasks:");
        tasks.forEach(System.out::println);

        System.out.println("\nComments:");
        comments.forEach(System.out::println);
    }
}


