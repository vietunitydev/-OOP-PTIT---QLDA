package com.example.qlda.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Data {
    // Static instance (Singleton)
    private static Data instance;

    public static User currentUser;

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
        users.add(new User(1, "User 1", "user1@example.com", "password1", "Admin", 1,"2024-01-01"));
        users.add(new User(2, "User 2", "user2@example.com", "password2", "Admin", 2,"2024-01-01"));
        users.add(new User(3, "User 3", "user3@example.com", "password3", "Admin", 3,"2024-01-01"));
        users.add(new User(4, "User 4", "user4@example.com", "password4", "Member", 4,"2024-01-01"));
        users.add(new User(5, "User 5", "user5@example.com", "password5", "Member", 5,"2024-01-01"));
        users.add(new User(6, "User 6", "user6@example.com", "password6", "Member", 1,"2024-01-01"));
        users.add(new User(7, "User 7", "user7@example.com", "password7", "Member", 2,"2024-01-01"));
        users.add(new User(8, "User 8", "user8@example.com", "password8", "Client", 3,"2024-01-01"));
        users.add(new User(9, "User 9", "user9@example.com", "password9", "Client", 4,"2024-01-01"));
        users.add(new User(10, "User 10", "user10@example.com", "password10", "Client", 1,"2024-01-01"));

        // Projects
        projects.add(new Project(1, "Project 1", "Description for Project 1", "2024-01-01", "2024-02-01", "Ongoing", 6, 1));
        projects.add(new Project(2, "Project 2", "Description for Project 2", "2024-01-02", "2024-02-02", "Planned", 7, 2));
        projects.add(new Project(3, "Project 3", "Description for Project 3", "2024-01-03", "2024-02-03", "Ongoing", 5, 3));
        projects.add(new Project(4, "Project 4", "Description for Project 4", "2024-01-04", "2024-02-04", "Planned", 7, 4));
        projects.add(new Project(5, "Project 5", "Description for Project 5", "2024-01-05", "2024-02-05", "Ongoing", 7, 5));
        projects.add(new Project(6, "Project 6", "Description for Project 6", "2024-01-06", "2024-02-06", "Planned", 5, 6));
        projects.add(new Project(7, "Project 7", "Description for Project 7", "2024-01-07", "2024-02-07", "Ongoing", 6, 7));
        projects.add(new Project(8, "Project 8", "Description for Project 8", "2024-01-08", "2024-02-08", "Planned", 6, 8));
        projects.add(new Project(9, "Project 9", "Description for Project 9", "2024-01-09", "2024-02-09", "Ongoing", 6, 9));
        projects.add(new Project(10, "Project 10", "Description for Project 10", "2024-01-10", "2024-02-10", "Planned", 7, 10));
        projects.add(new Project(11, "Project 11", "Description for Project 11", "2024-01-11", "2024-02-11", "Ongoing", 7, 11));
        projects.add(new Project(12, "Project 12", "Description for Project 12", "2024-01-12", "2024-02-12", "Planned", 7, 12));
        projects.add(new Project(13, "Project 13", "Description for Project 13", "2024-01-13", "2024-02-13", "Ongoing", 7, 13));
        projects.add(new Project(14, "Project 14", "Description for Project 14", "2024-01-14", "2024-02-14", "Planned", 7, 14));
        projects.add(new Project(15, "Project 15", "Description for Project 15", "2024-01-15", "2024-02-15", "Ongoing", 7, 15));

        // Tasks
        tasks.add(new Task(1, "Task 1", "Details for Task 1", 1, 1, "Task","High", "Todo", "2024-03-01"));
        tasks.add(new Task(2, "Task 2", "Details for Task 2", 2, 1, "Story","Medium", "InProgress", "2024-03-02"));
        tasks.add(new Task(3, "Task 3", "Details for Task 3", 3, 1, "Bug","Low", "Done", "2024-03-03"));
        tasks.add(new Task(4, "Task 4", "Details for Task 4", 4, 1, "Task","High", "Todo", "2024-03-04"));
        tasks.add(new Task(5, "Task 5", "Details for Task 5", 5, 1, "Bug","Medium", "Done", "2024-03-05"));
        tasks.add(new Task(6, "Task 6", "Details for Task 6", 6, 1, "Bug","Low", "InProgress", "2024-03-06"));
        tasks.add(new Task(7, "Task 7", "Details for Task 7", 7, 1, "Story","High", "Done", "2024-03-07"));
        tasks.add(new Task(8, "Task 8", "Details for Task 8", 8, 1, "Story","Medium", "InProgress", "2024-03-08"));
        tasks.add(new Task(9, "Task 9", "Details for Task 9", 9, 1, "Task","Low", "Todo", "2024-03-09"));
        tasks.add(new Task(10, "Task 10", "Details for Task 10", 10, 10, "Task","High", "InProgress", "2024-03-10"));
        tasks.add(new Task(11, "Task 11", "Details for Task 11", 1, 11, "Task","Medium", "Done", "2024-03-11"));
        tasks.add(new Task(12, "Task 12", "Details for Task 12", 2, 12, "Task","Low", "InProgress", "2024-03-12"));
        tasks.add(new Task(13, "Task 13", "Details for Task 13", 3, 13, "Task","High", "Done", "2024-03-13"));
        tasks.add(new Task(14, "Task 14", "Details for Task 14", 4, 14, "Task","Medium", "InProgress", "2024-03-14"));
        tasks.add(new Task(15, "Task 15", "Details for Task 15", 5, 15, "Task","Low", "Done", "2024-03-15"));
        // Thêm các task còn lại theo cách tương tự...

        // Comments
        comments.add(new Comment(1, 1, 1, "Comment 1 on Task 1", "2024-04-01"));
        comments.add(new Comment(2, 2, 2, "Comment 2 on Task 2", "2024-04-02"));
        comments.add(new Comment(3, 3, 3, "Comment 3 on Task 3", "2024-04-03"));
        comments.add(new Comment(4, 4, 4, "Comment 4 on Task 4", "2024-04-04"));
        comments.add(new Comment(5, 5, 5, "Comment 5 on Task 5", "2024-04-05"));
        comments.add(new Comment(6, 6, 6, "Comment 6 on Task 6", "2024-04-06"));
        comments.add(new Comment(7, 7, 7, "Comment 7 on Task 7", "2024-04-07"));
        comments.add(new Comment(8, 8, 8, "Comment 8 on Task 8", "2024-04-08"));
        comments.add(new Comment(9, 9, 9, "Comment 9 on Task 9", "2024-04-09"));
        comments.add(new Comment(10, 10, 10, "Comment 10 on Task 10", "2024-04-10"));
        // Thêm các comment còn lại theo cách tương tự...
    }


    // CRUD Methods

    public User getUserById(int userId) {
        return users.stream().filter(user -> user.getUserId() == userId).findFirst().orElse(null);
    }

    public List<Task> getTasksByProjectId(int projectId) {
        return tasks.stream().filter(task -> task.getProjectId() == projectId).collect(Collectors.toList());
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

    public List<Project> getProjectsByUserId(int userId) {
        return projects.stream()
                .filter(project -> project.getManagerId() == userId)
                .collect(Collectors.toList());
    }

    public List<Project> getAllProjects() {
        return projects;
    }
    public List<User> getAllUsers() {
        return users;
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


