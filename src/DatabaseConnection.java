import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Singleton class for getting database connection
class DatabaseConnection {
	private static final String CONNECTION_STRING_KEY = "connection_string";
	private static final String USERNAME_KEY = "username";
	private static final String PASSWORD_KEY = "password";
	
	// Connection object shared across all the code
	private static Connection connection;
	
	static Connection getConnection() {
		
		try {
			// Returns the connection object if it's not null
			if (connection != null) {
				return connection;
			}
			
			// Get database inputs from the properties file
			Properties properties = Utility.loadDatabaseInputs();
			String connectionString = properties.getProperty(CONNECTION_STRING_KEY);
			String username = properties.getProperty(USERNAME_KEY);
			String password = properties.getProperty(PASSWORD_KEY);
			
			// Establish connection to the database
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(connectionString, username, password);
			
		} catch (SQLException | ClassNotFoundException e) {
			throw new IllegalStateException(e.getMessage());
		}
		return connection;
	}
}
