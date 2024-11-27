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
    private List<ProjectUser> projectUsers;

    private int userIdCounter = 1;
    private int projectIdCounter = 1;
    private int taskIdCounter = 1;
    private int commentIdCounter = 1;

    ConnectSqlServer db = new ConnectSqlServer();

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
        users = db.getUserList();
        projects = db.getProjectList();
        tasks = db.getTasksList();
        comments = db.getCommentList();
        projectUsers = db.getProjectUserList();

        userIdCounter = users.size();
        projectIdCounter = projects.size();
        taskIdCounter = tasks.size();
        commentIdCounter = comments.size();
    }

    public User createUser(String name, String email, String password, int avtID, String createdAt) {
        User newUser = new User(userIdCounter++, name, email, password, avtID, createdAt);
        users.add(newUser);
        return newUser;
    }

    public Project createProject(String name, String description, String startDate, String endDate, String status, int avtID) {
        Project newProject = new Project(projectIdCounter++, name, description, startDate, endDate, status, avtID);
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
    public List<Task> getAllTasks() {
        return tasks;
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


