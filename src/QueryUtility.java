import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryUtility {
	private static Connection connection = DatabaseConnection.getConnection();
	
	// Inserts two integer values in a link table
	// The method only inserts a single row
	public static void insertIntoLinkTable(String tableName, int id1, int id2) throws SQLException {
		String query = String.format("INSERT INTO %s VALUES (?, ?)", tableName);
	    PreparedStatement ps = connection.prepareStatement(query);
	    ps.setInt(1, id1);
	    ps.setInt(2, id2);
	    ps.executeUpdate();
	}
	
	// Returns a result set after querying a table without the where clause
	// All columns are selected from the table
	public static ResultSet getAllColumnsAndRows(String tableName) throws SQLException {
		String query = String.format("SELECT * FROM %s", tableName);
	    PreparedStatement ps = connection.prepareStatement(query);
	    return ps.executeQuery();
	}
	
	// Inserts a single row in a table with one string column
	// the method only inserts a single row
	public static int insertIntoOneColumnStringTable(String tableName, String columnName, String columnValue) throws SQLException {
		String query = String.format("INSERT INTO %s(%s) VALUES (?)", tableName, columnName);
	    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
	    ps.setString(1, columnValue);
	    ps.executeUpdate();
	    ResultSet rs = ps.getGeneratedKeys();
	    rs.next();
	    return rs.getInt(1);
	}
	
	// Inserts a single row in a table with one string column and one integer column
	public static void insertIntoTwoColumnsTable(String tableName, String columnName1, String columnValue1, String columnName2, int columnValue2) throws SQLException {
		String query = String.format("INSERT INTO %s(%s,%s) VALUES (?, ?)", tableName, columnName1, columnName2);
	    PreparedStatement ps = connection.prepareStatement(query);
	    ps.setString(1, columnValue1);
	    ps.setInt(2, columnValue2);
	    ps.executeUpdate();
	}
	
	// Inserts two rows symmetrically - integers
	public static void insertSymmetricIntValuesIntoTable(String tableName, int personId1, int personId2) throws SQLException {
		String query = String.format("INSERT INTO %s VALUES (%d, %d), (%d, %d)", tableName, personId1, personId2, personId2, personId1);
	    PreparedStatement ps = connection.prepareStatement(query);
	    ps.executeUpdate();
	}
}
