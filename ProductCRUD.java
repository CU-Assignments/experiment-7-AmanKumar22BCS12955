import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/your_database";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";
    private static Connection conn = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Establish connection to the database
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false); // Set auto-commit to false for transaction management

            while (true) {
                // Show menu and prompt for choice
                System.out.println("************ Product CRUD Operations ************");
                System.out.println("1. Create Product");
                System.out.println("2. Read Product");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        createProduct();
                        break;
                    case 2:
                        readProduct();
                        break;
                    case 3:
                        updateProduct();
                        break;
                    case 4:
                        deleteProduct();
                        break;
                    case 5:
                        System.out.println("Exiting program.");
                        closeConnection();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Create Product
    private static void createProduct() {
        try {
            System.out.print("Enter Product ID: ");
            int productId = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            System.out.print("Enter Product Name: ");
            String productName = scanner.nextLine();
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            // SQL Insert Statement
            String query = "INSERT INTO Product (ProductID, ProductName, Price, Quantity) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, productId);
                stmt.setString(2, productName);
                stmt.setDouble(3, price);
                stmt.setInt(4, quantity);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit(); // Commit transaction
                    System.out.println("Product created successfully.");
                } else {
                    conn.rollback(); // Rollback transaction if failed
                    System.out.println("Failed to create product.");
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Read Product
    private static void readProduct() {
        try {
            System.out.print("Enter Product ID to search: ");
            int productId = scanner.nextInt();

            // SQL Query to select the product
            String query = "SELECT * FROM Product WHERE ProductID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, productId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    System.out.println("Product ID: " + rs.getInt("ProductID"));
                    System.out.println("Product Name: " + rs.getString("ProductName"));
                    System.out.println("Price: " + rs.getDouble("Price"));
                    System.out.println("Quantity: " + rs.getInt("Quantity"));
                } else {
                    System.out.println("Product not found.");
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Update Product
    private static void updateProduct() {
        try {
            System.out.print("Enter Product ID to update: ");
            int productId = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            System.out.print("Enter new Product Name: ");
            String productName = scanner.nextLine();
            System.out.print("Enter new Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter new Quantity: ");
            int quantity = scanner.nextInt();

            // SQL Update Statement
            String query = "UPDATE Product SET ProductName = ?, Price = ?, Quantity = ? WHERE ProductID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, productName);
                stmt.setDouble(2, price);
                stmt.setInt(3, quantity);
                stmt.setInt(4, productId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit(); // Commit transaction
                    System.out.println("Product updated successfully.");
                } else {
                    conn.rollback(); // Rollback transaction if failed
                    System.out.println("Failed to update product.");
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Delete Product
    private static void deleteProduct() {
        try {
            System.out.print("Enter Product ID to delete: ");
            int productId = scanner.nextInt();

            // SQL Delete Statement
            String query = "DELETE FROM Product WHERE ProductID = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, productId);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit(); // Commit transaction
                    System.out.println("Product deleted successfully.");
                } else {
                    conn.rollback(); // Rollback transaction if failed
                    System.out.println("Failed to delete product.");
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    // Handle SQLException and perform rollback if necessary
    private static void handleSQLException(SQLException e) {
        try {
            conn.rollback(); // Rollback transaction if exception occurs
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        e.printStackTrace();
    }

    // Close database connection
    private static void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
