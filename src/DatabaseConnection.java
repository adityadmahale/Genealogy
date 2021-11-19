import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton class for getting database connection
public class DatabaseConnection {
	
	// Connection object shared across all the code
	private static Connection connection;
	
	public static Connection getConnection() {
		try {
			// Returns the connection object if it's not null
			if (connection != null) {
				return connection;
			}
			
			// Establish connection to the database
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/genealogy", "root", "pulsar25");
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
