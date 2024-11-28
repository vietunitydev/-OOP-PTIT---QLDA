package com.example.qlda.Data;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ConnectSqlServer {
    // Thông tin kết nối
    private static final String DRIVER_CLASS = "net.sourceforge.jtds.jdbc.Driver";
    private static final String IP = "192.168.1.195";
    private static final String PORT = "1433";
    private static final String DATABASE = "BTL_OOP";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "Password.1";

    /**
     * Hàm kết nối với SQL Server.
     * @return Connection đối tượng kết nối với cơ sở dữ liệu, hoặc null nếu có lỗi.
     */
    public Connection TryConnectDB() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        try {
            Class.forName(DRIVER_CLASS); // Nạp driver JDBC
            String connectionUrl = "jdbc:jtds:sqlserver://" + IP + ":" + PORT + "/" + DATABASE;
            connection = DriverManager.getConnection(connectionUrl, USERNAME, PASSWORD);
            System.err.println("ConnectDB: Connect thành công.");
        } catch (ClassNotFoundException e) {
            System.err.println("ConnectDB: Không tìm thấy driver JDBC.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ConnectDB: Kết nối tới cơ sở dữ liệu không thành công.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Hàm thực thi câu truy vấn SQL và lấy dữ liệu kiểu int.
     * @param query Câu truy vấn SQL.
     * @return Giá trị int từ cột đầu tiên của hàng đầu tiên, hoặc 0 nếu không có dữ liệu.
     */
    public int getIntData(String query) {
        int result = 0;
        try (Connection connection = TryConnectDB();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null && resultSet.next()) {
                result = resultSet.getInt(1); // Lấy giá trị từ cột đầu tiên
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thực thi câu truy vấn: " + query);
            e.printStackTrace();
        }
        return result;
    }

    public String getStringData(String query) {
        String result = "";
        try (Connection connection = TryConnectDB();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null && resultSet.next()) {
                result = resultSet.getString(1); // Lấy giá trị từ cột đầu tiên
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi thực thi câu truy vấn: " + query);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Hàm lấy danh sách các dự án từ bảng Project.
     * @return Danh sách các đối tượng Project.
     */
    public List<Project> getProjectList() {
        List<Project> projects = new ArrayList<>();
        String query = "SELECT * FROM Project";

        try (Connection connection = TryConnectDB();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            while (resultSet != null && resultSet.next()) {
                String createdStartString = null;
                Timestamp createdDateTimestamp = resultSet.getTimestamp("startDate");
                if (createdDateTimestamp != null) {
                    createdStartString = getStringFromTimeStamp(createdDateTimestamp);
                }

                String createdEndString = null;
                Timestamp createdAtTimestamp = resultSet.getTimestamp("endDate");
                if (createdAtTimestamp != null) {
                    createdEndString =getStringFromTimeStamp(createdAtTimestamp);
                }

                Project project = new Project(
                        resultSet.getInt("projectID"),
                        resultSet.getString("projectName"),
                        resultSet.getString("description"),
                        createdStartString,
                        createdEndString,
                        resultSet.getString("status"),
                        resultSet.getInt("avataID")
                );
                projects.add(project);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách dự án.");
            e.printStackTrace();
        }
        return projects;
    }
    public static String fixName(String it){
        StringBuffer tmp = new StringBuffer();
        tmp.append(Character.toUpperCase(it.charAt(0)))
                .append(it.substring(1))
                .append(" ");
        return tmp.toString().trim();
    }
    public List<Task> getTasksList() {
        String query = "SELECT * FROM Task"; // Đảm bảo đúng bảng Task
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = TryConnectDB();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            if (resultSet != null) {
                while (resultSet.next()) {
                    int taskId = resultSet.getInt("TaskId");
                    String taskName = resultSet.getString("TaskName");
                    String description = resultSet.getString("Description");
                    int assignedTo = resultSet.getInt("AssignedTo");
                    int reporter = resultSet.getInt("Reporter");
                    int projectId = resultSet.getInt("ProjectId");

                    TaskType taskType = TaskType.valueOf(fixName(resultSet.getString("TaskType")));
                    Priority priority = Priority.valueOf(fixName(resultSet.getString("priority")));
                    StatusType status = StatusType.valueOf(fixName(resultSet.getString("status")));

                    String createdDateString = null;
                    Timestamp createdDateTimestamp = resultSet.getTimestamp("CreateDate");
                    if (createdDateTimestamp != null) {
                        createdDateString = getStringFromTimeStamp(createdDateTimestamp);
                    }

                    String updatedDateString = null;
                    Timestamp updatedDateTimestamp = resultSet.getTimestamp("UpdatedDate");
                    if (updatedDateTimestamp != null) {
                        updatedDateString = getStringFromTimeStamp(updatedDateTimestamp);
                    }

                    Task task = new Task(taskId, taskName, description, assignedTo, reporter, projectId,
                            taskType, priority, status, createdDateString, updatedDateString);
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    public List<User> getUserList() {
        String query = "SELECT * FROM [User]";
        List<User> users = new ArrayList<>();
        try (Connection connection = TryConnectDB();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            while (resultSet != null && resultSet.next()) {
                String createdAtString = null;
                Timestamp createdAtTimestamp = resultSet.getTimestamp("createdAt");
                if (createdAtTimestamp != null) {
                    createdAtString = getStringFromTimeStamp(createdAtTimestamp);
                }

                User user = new User(
                        resultSet.getInt("userId"),
                        resultSet.getString("fullName"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getInt("avatarID"),
                        createdAtString
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<Comment> getCommentList() {
        String query = "SELECT * FROM Comment";
        List<Comment> comments = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Định dạng ngày giờ

        try (Connection connection = TryConnectDB();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            while (resultSet != null && resultSet.next()) {
                String createdAtString = null;
                Timestamp createdAtTimestamp = resultSet.getTimestamp("createdAt"); // Lấy dữ liệu dạng Timestamp
                if (createdAtTimestamp != null) {
                    createdAtString = dateFormat.format(createdAtTimestamp); // Chuyển sang String
                }

                Comment comment = new Comment(
                        resultSet.getInt("commentId"),
                        resultSet.getInt("taskId"),
                        resultSet.getInt("userId"),
                        resultSet.getString("content"),
                        createdAtString
                );
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
    public List<ProjectUser> getProjectUserList() {
        String query = "SELECT * FROM ProjectUser";
        List<ProjectUser> projectUsers = new ArrayList<>();

        try (Connection connection = TryConnectDB();
             Statement statement = connection != null ? connection.createStatement() : null;
             ResultSet resultSet = statement != null ? statement.executeQuery(query) : null) {

            while (resultSet != null && resultSet.next()) {

                ProjectUser projectUser = new ProjectUser(
                        resultSet.getInt("projectID"),
                        resultSet.getInt("userID"),
                        resultSet.getString("role")
                );
                projectUsers.add(projectUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return projectUsers;
    }

    private String getStringFromTimeStamp(Timestamp time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Định dạng ngày giờ
        return dateFormat.format(time);
    }
}
