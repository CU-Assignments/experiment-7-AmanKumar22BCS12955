import java.sql.*;

public class FetchEmployeeData {
    public static void main(String[] args) {
        // Database credentials
        String url = "jdbc:mysql://localhost:3306/your_database"; // replace with your DB URL
        String username = "your_username"; // replace with your DB username
        String password = "your_password"; // replace with your DB password
        
        // SQL query to fetch all records from the Employee table
        String query = "SELECT EmpID, Name, Salary FROM Employee";
        
        // Declare connection and statement objects
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            // Step 1: Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Establish connection to the database
            conn = DriverManager.getConnection(url, username, password);

            // Step 3: Create a statement
            stmt = conn.createStatement();

            // Step 4: Execute the query and get the result set
            rs = stmt.executeQuery(query);

            // Step 5: Process the result set
            System.out.println("EmpID\tName\t\tSalary");
            System.out.println("------------------------------------");

            // Loop through the result set and display each record
            while (rs.next()) {
                int empID = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");
                
                // Display the employee details
                System.out.printf("%d\t%s\t\t%.2f\n", empID, name, salary);
            }
        } catch (SQLException | ClassNotFoundException e) {
            // Handle any exceptions that occur
            e.printStackTrace();
        } finally {
            // Step 6: Close resources to avoid memory leaks
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
