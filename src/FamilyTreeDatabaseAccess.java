import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FamilyTreeDatabaseAccess {
	// Family tree related table names
	private static final String TABLE_PERSON = "person";
	private static final String TABLE_REFERENCE = "reference";
	private static final String TABLE_NOTE = "note";
	private static final String TABLE_PARTNER = "partner";
	private static final String TABLE_CHILD = "child";
	
	// Family tree related column names
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PERSON_ID = "person_id";
	private static final String COLUMN_PARTNER_ID = "partner_id";
	private static final String COLUMN_CHILD_ID = "child_id";
	private static final String COLUMN_SOURCE = "source";
	private static final String COLUMN_TEXT = "text";
	
	// Database connection object
	private static Connection connection = DatabaseConnection.getConnection();
	
	// Returns all the persons present in the database
	public void loadPersons(Map<String, PersonIdentity> persons, Map<Integer, PersonIdentity> personIds, Set<PersonIdentity> roots) throws SQLException {
		
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
	    	roots.add(personIdentity);
	    }
	}
	
	// Load partnering relationships
	public void loadPartneringRelationships(Set<PersonIdentity> partnered, Map<Integer, PersonIdentity> personIds) throws SQLException {
		
		// Get the result set
	    ResultSet rs = QueryUtility.getAllColumnsAndRows(TABLE_PARTNER);
	    
	    // Iterate over the result set and store the values in the partnered set
	    while(rs.next()) {
	    	// Get row values
	    	int person_id = rs.getInt(COLUMN_PERSON_ID);
	    	int partner_id = rs.getInt(COLUMN_PARTNER_ID);
	    	
	    	// Get the PersonIdentity objects for partners
	    	PersonIdentity personIdentity1 = personIds.get(person_id);
	    	PersonIdentity personIdentity2 = personIds.get(partner_id);
	    	
	    	// Add the person to the partnered set
	    	partnered.add(personIdentity1);
	    	
	    	// Set the partner in the PersonIdentity object
	    	personIdentity1.setPartner(personIdentity2);
	    }
	}
	
	// Load parent-child relationships
	public void loadParentChildRelationships(Set<PersonIdentity> children, Set<PersonIdentity> roots, Map<Integer, PersonIdentity> personIds) throws SQLException {
		
		// Get the result set
	    ResultSet rs = QueryUtility.getAllColumnsAndRows(TABLE_CHILD);
	    
	    // Iterate over the result set and store the values in the children set
	    while(rs.next()) {
	    	// Get row values
	    	int parentId = rs.getInt(COLUMN_PERSON_ID);
	    	int childId = rs.getInt(COLUMN_CHILD_ID);
	    	
	    	// Get the PersonIdentity objects for parent and child
	    	PersonIdentity parentIdentity = personIds.get(parentId);
	    	PersonIdentity childIdentity = personIds.get(childId);
	    	
	    	// Remove child from root and add to children
	    	roots.remove(childIdentity);
			children.add(childIdentity);
			
			// Add child for the individual and the partner
			parentIdentity.addChild(childIdentity);
			if (parentIdentity.hasPartner()) {
				parentIdentity.getPartner().addChild(childIdentity);
			}
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
	
	// Inserts partnering relation
	public void insertPartneringRelation(int personId1, int personId2) throws SQLException {
		QueryUtility.insertSymmetricIntValuesIntoTable(TABLE_PARTNER, personId1, personId2);
	}
	
	// Deletes partnering relation
	public void dissolvePartneringRelation(int personId1, int personId2) throws SQLException {
		QueryUtility.deleteSymmetricIntValuesFromTable(TABLE_PARTNER, COLUMN_PERSON_ID, personId1, personId2);
	}
	
	// Inserts a parent-child relation
	public void insertParentChild(int parentId, int childId) throws SQLException {
		QueryUtility.insertIntoLinkTable(TABLE_CHILD, parentId, childId);
	}
}
