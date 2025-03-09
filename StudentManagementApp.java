import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentManagementApp {

    // Student Model Class
    public static class Student {
        private int studentID;
        private String name;
        private String department;
        private double marks;

        // Constructor
        public Student(int studentID, String name, String department, double marks) {
            this.studentID = studentID;
            this.name = name;
            this.department = department;
            this.marks = marks;
        }

        // Getters and Setters
        public int getStudentID() {
            return studentID;
        }

        public void setStudentID(int studentID) {
            this.studentID = studentID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public double getMarks() {
            return marks;
        }

        public void setMarks(double marks) {
            this.marks = marks;
        }

        @Override
        public String toString() {
            return "StudentID: " + studentID + ", Name: " + name + ", Department: " + department + ", Marks: " + marks;
        }
    }

    // Student Controller Class (Handles Database Operations)
    public static class StudentController {
        private static final String URL = "jdbc:mysql://localhost:3306/your_database";
        private static final String USER = "your_username";
        private static final String PASSWORD = "your_password";
        private Connection conn;

        public StudentController() {
            try {
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Create student
        public boolean addStudent(Student student) {
            String query = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, student.getStudentID());
                stmt.setString(2, student.getName());
                stmt.setString(3, student.getDepartment());
                stmt.setDouble(4, student.getMarks());
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        // Read all students
        public List<Student> getAllStudents() {
            List<Student> students = new ArrayList<>();
            String query = "SELECT * FROM Student";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int studentID = rs.getInt("StudentID");
                    String name = rs.getString("Name");
                    String department = rs.getString("Department");
                    double marks = rs.getDouble("Marks");
                    students.add(new Student(studentID, name, department, marks));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return students;
        }

        // Update student
        public boolean updateStudent(Student student) {
            String query = "UPDATE Student SET Name = ?, Department = ?, Marks = ? WHERE StudentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, student.getName());
                stmt.setString(2, student.getDepartment());
                stmt.setDouble(3, student.getMarks());
                stmt.setInt(4, student.getStudentID());
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        // Delete student
        public boolean deleteStudent(int studentID) {
            String query = "DELETE FROM Student WHERE StudentID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, studentID);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        // Close connection
        public void closeConnection() {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Student View Class (User Interface)
    public static class StudentView {
        private StudentController controller;
        private Scanner scanner;

        public StudentView(StudentController controller) {
            this.controller = controller;
            this.scanner = new Scanner(System.in);
        }

        public void displayMenu() {
            while (true) {
                System.out.println("\n************ Student CRUD Operations ************");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline character

                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        viewAllStudents();
                        break;
                    case 3:
                        updateStudent();
                        break;
                    case 4:
                        deleteStudent();
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        controller.closeConnection();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        private void addStudent() {
            System.out.print("Enter Student ID: ");
            int studentID = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Department: ");
            String department = scanner.nextLine();
            System.out.print("Enter Marks: ");
            double marks = scanner.nextDouble();

            Student student = new Student(studentID, name, department, marks);
            if (controller.addStudent(student)) {
                System.out.println("Student added successfully.");
            } else {
                System.out.println("Failed to add student.");
            }
        }

        private void viewAllStudents() {
            List<Student> students = controller.getAllStudents();
            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                System.out.println("\nStudent ID | Name | Department | Marks");
                for (Student student : students) {
                    System.out.println(student);
                }
            }
        }

        private void updateStudent() {
            System.out.print("Enter Student ID to update: ");
            int studentID = scanner.nextInt();
            scanner.nextLine();  // Consume newline
            System.out.print("Enter New Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter New Department: ");
            String department = scanner.nextLine();
            System.out.print("Enter New Marks: ");
            double marks = scanner.nextDouble();

            Student student = new Student(studentID, name, department, marks);
            if (controller.updateStudent(student)) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Failed to update student.");
            }
        }

        private void deleteStudent() {
            System.out.print("Enter Student ID to delete: ");
            int studentID = scanner.nextInt();
            if (controller.deleteStudent(studentID)) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Failed to delete student.");
            }
        }
    }

    // Main Application Entry Point
    public static void main(String[] args) {
        StudentController controller = new StudentController();
        StudentView view = new StudentView(controller);
        view.displayMenu();
    }
}
