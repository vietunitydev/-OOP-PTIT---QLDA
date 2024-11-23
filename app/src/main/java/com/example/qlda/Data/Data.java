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

        createUser("Phạm Xuân Hải", "hai.px@gmail.com", "password", 1, "2024-11-05");
        createUser("Đoàn Quốc Việt", "viet.dq@gmail.com", "password", 2, "2024-11-07");
        createUser("Lê Quang Thắng", "thang.lq@gmail.com", "password", 3, "2024-11-09");
        createUser("Hồ Ngọc Linh", "linh.hn@gmail.com", "password", 4, "2024-11-11");
        createUser("Phan Tuấn Anh", "anh.qt@gmail.com", "password", 5, "2024-11-12");
        createUser("Nguyễn Huy Hoàng", "hoang.nh@gmail.com", "password", 3, "2024-11-13");
        createUser("Vũ Minh Thảo", "thao.vm@gmail.com", "password", 7, "2024-11-14");
        createUser("Đặng Thị Linh", "linh.dt@gmail.com", "password", 3, "2024-11-15");
        createUser("Võ Văn Tùng", "tung.vt@gmail.com", "password", 3, "2024-11-16");
        createUser("Nguyễn Quang Lâm", "lam.nq@gmail.com", "password", 3, "2024-11-17");
        createUser("Trần Tuấn Bảo", "bao.tt@gmail.com", "password", 3, "2024-11-18");
        createUser("Lê Thị Trang", "trang.lt@gmail.com", "password", 3, "2024-11-19");
        createUser("Phạm Bích Lan", "lan.pb@gmail.com", "password", 3, "2024-11-20");
        createUser("Nguyễn Văn Nam", "nam.nv@gmail.com", "password", 3, "2024-11-21");
        createUser("Nguyễn Tường Vy", "vy.nt@gmail.com", "password", 3, "2024-11-22");

        // 20 project
        createProject("Quản lý hệ thống nhân sự", "Xây dựng hệ thống quản lý nhân sự cho công ty", "2024-11-01", "2024-12-01", "In Progress", 5);
        createProject("Ứng dụng thanh toán điện tử", "Phát triển ứng dụng thanh toán điện tử an toàn và tiện lợi", "2024-11-02", "2024-12-05", "In Progress", 5);
        createProject("Phát triển hệ thống báo cáo", "Xây dựng hệ thống báo cáo tài chính cho doanh nghiệp", "2024-11-03", "2024-12-10", "In Progress", 5);
        createProject("Tối ưu hóa hiệu suất hệ thống", "Tối ưu hóa hiệu suất và bảo mật hệ thống website", "2024-11-04", "2024-11-30", "In Progress", 5);
        createProject("Xây dựng hệ thống chatbot", "Tạo chatbot AI hỗ trợ khách hàng tự động 24/7", "2024-11-05", "2024-12-15", "In Progress", 5);
        createProject("Quản lý kho hàng", "Phát triển hệ thống quản lý kho hàng và logistics", "2024-11-06", "2024-12-20", "In Progress", 6);
        createProject("Hệ thống đặt vé máy bay", "Phát triển hệ thống đặt vé máy bay trực tuyến", "2024-11-07", "2024-12-10", "In Progress", 7);
        createProject("Ứng dụng bán hàng online", "Xây dựng ứng dụng bán hàng trực tuyến với tính năng thanh toán an toàn", "2024-11-08", "2024-12-05", "In Progress", 5);
        createProject("Phát triển hệ thống CRM", "Xây dựng hệ thống CRM để quản lý mối quan hệ khách hàng", "2024-11-09", "2024-12-12", "In Progress", 5);
        createProject("Quản lý dự án phần mềm", "Phát triển phần mềm quản lý dự án cho các công ty", "2024-11-10", "2024-12-01", "In Progress", 5);
        createProject("Ứng dụng giáo dục trực tuyến", "Phát triển nền tảng học trực tuyến cho học sinh và sinh viên", "2024-11-11", "2024-12-10", "In Progress", 5);
        createProject("Quản lý tài chính cá nhân", "Xây dựng ứng dụng quản lý tài chính cá nhân", "2024-11-12", "2024-12-15", "In Progress", 5);
        createProject("Hệ thống chăm sóc sức khỏe", "Phát triển ứng dụng chăm sóc sức khỏe cho người dùng", "2024-11-13", "2024-12-15", "In Progress", 5);
        createProject("Quản lý bán vé sự kiện", "Phát triển hệ thống bán vé sự kiện trực tuyến", "2024-11-14", "2024-12-20", "In Progress", 5);
        createProject("Hệ thống quản lý trường học", "Xây dựng hệ thống quản lý học sinh, điểm số và lịch học", "2024-11-15", "2024-12-18", "In Progress", 5);
        createProject("Phát triển game di động", "Phát triển một trò chơi di động giải trí cho người dùng", "2024-11-16", "2024-12-20", "In Progress", 5);
        createProject("Quản lý dịch vụ thuê xe", "Xây dựng hệ thống quản lý thuê xe cho khách hàng", "2024-11-17", "2024-12-15", "In Progress", 5);
        createProject("Ứng dụng tìm kiếm việc làm", "Phát triển ứng dụng tìm kiếm việc làm cho sinh viên", "2024-11-18", "2024-12-10", "In Progress", 5);
        createProject("Quản lý bán hàng cho doanh nghiệp", "Phát triển hệ thống quản lý bán hàng cho các doanh nghiệp nhỏ", "2024-11-19", "2024-12-05", "In Progress", 5);
        createProject("Ứng dụng hỗ trợ mua sắm", "Phát triển ứng dụng hỗ trợ mua sắm cho người tiêu dùng", "2024-11-20", "2024-12-12", "In Progress", 5);

        //30 task
        createTask("Thiết kế cơ sở dữ liệu cho hệ thống nhân sự", "Tạo mô hình dữ liệu cho hệ thống nhân sự", 1, 2, 1, TaskType.Story, Priority.High, StatusType.InProgress, "2024-11-01", "2024-11-10");
        createTask("Tích hợp API thanh toán", "Kết nối với cổng thanh toán điện tử", 2, 3, 2, TaskType.Task, Priority.High, StatusType.InProgress, "2024-11-02", "2024-11-12");
        createTask("Tối ưu hiệu suất hệ thống website", "Tối ưu hóa tốc độ tải trang và bảo mật", 3, 4, 3, TaskType.Bug, Priority.Medium, StatusType.Todo, "2024-11-03", "2024-11-15");
        createTask("Phát triển chatbot hỗ trợ khách hàng", "Thiết kế giao diện và tính năng chatbot", 4, 5, 4, TaskType.Story, Priority.High, StatusType.InProgress, "2024-11-04", "2024-11-18");
        createTask("Cài đặt hệ thống quản lý kho", "Cài đặt phần mềm quản lý kho hàng", 5, 6, 5, TaskType.Task, Priority.High, StatusType.Todo, "2024-11-05", "2024-11-20");
        createTask("Phát triển chức năng thanh toán trong ứng dụng bán hàng", "Tạo hệ thống thanh toán an toàn cho người dùng", 6, 7, 6, TaskType.Task, Priority.High, StatusType.Todo, "2024-11-06", "2024-11-25");
        createTask("Xây dựng API tìm kiếm vé máy bay", "Tạo API tìm kiếm và đặt vé máy bay", 7, 8, 7, TaskType.Story, Priority.Medium, StatusType.InProgress, "2024-11-07", "2024-11-20");
        createTask("Phát triển hệ thống CRM", "Xây dựng tính năng quản lý thông tin khách hàng", 8, 9, 8, TaskType.Task, Priority.Medium, StatusType.Todo, "2024-11-08", "2024-11-22");
        createTask("Sửa lỗi báo cáo tài chính", "Khắc phục lỗi không hiển thị dữ liệu trong báo cáo", 9, 10, 9, TaskType.Bug, Priority.Low, StatusType.InProgress, "2024-11-09", "2024-11-14");
        createTask("Thiết kế giao diện người dùng", "Tạo giao diện cho ứng dụng giáo dục trực tuyến", 10, 11, 10, TaskType.Story, Priority.High, StatusType.Todo, "2024-11-10", "2024-11-18");
        createTask("Thiết kế cơ sở dữ liệu cho hệ thống quản lý kho", "Xây dựng mô hình dữ liệu cho hệ thống kho hàng", 5, 6, 6, TaskType.Story, Priority.High, StatusType.InProgress, "2024-11-07", "2024-11-17");
        createTask("Xây dựng tính năng quản lý đơn hàng", "Phát triển tính năng cho phép khách hàng quản lý đơn hàng của họ", 8, 7, 8, TaskType.Story, Priority.High, StatusType.Todo, "2024-11-10", "2024-11-20");
        createTask("Phát triển hệ thống phân tích báo cáo tài chính", "Xây dựng công cụ phân tích dữ liệu báo cáo tài chính", 9, 9, 9, TaskType.Task, Priority.Medium, StatusType.InProgress, "2024-11-12", "2024-11-22");
        createTask("Tối ưu hóa giao diện người dùng cho ứng dụng giáo dục", "Cải thiện giao diện người dùng cho học viên và giảng viên", 11, 10, 10, TaskType.Task, Priority.Low, StatusType.Todo, "2024-11-13", "2024-11-25");
        createTask("Tích hợp hệ thống thanh toán vào website bán vé máy bay", "Liên kết hệ thống thanh toán vào các trang sản phẩm vé máy bay", 7, 6, 7, TaskType.Task, Priority.High, StatusType.InProgress, "2024-11-14", "2024-11-20");
        createTask("Xây dựng công cụ tìm kiếm cho hệ thống bán vé máy bay", "Phát triển công cụ tìm kiếm vé máy bay theo các tiêu chí", 7, 8, 7, TaskType.Story, Priority.High, StatusType.InProgress, "2024-11-15", "2024-11-25");
        createTask("Cập nhật chức năng báo cáo tài chính", "Cập nhật các mẫu báo cáo tài chính theo yêu cầu mới", 9, 10, 9, TaskType.Task, Priority.Medium, StatusType.Todo, "2024-11-16", "2024-11-30");
        createTask("Tạo tính năng quản lý người dùng cho ứng dụng bán hàng", "Phát triển tính năng cho phép quản lý tài khoản người dùng", 8, 7, 8, TaskType.Task, Priority.High, StatusType.InProgress, "2024-11-17", "2024-11-27");
        createTask("Đảm bảo bảo mật cho hệ thống thanh toán", "Kiểm tra và củng cố các lỗ hổng bảo mật trong hệ thống thanh toán", 2, 6, 6, TaskType.Bug, Priority.High, StatusType.InProgress, "2024-11-18", "2024-11-22");
        createTask("Xây dựng hệ thống email marketing cho CRM", "Phát triển công cụ gửi email marketing cho khách hàng trong CRM", 9, 8, 9, TaskType.Story, Priority.Medium, StatusType.Todo, "2024-11-19", "2024-11-28");
        createTask("Tạo báo cáo tài chính tháng 11", "Tạo báo cáo tài chính cho doanh nghiệp, hiển thị các chi phí và lợi nhuận", 9, 10, 9, TaskType.Task, Priority.Low, StatusType.Todo, "2024-11-20", "2024-11-30");
        createTask("Cải tiến chức năng tìm kiếm trong ứng dụng giáo dục", "Tạo công cụ tìm kiếm nội dung học liệu cho học viên", 11, 10, 10, TaskType.Task, Priority.High, StatusType.InProgress, "2024-11-21", "2024-11-29");
        createTask("Tối ưu API hệ thống thanh toán", "Tối ưu hóa API để giảm độ trễ trong quá trình thanh toán", 2, 7, 7, TaskType.Task, Priority.High, StatusType.InProgress, "2024-11-22", "2024-11-30");
        createTask("Kiểm thử giao diện người dùng của hệ thống bán hàng", "Chạy thử nghiệm giao diện của ứng dụng bán hàng", 8, 7, 8, TaskType.Bug, Priority.Low, StatusType.InProgress, "2024-11-23", "2024-11-30");
        createTask("Quản lý kho hàng trực tuyến", "Tạo giao diện quản lý kho cho người dùng", 5, 6, 6, TaskType.Task, Priority.Medium, StatusType.Todo, "2024-11-24", "2024-11-30");
        createTask("Cải thiện tốc độ tải trang trên hệ thống bán vé", "Tối ưu mã nguồn và cơ sở dữ liệu để giảm thời gian tải trang", 7, 8, 7, TaskType.Bug, Priority.High, StatusType.Todo, "2024-11-25", "2024-11-30");
        createTask("Xây dựng công cụ tự động hóa marketing", "Phát triển hệ thống tự động hóa marketing cho sản phẩm", 9, 10, 9, TaskType.Story, Priority.Medium, StatusType.Todo, "2024-11-26", "2024-12-05");
        createTask("Cập nhật hệ thống API cho ứng dụng bán vé", "Cập nhật API để hỗ trợ thêm các tính năng mới", 7, 8, 7, TaskType.Task, Priority.High, StatusType.Todo, "2024-11-27", "2024-12-05");
        createTask("Tạo tính năng đăng nhập và bảo mật cho ứng dụng bán hàng", "Cải thiện hệ thống đăng nhập và bảo mật tài khoản người dùng", 8, 7, 8, TaskType.Task, Priority.High, StatusType.Todo, "2024-11-28", "2024-12-10");
        createTask("Phát triển công cụ quản lý đánh giá khách hàng cho CRM", "Tạo hệ thống theo dõi và phân tích đánh giá của khách hàng", 9, 10, 9, TaskType.Story, Priority.Medium, StatusType.Todo, "2024-11-29", "2024-12-10");
        createTask("Thực hiện kiểm thử hệ thống thanh toán", "Chạy thử nghiệm và kiểm tra hệ thống thanh toán", 2, 6, 6, TaskType.Bug, Priority.High, StatusType.Todo, "2024-11-30", "2024-12-10");

        // 50 comment
        createComment(1, 1, "Cơ sở dữ liệu đã được thiết kế, cần xem xét và phê duyệt.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(2, 1, "API thanh toán đã được tích hợp, cần thực hiện kiểm thử kỹ càng.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(3, 1, "Lỗi tốc độ đã được khắc phục, kiểm tra và xác nhận lại giúp tôi.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(4, 1, "Chức năng chatbot đã hoàn thiện, đang được kiểm thử trước khi triển khai.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(5, 1, "Phần mềm quản lý kho đã được triển khai, hiện tại hệ thống đang chạy ổn định.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(6, 2, "Tính năng thanh toán đã được cài đặt xong, chờ kiểm thử thêm.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(7, 2, "API tìm kiếm vé máy bay đã hoàn thành, cần kiểm tra hiệu suất.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(8, 2, "Đã tích hợp hệ thống email marketing thành công, kiểm thử thử nghiệm.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(9, 3, "Báo cáo tài chính tháng này đã được tạo, chờ kiểm tra trước khi phát hành.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(10, 3, "Giao diện cho ứng dụng giáo dục trực tuyến đã hoàn thành, cần phản hồi từ nhóm.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(11, 3, "Tính năng tìm kiếm trong ứng dụng đã được tối ưu, đang trong giai đoạn kiểm thử.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(12, 4, "Cập nhật hệ thống thanh toán đã xong, cần triển khai kiểm thử với dữ liệu thực.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(13, 4, "Hệ thống báo cáo tài chính cần thêm một số điều chỉnh nhỏ.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(14, 4, "Kiểm tra hệ thống thanh toán đã hoàn tất, hiệu suất cải thiện rõ rệt.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(15, 5, "Tối ưu hóa tốc độ tải trang đã thành công.","2024-11-15");
        createComment(16, 6, "Đã hoàn thành kiểm thử chức năng đăng nhập và bảo mật, yêu cầu phê duyệt.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(17, 7, "Tính năng quản lý đơn hàng đang được kiểm thử, dự kiến sẽ hoàn thành trong tuần tới.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(18, 8, "Chức năng phân tích báo cáo tài chính đã hoàn thiện, cần kiểm thử với các dữ liệu thực tế.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(19, 4, "Giao diện người dùng của ứng dụng bán vé máy bay đã được cải thiện, cần phản hồi.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(20, 5, "Tính năng tìm kiếm vé máy bay đang được thử nghiệm, hiệu suất khá ổn.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(21, 4, "Tạo báo cáo tài chính cho tháng 11 đã hoàn thành, cần kiểm tra một số chỉ tiêu mới.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(22, 5, "Đã cập nhật các tính năng mới cho CRM, cần thử nghiệm với các dữ liệu đầu vào khác nhau.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(23, 6, "Công cụ tự động hóa marketing đã sẵn sàng, đang tiến hành kiểm thử với các chiến dịch mẫu.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(24, 3, "Tính năng quản lý người dùng trong ứng dụng bán hàng đã hoàn thiện, đang trong giai đoạn kiểm thử.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(25, 2, "Lỗi bảo mật trong hệ thống thanh toán đã được khắc phục, cần thử nghiệm với dữ liệu thực tế.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(26, 7, "Cập nhật API cho ứng dụng bán vé đã hoàn thành, cần thử nghiệm với dữ liệu người dùng thực.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(27, 10, "Chức năng thanh toán trực tuyến đã được cải tiến, tiến hành kiểm thử trong các kịch bản sử dụng khác nhau.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(28, 8, "Giao diện người dùng của hệ thống quản lý kho đã được cập nhật, yêu cầu phản hồi từ nhóm kiểm thử.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(29, 9, "Chức năng báo cáo tài chính đã được hoàn thiện, đang trong giai đoạn thử nghiệm cuối cùng.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(30, 10, "Đã triển khai và kiểm thử thành công tính năng tìm kiếm trong hệ thống bán vé, hiệu suất đã được cải thiện.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(31, 11, "Báo cáo tài chính tháng 11 đã hoàn thành, đang gửi cho nhóm quản lý để xem xét.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(32, 12, "Đã hoàn thành việc kiểm thử hệ thống thanh toán, có thể triển khai lên môi trường sản xuất.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(33, 13, "Chức năng tìm kiếm vé máy bay đã được hoàn thiện, chờ triển khai vào ngày mai.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(34, 14, "Các báo cáo tài chính đã được cập nhật đầy đủ, sẽ đưa vào hệ thống kiểm thử vào tuần tới.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(35, 5, "Các tính năng bảo mật trong hệ thống thanh toán đã được nâng cấp, cần triển khai kiểm thử trong môi trường thực tế.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(36, 6, "Chức năng báo cáo tài chính đã được hoàn thành, đang chờ phê duyệt từ nhóm kiểm thử.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(37, 7, "Cập nhật API cho hệ thống bán vé đã xong, đang kiểm tra các tính năng đã được triển khai.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(38, 8, "Chức năng tự động hóa marketing đã được hoàn thiện, đang kiểm thử trong các chiến dịch nhỏ.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(39, 9, "Đã hoàn thành việc cải tiến tốc độ tải trang, tiến hành thử nghiệm và đánh giá hiệu suất.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(40, 10, "Chức năng quản lý kho hàng đã được cài đặt và thử nghiệm thành công, đang triển khai lên môi trường thực.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(41, 11, "Hệ thống tìm kiếm trong ứng dụng bán vé đã được hoàn thiện, đang trong giai đoạn kiểm thử cuối cùng.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(42, 12, "Tính năng báo cáo tài chính cho doanh nghiệp đã được cải tiến, cần triển khai vào tháng tới.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(43, 3, "Hệ thống thanh toán đã được kiểm tra và cập nhật, dự kiến triển khai vào tuần tới.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(44, 4, "Cải tiến hệ thống đăng nhập và bảo mật đã được hoàn thành, cần thử nghiệm trong các điều kiện môi trường thực tế.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(45, 5, "Tính năng quản lý đánh giá khách hàng đã được triển khai, cần kiểm thử với một số chiến dịch mẫu.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(46, 6, "Ứng dụng bán vé máy bay đã cập nhật hệ thống thanh toán, cần triển khai kiểm thử toàn diện.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(47, 7, "Hệ thống báo cáo tài chính đã hoàn tất và đang trong giai đoạn triển khai.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(48, 4, "Chức năng tự động hóa marketing đã được tích hợp thành công, dự kiến triển khai trong tuần tới.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(49, 4, "Đã hoàn thành kiểm thử bảo mật cho hệ thống thanh toán, hiệu suất hoạt động ổn định.", (new Date(2024,10,10,10,10,10)).toString());
        createComment(50, 5, "Ứng dụng bán vé máy bay đã được kiểm thử xong, tiến hành triển khai lên môi trường sản xuất.", (new Date(2024,10,10,10,10,10)).toString());

        // Phạm Xuân Hải làm Admin các dự án
        projectUsers.add(new ProjectUser(1, 1, "Admin"));
        projectUsers.add(new ProjectUser(2, 1, "Admin"));
        projectUsers.add(new ProjectUser(3, 1, "Admin"));
        projectUsers.add(new ProjectUser(4, 1, "Admin"));
        projectUsers.add(new ProjectUser(5, 1, "Admin"));
        projectUsers.add(new ProjectUser(6, 1, "Admin"));

// Đoàn Quốc Việt làm Admin các dự án
        projectUsers.add(new ProjectUser(7, 2, "Admin"));
        projectUsers.add(new ProjectUser(8, 2, "Admin"));
        projectUsers.add(new ProjectUser(9, 2, "Admin"));
        projectUsers.add(new ProjectUser(10, 2, "Admin"));
        projectUsers.add(new ProjectUser(11, 2, "Admin"));
        projectUsers.add(new ProjectUser(12, 2, "Admin"));

// Lê Quang Thắng làm Admin các dự án
        projectUsers.add(new ProjectUser(13, 3, "Admin"));
        projectUsers.add(new ProjectUser(14, 3, "Admin"));
        projectUsers.add(new ProjectUser(15, 3, "Admin"));
        projectUsers.add(new ProjectUser(16, 3, "Admin"));
        projectUsers.add(new ProjectUser(17, 3, "Admin"));
        projectUsers.add(new ProjectUser(18, 3, "Admin"));

// Hồ Ngọc Linh làm Admin các dự án
        projectUsers.add(new ProjectUser(19, 4, "Admin"));
        projectUsers.add(new ProjectUser(20, 4, "Admin"));
        projectUsers.add(new ProjectUser(1, 4, "Admin"));
        projectUsers.add(new ProjectUser(2, 4, "Admin"));
        projectUsers.add(new ProjectUser(3, 4, "Admin"));
        projectUsers.add(new ProjectUser(4, 4, "Admin"));

// Các người dùng còn lại tham gia dự án với vai trò "Member"
        projectUsers.add(new ProjectUser(1, 5, "Member"));
        projectUsers.add(new ProjectUser(2, 6, "Member"));
        projectUsers.add(new ProjectUser(3, 7, "Member"));
        projectUsers.add(new ProjectUser(4, 8, "Member"));
        projectUsers.add(new ProjectUser(5, 9, "Member"));
        projectUsers.add(new ProjectUser(6, 10, "Member"));
        projectUsers.add(new ProjectUser(7, 11, "Member"));
        projectUsers.add(new ProjectUser(8, 12, "Member"));
        projectUsers.add(new ProjectUser(9, 13, "Member"));
        projectUsers.add(new ProjectUser(10, 14, "Member"));
        projectUsers.add(new ProjectUser(11, 15, "Member"));
        projectUsers.add(new ProjectUser(12, 16, "Member"));
        projectUsers.add(new ProjectUser(13, 17, "Member"));
        projectUsers.add(new ProjectUser(14, 18, "Member"));
        projectUsers.add(new ProjectUser(15, 19, "Member"));
        projectUsers.add(new ProjectUser(16, 20, "Member"));
        projectUsers.add(new ProjectUser(17, 5, "Member"));
        projectUsers.add(new ProjectUser(18, 6, "Member"));
        projectUsers.add(new ProjectUser(19, 7, "Member"));
        projectUsers.add(new ProjectUser(20, 8, "Member"));
        projectUsers.add(new ProjectUser(1, 9, "Member"));
        projectUsers.add(new ProjectUser(2, 10, "Member"));
        projectUsers.add(new ProjectUser(3, 11, "Member"));
        projectUsers.add(new ProjectUser(4, 12, "Member"));
        projectUsers.add(new ProjectUser(5, 13, "Member"));
        projectUsers.add(new ProjectUser(6, 14, "Member"));
        projectUsers.add(new ProjectUser(7, 15, "Member"));
        projectUsers.add(new ProjectUser(8, 16, "Member"));
        projectUsers.add(new ProjectUser(9, 17, "Member"));
        projectUsers.add(new ProjectUser(10, 18, "Member"));
        projectUsers.add(new ProjectUser(11, 19, "Member"));
        projectUsers.add(new ProjectUser(12, 20, "Member"));
        projectUsers.add(new ProjectUser(13, 5, "Member"));
        projectUsers.add(new ProjectUser(14, 6, "Member"));
        projectUsers.add(new ProjectUser(15, 7, "Member"));
        projectUsers.add(new ProjectUser(16, 8, "Member"));
        projectUsers.add(new ProjectUser(17, 9, "Member"));
        projectUsers.add(new ProjectUser(18, 10, "Member"));
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


