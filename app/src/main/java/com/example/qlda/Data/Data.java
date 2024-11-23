package com.example.qlda.Data;

import java.util.ArrayList;
import java.util.Date;
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
    private List<ProjectUser> projectUsers;

    private int userIdCounter = 1;
    private int projectIdCounter = 1;
    private int taskIdCounter = 1;
    private int commentIdCounter = 1;

    // Private constructor
    private Data() {
        users = new ArrayList<>();
        projects = new ArrayList<>();
        tasks = new ArrayList<>();
        comments = new ArrayList<>();
        projectUsers = new ArrayList<>();

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
        createUser("User 1", "user1@example.com", "password1", 1, "2024-01-01");
        createUser("User 2", "user2@example.com", "password2", 2, "2024-01-01");
        createUser("User 3", "user3@example.com", "password3", 3, "2024-01-01");
        createUser("User 4", "user4@example.com", "password4", 4, "2024-01-01");
        createUser("User 5", "user5@example.com", "password5", 5, "2024-01-01");
        createUser("User 6", "user6@example.com", "password6", 1, "2024-01-01");
        createUser("User 7", "user7@example.com", "password7", 2, "2024-01-01");
        createUser("User 8", "user8@example.com", "password8", 3, "2024-01-01");
        createUser("User 9", "user9@example.com", "password9", 4, "2024-01-01");
        createUser("User 10", "user10@example.com", "password10", 1, "2024-01-01");

        // Projects
        createProject("Project 1", "Description for Project 1", "2024-01-01", "2024-02-01", "Ongoing", 6);
        createProject("Project 2", "Description for Project 2", "2024-01-02", "2024-02-02", "Planned", 7);
        createProject("Project 3", "Description for Project 3", "2024-01-03", "2024-02-03", "Ongoing", 5);
        createProject("Project 4", "Description for Project 4", "2024-01-04", "2024-02-04", "Planned", 7);
        createProject("Project 5", "Description for Project 5", "2024-01-05", "2024-02-05", "Ongoing", 7);
        createProject("Project 6", "Description for Project 6", "2024-01-06", "2024-02-06", "Planned", 5);
        createProject("Project 7", "Description for Project 7", "2024-01-07", "2024-02-07", "Ongoing", 6);
        createProject("Project 8", "Description for Project 8", "2024-01-08", "2024-02-08", "Planned", 6);
        createProject("Project 9", "Description for Project 9", "2024-01-09", "2024-02-09", "Ongoing", 6);
        createProject("Project 10", "Description for Project 10", "2024-01-10", "2024-02-10", "Planned", 7);

        // Tasks
        createTask("Task 1", "Details for Task 1", 1, 1, 1, TaskType.Task, Priority.Low, StatusType.Todo, "2024-03-01", "2024-03-01");
        createTask("Task 2", "Details for Task 2", 1, 2, 1, TaskType.Story, Priority.Low, StatusType.InProgress, "2024-03-01", "2024-03-02");
        createTask("Task 3", "Details for Task 3", 1, 3, 1, TaskType.Bug, Priority.Low, StatusType.InProgress, "2024-03-01", "2024-03-03");
        createTask("Task 4", "Details for Task 4", 1, 4, 1, TaskType.Task, Priority.Low, StatusType.Done, "2024-03-01", "2024-03-04");
        createTask("Task 5", "Details for Task 5", 1, 5, 1, TaskType.Bug, Priority.Low, StatusType.InProgress, "2024-03-01", "2024-03-05");

        // Comments
        createComment(1, 1, "Comment 1 on Task 1", (new Date()).toString());
        createComment(2, 2, "Comment 2 on Task 2", (new Date()).toString());
        createComment(3, 3, "Comment 3 on Task 3", (new Date()).toString());
        createComment(4, 4, "Comment 4 on Task 4", (new Date()).toString());
        createComment(5, 5, "Comment 5 on Task 5", (new Date()).toString());


        projectUsers.add(new ProjectUser(1,1,"Admin"));
        projectUsers.add(new ProjectUser(1,2,"Member"));
        projectUsers.add(new ProjectUser(1,3,"Member"));
        projectUsers.add(new ProjectUser(1,4,"Member"));
        projectUsers.add(new ProjectUser(2,1,"Admin"));
        projectUsers.add(new ProjectUser(3,1,"Member"));
        projectUsers.add(new ProjectUser(3,2,"Member"));
    }

    public User createUser(String name, String email, String password, int roleId, String createdAt) {
        User newUser = new User(userIdCounter++, name, email, password, roleId, createdAt);
        users.add(newUser);
        return newUser;
    }

    public Project createProject(String name, String description, String startDate, String endDate, String status, int managerId) {
        Project newProject = new Project(projectIdCounter++, name, description, startDate, endDate, status, managerId);
        projects.add(newProject);
        return newProject;
    }

    public Task createTask(String title, String details, int projectId, int assignedTo, int createdBy,
                           TaskType taskType, Priority priority, StatusType status, String startDate, String dueDate) {
        Task newTask = new Task(taskIdCounter++, title, details, projectId, assignedTo, createdBy,
                taskType, priority, status, startDate, dueDate);
        tasks.add(newTask);
        return newTask;
    }

    public Comment createComment(int taskId, int userId, String content, String createdAt){
        Comment newComment = new Comment(commentIdCounter++, taskId, userId, content, createdAt);
        comments.add(newComment);
        return newComment;
    }



    // CRUD Methods

    static User unAssign = new User(0, "Un Assign", "unassign@example.com", "", 1,"2024-01-01");

    public User getUserById(int userId) {
        return users.stream().filter(user -> user.getUserId() == userId).findFirst().orElse(null);
    }

    public List<Task> getTasksByProjectId(int projectId) {
        return tasks.stream().filter(task -> task.getProjectId() == projectId).collect(Collectors.toList());
    }

    public List<Comment> getCommentsByTaskId(int taskId) {
        return comments.stream().filter(comment -> comment.getTaskId() == taskId).collect(Collectors.toList());
    }

    public boolean updateTaskStatus(int taskId, StatusType newStatus) {
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
            projectIdCounter--;
            tasks.removeIf(task -> task.getProjectId() == projectId);
        }
        return removed;
    }

    public boolean deleteTaskById(int taskId) {
        boolean removed = tasks.removeIf(task -> task.getTaskId() == taskId);
        if (removed) {
            taskIdCounter--;
            comments.removeIf(cmt -> cmt.getTaskId() == taskId);
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
        List<Integer> projectIds = projectUsers.stream()
                .filter(projectUser -> projectUser.getUserID() == userId)
                .map(ProjectUser::getProjectID)
                .collect(Collectors.toList());

        return projects.stream()
                .filter(project -> projectIds.contains(project.getProjectId()))
                .collect(Collectors.toList());
    }

    public List<User> getUsersByProjectId(int projectID) {
        List<Integer> userIDs = projectUsers.stream()
                .filter(projectUser -> projectUser.getProjectID() == projectID)
                .map(ProjectUser::getUserID)
                .collect(Collectors.toList());

        return users.stream()
                .filter(user -> userIDs.contains(user.getUserId()))
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


