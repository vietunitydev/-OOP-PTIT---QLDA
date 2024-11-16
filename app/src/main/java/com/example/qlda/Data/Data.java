package com.example.qlda.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Data {
    private List<User> users;
    private List<Project> projects;
    private List<Task> tasks;
    private List<Comment> comments;

    public Data() {
        users = new ArrayList<>();
        projects = new ArrayList<>();
        tasks = new ArrayList<>();
        comments = new ArrayList<>();

        initializeSampleData();
    }

    // Initialize sample data
    private void initializeSampleData() {
        // Create Users
        users.add(new User(1, "Alice Nguyen", "alice@example.com", "12345", "Admin", "2024-01-01"));
        users.add(new User(2, "Bob Tran", "bob@example.com", "password", "Member", "2024-02-01"));
        users.add(new User(3, "Charlie Le", "charlie@example.com", "secret", "Client", "2024-03-01"));

        // Create Projects
        for (int i = 1; i <= 6; i++) {
            projects.add(new Project(
                    i,
                    "Project " + i,
                    "Description for Project " + i,
                    "2024-01-0" + i,
                    "2024-02-0" + i,
                    i % 2 == 0 ? "Ongoing" : "Planned",
                    1
            ));
        }

        // Create Tasks
        for (int i = 1; i <= 20; i++) {
            tasks.add(new Task(
                    i,
                    "Task " + i,
                    "Details for Task " + i,
                    (i % 3) + 1, // Assign to users (1, 2, 3 cyclically)
                    (i % 6) + 1, // Assign to projects (1-6 cyclically)
                    i % 2 == 0 ? "High" : "Medium",
                    i % 3 == 0 ? "Done" : "InProgress",
                    "2024-02-" + (i % 28 + 1)
            ));
        }

        // Create Comments
        for (int i = 1; i <= 20; i++) {
            comments.add(new Comment(
                    i,
                    (i % 20) + 1, // Assign to tasks (1-20 cyclically)
                    (i % 3) + 1, // Written by users (1, 2, 3 cyclically)
                    "Comment " + i + " for Task " + ((i % 20) + 1),
                    "2024-02-0" + (i % 28 + 1)
            ));
        }
    }

    // CRUD Methods

    // 1. Get User by ID
    public User getUserById(int userId) {
        return users.stream().filter(user -> user.getUserId() == userId).findFirst().orElse(null);
    }

    // 2. Get all tasks of a project
    public List<Task> getTasksByProjectId(int projectId) {
        return tasks.stream().filter(task -> task.getProjectId() == projectId).collect(Collectors.toList());
    }

    // 3. Get all comments for a task
    public List<Comment> getCommentsByTaskId(int taskId) {
        return comments.stream().filter(comment -> comment.getTaskId() == taskId).collect(Collectors.toList());
    }

    // 4. Update task status
    public boolean updateTaskStatus(int taskId, String newStatus) {
        Task task = tasks.stream().filter(t -> t.getTaskId() == taskId).findFirst().orElse(null);
        if (task != null) {
            task.setStatus(newStatus);
            return true;
        }
        return false;
    }

    // 5. Delete a project and its tasks
    public boolean deleteProjectById(int projectId) {
        boolean removed = projects.removeIf(project -> project.getProjectId() == projectId);
        if (removed) {
            tasks.removeIf(task -> task.getProjectId() == projectId);
        }
        return removed;
    }

    // 6. Add a new comment to a task
    public void addCommentToTask(int taskId, int userId, String content, String createdAt) {
        int newCommentId = comments.size() + 1;
        comments.add(new Comment(newCommentId, taskId, userId, content, createdAt));
    }

    // 7. Get all tasks assigned to a user
    public List<Task> getTasksAssignedToUser(int userId) {
        return tasks.stream().filter(task -> task.getAssignedTo() == userId).collect(Collectors.toList());
    }

    // Debugging: Print all data
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

