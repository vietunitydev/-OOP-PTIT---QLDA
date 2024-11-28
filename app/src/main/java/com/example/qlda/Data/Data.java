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

    ConnectSqlServer db = new ConnectSqlServer();
    Connection connection;

    // Private constructor
    private Data() {
        connection = db.TryConnectDB();
    }

    // Public method to access the singleton instance
    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
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

    public List<Comment> getCommentByTaskID(int taskID) throws SQLException {
        String query = "SELECT * FROM Comment c WHERE taskID = ?;";

        List<Comment> cmts = new ArrayList<>();

        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, taskID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comment cmt = new Comment(
                            rs.getInt("commentId"),
                            rs.getInt("taskID"),
                            rs.getInt("userID"),
                            rs.getString("content"),
                            rs.getString("created")
                    );
                    cmts.add(cmt);
                }
            }
        }

        return cmts;
    }

    public User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM [User] WHERE userID = ?;";
        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("userID"),
                            rs.getString("fullName"),
                            rs.getString("email"),
                            "",
                            rs.getInt("avatarID"),
                            rs.getString("createdAt")
                    );
                }
            }
        }
        return null;
    }

    public boolean updateTaskStatus(int taskId, String newStatus) throws SQLException {
        String query = "UPDATE Task SET status = ? WHERE taskID = ?;";
        try (Connection conn = db.TryConnectDB();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, taskId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteProjectById(int projectId) throws SQLException {
        String deleteTasksQuery = "DELETE FROM Task WHERE projectID = ?;";
        String deleteProjectQuery = "DELETE FROM Project WHERE projectID = ?;";

        try (Connection conn = db.TryConnectDB();
             PreparedStatement deleteTasksStmt = conn.prepareStatement(deleteTasksQuery);
             PreparedStatement deleteProjectStmt = conn.prepareStatement(deleteProjectQuery)) {

            deleteTasksStmt.setInt(1, projectId);
            deleteTasksStmt.executeUpdate();

            deleteProjectStmt.setInt(1, projectId);
            return deleteProjectStmt.executeUpdate() > 0;
        }
    }

    public boolean deleteTaskById(int taskID) throws SQLException {
        String deleteTasksQuery = "DELETE FROM Task WHERE taskId = ?;";
        String deleteCommentsQuery = "DELETE FROM Comment WHERE taskID = ?;";

        try (Connection conn = db.TryConnectDB();
             PreparedStatement deleteTasksStmt = conn.prepareStatement(deleteTasksQuery);
             PreparedStatement deleteCommentStmt = conn.prepareStatement(deleteCommentsQuery)) {

            deleteTasksStmt.setInt(1, taskID);
            deleteTasksStmt.executeUpdate();

            deleteCommentStmt.setInt(1, taskID);
            return deleteCommentStmt.executeUpdate() > 0;
        }
    }

}


