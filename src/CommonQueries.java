import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CommonQueries {
	private static Connection connection = DatabaseConnection.getConnection();
	
	// Inserts two integer values in a link table
	// The method only inserts a single row
	public static void insertIntoLinkTable(String tableName, int id1, int id2) throws SQLException {
		String query = "INSERT INTO " + tableName + " VALUES (?, ?)";
	    PreparedStatement ps = connection.prepareStatement(query);
	    ps.setInt(1, id1);
	    ps.setInt(2, id2);
	    ps.executeUpdate();
	}
	
	// Returns a result set after querying a table without the where clause
	// All columns are selected from the table
	public static ResultSet getAllColumnsAndRows(String tableName) throws SQLException {
		String query = "SELECT * FROM " + tableName;
	    PreparedStatement ps = connection.prepareStatement(query);
	    return ps.executeQuery();
	}
	
	// Inserts a single row in a table with one string column
	// the method only inserts a single row
	public static int insertIntoOneColumnStringTable(String tableName, String columnName, String columnValue) throws SQLException {
		String query = "INSERT INTO " + tableName + "(" + columnName + ")" + " VALUES (?)";
	    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    ps.setString(1, columnValue);
	    ps.executeUpdate();
	    ResultSet rs = ps.getGeneratedKeys();
	    rs.next();
	    return rs.getInt(1);
	}
}
