import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FamilyTreeDatabaseAccess {
	// Database connection object
	private static Connection connection = DatabaseConnection.getConnection();
	
	// Returns all the persons present in the database
	public Map<String, PersonIdentity> getPersons() throws SQLException {
		
		// Get the result set
	    ResultSet rs = CommonQueries.getAllColumnsAndRows("person");
	    
	    // Iterate over the result set and store the values in the map
	    // with the person name as the key and person identity object as the value
	    Map<String, PersonIdentity> persons = new HashMap<>();
	    while(rs.next()) {
	    	String name = rs.getString("name");
	    	int person_id = rs.getInt("person_id");
	    	persons.put(name, new PersonIdentity(person_id, name));
	    }
	    
	    return persons;
	}
	
	// Inserts a person into the database
	public int insertPerson(String name) throws SQLException {
		return CommonQueries.insertIntoOneColumnStringTable("person", "name", name);
	}
}
