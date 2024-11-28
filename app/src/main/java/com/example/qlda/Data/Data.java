package com.example.qlda.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


    //////////////////// CREATE
    public boolean createProject(String projectName, String description, String startDate, String endDate, String status, int avatarID) {
        String query = "INSERT INTO Project (projectName, description, startDate, endDate, status, avataID) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, projectName);
            stmt.setString(2, description);
            stmt.setString(3, startDate);
            stmt.setString(4, endDate);
            stmt.setString(5, status);
            stmt.setInt(6, avatarID);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Project createAndFetchProject(String projectName, String description, String startDate, String endDate, String status, int avatarID) {
        String insertProjectQuery =
                "INSERT INTO Project (projectName, description, startDate, endDate, status, avataID) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        String insertProjectUserQuery =
                "INSERT INTO ProjectUser (projectID, userID, role) VALUES (?, ?, ?)";

        try (Connection conn = db.TryConnectDB()) {
            conn.setAutoCommit(false);

            int projectId;
            try (PreparedStatement projectStmt = conn.prepareStatement(insertProjectQuery, Statement.RETURN_GENERATED_KEYS)) {
                projectStmt.setString(1, projectName);
                projectStmt.setString(2, description);
                projectStmt.setString(3, startDate);
                projectStmt.setString(4, endDate);
                projectStmt.setString(5, status);
                projectStmt.setInt(6, avatarID);

                int rowsAffected = projectStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Failed to create project, no rows affected.");
                }

                try (ResultSet rs = projectStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        projectId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve projectID.");
                    }
                }
            }

            try (PreparedStatement projectUserStmt = conn.prepareStatement(insertProjectUserQuery)) {
                projectUserStmt.setInt(1, projectId);
                projectUserStmt.setInt(2, currentUser.getUserId());
                projectUserStmt.setString(3, "Admin");

                int rowsAffected = projectUserStmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Failed to create ProjectUser.");
                }
            }

            conn.commit();

            return new Project(
                    projectId,
                    projectName,
                    description,
                    startDate,
                    endDate,
                    status,
                    avatarID
            );

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
        return null;
    }

    public Task createAndFetchTask(String taskName, String description, int assignedTo, int reporter, int projectId, String taskType, String priority, String status, String createDate, String updatedDate) {
        String query = "INSERT INTO Task (taskName, description, assignedTo, reporter, projectID, taskType, priority, status, createDate, updatedDate) " +
                "OUTPUT INSERTED.taskId, INSERTED.taskName, INSERTED.description, INSERTED.assignedTo, INSERTED.reporter, INSERTED.projectID, INSERTED.taskType, INSERTED.priority, INSERTED.status, INSERTED.createDate, INSERTED.updatedDate " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, taskName);
            stmt.setString(2, description);
            stmt.setInt(3, assignedTo);
            stmt.setInt(4, reporter);
            stmt.setInt(5, projectId);
            stmt.setString(6, taskType);
            stmt.setString(7, priority);
            stmt.setString(8, status);
            stmt.setString(9, createDate);
            stmt.setString(10, updatedDate);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Task(
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Comment createAndFetchComment(int taskId, int userId, String content, String createdAt) {
        String query = "INSERT INTO Comment (taskID, userID, content, createdAt) " +
                "OUTPUT INSERTED.commentId, INSERTED.taskID, INSERTED.userID, INSERTED.content, INSERTED.createdAt " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, taskId);
            stmt.setInt(2, userId);
            stmt.setString(3, content);
            stmt.setString(4, createdAt);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Comment(
                        rs.getInt("commentId"),
                        rs.getInt("taskID"),
                        rs.getInt("userID"),
                        rs.getString("content"),
                        rs.getString("createdAt")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User createAndFetchUser(String fullName, String email, String password, int avatarID, String createdAt) {
        String query = "INSERT INTO [User] (fullName, email, password, avatarID, createdAt) " +
                "OUTPUT INSERTED.userID, INSERTED.fullName, INSERTED.email, INSERTED.password, INSERTED.avatarID, INSERTED.createdAt " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, fullName);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setInt(4, avatarID);
            stmt.setString(5, createdAt);

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    ///////////////////
    public boolean deleteUserById(int userId) {
        String deleteCommentsQuery = "DELETE FROM Comment WHERE userID = ?";
        String deleteTasksQuery = "DELETE FROM Task WHERE assignedTo = ? OR reporter = ?";
        String deleteProjectUsersQuery = "DELETE FROM ProjectUser WHERE userID = ?";
        String deleteUserQuery = "DELETE FROM [User] WHERE userID = ?";

        try {
            // Bắt đầu transaction
            connection.setAutoCommit(false);

            // Xóa Comment
            try (PreparedStatement stmt = connection.prepareStatement(deleteCommentsQuery)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Xóa Task
            try (PreparedStatement stmt = connection.prepareStatement(deleteTasksQuery)) {
                stmt.setInt(1, userId);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
            }

            // Xóa ProjectUser
            try (PreparedStatement stmt = connection.prepareStatement(deleteProjectUsersQuery)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Xóa User
            try (PreparedStatement stmt = connection.prepareStatement(deleteUserQuery)) {
                stmt.setInt(1, userId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    connection.commit();
                    return true;
                } else {
                    connection.rollback();
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    ////////////////////

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
                            rs.getString("createdAt")
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


