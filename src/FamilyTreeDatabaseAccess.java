import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private static final String COLUMN_SOURCE = "source";
	private static final String COLUMN_TEXT = "text";
	
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
