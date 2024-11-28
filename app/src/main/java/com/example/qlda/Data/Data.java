package com.example.qlda.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

//    public List<Task> getTasksByProjectId(int projectId) {
//        return tasks.stream().filter(task -> task.getProjectId() == projectId).collect(Collectors.toList());
//    }

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

    public User getUserByEmail(String email) throws SQLException {
        String query = "SELECT * FROM [User] WHERE email = ?";
        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("userID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("avatarID"),
                        rs.getString("createdAt")
                );
            }
        }
        return null;
    }
    public User login(String email, String password) throws SQLException {
        String query = "SELECT * FROM [User] WHERE email = ? AND password = ?";
        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("userID"),
                        rs.getString("fullName"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("avatarID"),
                        rs.getString("createdAt")
                );
            }
        }
        return null;
    }

    public List<Project> getProjectsByUserId(int userId) throws SQLException {
        String query = "SELECT P.* FROM ProjectUser PU JOIN Project P ON PU.projectID = P.projectID WHERE PU.userID = ?";
        List<Project> projects = new ArrayList<>();
        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                projects.add(new Project(
                        rs.getInt("projectID"),
                        rs.getString("projectName"),
                        rs.getString("description"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getString("status"),
                        rs.getInt("avataID")
                ));
            }
        }
        return projects;
    }

    public List<Task> getTasksByProjectId(int projectId) throws SQLException {
        String query = "SELECT * FROM Task WHERE projectID = ?";
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("taskId"),
                        rs.getString("taskName"),
                        rs.getString("description"),
                        rs.getInt("reporter"),
                        rs.getInt("assignedTo"),
                        rs.getInt("projectID"),
                        TaskType.valueOf(rs.getString("taskType")),
                        Priority.valueOf(rs.getString("priority")),
                        StatusType.valueOf(rs.getString("status")),
                        rs.getString("createDate"),
                        rs.getString("updatedDate")
                ));
            }
        }
        return tasks;
    }

    public List<Task> getTasksByUserOwner(int userID) throws SQLException {
        String query = "SELECT * FROM Task t JOIN ProjectUser pu ON t.projectID = pu.projectID WHERE pu.userID = ?;";

        List<Task> tasks = new ArrayList<>();

        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task(
                            rs.getInt("taskId"),
                            rs.getString("taskName"),
                            rs.getString("description"),
                            rs.getInt("reporter"),
                            rs.getInt("assignedTo"),
                            rs.getInt("projectID"),
                            TaskType.valueOf(rs.getString("taskType")),
                            Priority.valueOf(rs.getString("priority")),
                            StatusType.valueOf(rs.getString("status")),
                            rs.getString("createDate"),
                            rs.getString("updatedDate")
                    );
                    tasks.add(task);
                }
            }
        }

        return tasks;
    }

    public List<User> getUsersInProjectWithID(int projectID) throws SQLException {
        String query = "SELECT * FROM [User] u JOIN ProjectUser pu ON u.userID = pu.userID WHERE pu.projectID = ?;";

        List<User> users = new ArrayList<>();

        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, projectID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getInt("userID"),
                            rs.getString("fullName"),
                            rs.getString("email"),
                            "",
                            rs.getInt("avatarID"),
                            rs.getString("createdAt")
                    );
                    users.add(user);
                }
            }
        }

        return users;
    }

    public Project getProjectById(int projectId) throws SQLException {
        String query = "SELECT * FROM Project p WHERE p.projectID = ?;";

        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, projectId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    return new Project(
                            rs.getInt("projectID"),
                            rs.getString("projectName"),
                            rs.getString("description"),
                            rs.getString("startDate"),
                            rs.getString("endDate"),
                            rs.getString("status"),
                            rs.getInt("avataID"));
                }
            }
        }
        return null;
    }
}


