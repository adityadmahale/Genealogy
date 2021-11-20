import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FamilyTreeDatabaseAccess {
	// Database connection object
	private static Connection connection = DatabaseConnection.getConnection();
	
	// Returns all the persons present in the database
	public Map<String, PersonIdentity> getPersons() throws SQLException {
		
		// Get the result set
	    ResultSet rs = QueryUtility.getAllColumnsAndRows("person");
	    
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
		return QueryUtility.insertIntoOneColumnStringTable("person", "name", name);
	}
	
	// Inserts a reference for a person
	public void insertReference(int personId, String reference) throws SQLException {
		QueryUtility.insertIntoTwoColumnsTable("reference", "source", reference, "person_id", personId);
	}
	
	// Inserts a note for a person
	public void insertNote(int personId, String note) throws SQLException {
		QueryUtility.insertIntoTwoColumnsTable("note", "text", note, "person_id", personId);
	}
}
