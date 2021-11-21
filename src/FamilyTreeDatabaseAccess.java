import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FamilyTreeDatabaseAccess {
	// Family tree related table names
	private static final String TABLE_PERSON = "person";
	private static final String TABLE_REFERENCE = "reference";
	private static final String TABLE_NOTE = "note";
	
	// Family tree related column names
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PERSON_ID = "person_id";
	private static final String COLUMN_SOURCE = "source";
	private static final String COLUMN_TEXT = "text";
	
	// Database connection object
	private static Connection connection = DatabaseConnection.getConnection();
	
	// Returns all the persons present in the database
	public void loadMaps(Map<String, PersonIdentity> persons, Map<Integer, PersonIdentity> personIds) throws SQLException {
		
		// Get the result set
	    ResultSet rs = QueryUtility.getAllColumnsAndRows(TABLE_PERSON);
	    
	    // Iterate over the result set and store the values in the map
	    // with the person name as the key and person identity object as the value
	    while(rs.next()) {
	    	String name = rs.getString(COLUMN_NAME);
	    	int person_id = rs.getInt(COLUMN_PERSON_ID);
	    	PersonIdentity personIdentity = new PersonIdentity(person_id, name);
	    	persons.put(name, personIdentity);
	    	personIds.put(person_id, personIdentity);
	    }
	}
	
	// Inserts a person into the database
	public int insertPerson(String name) throws SQLException {
		return QueryUtility.insertIntoOneColumnStringTable(TABLE_PERSON, COLUMN_NAME, name);
	}
	
	// Inserts a reference for a person
	public void insertReference(int personId, String reference) throws SQLException {
		QueryUtility.insertIntoTwoColumnsTable(TABLE_REFERENCE, COLUMN_SOURCE, reference, COLUMN_PERSON_ID, personId);
	}
	
	// Inserts a note for a person
	public void insertNote(int personId, String note) throws SQLException {
		QueryUtility.insertIntoTwoColumnsTable(TABLE_NOTE, COLUMN_TEXT, note, COLUMN_PERSON_ID, personId);
	}
}
