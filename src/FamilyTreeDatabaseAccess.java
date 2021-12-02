import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;


class FamilyTreeDatabaseAccess {
	// Family tree related table names
	private static final String TABLE_PERSON = "person";
	private static final String TABLE_REFERENCE = "reference";
	private static final String TABLE_NOTE = "note";
	private static final String TABLE_PARTNER = "partner";
	private static final String TABLE_CHILD = "child";
	private static final String TABLE_PERSON_ATTRIBUTE = "person_attribute";
	private static final String TABLE_LOCATION = "location";
	private static final String TABLE_OCCUPATION = "occupation";
	
	// Family tree related column names
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PERSON_ID = "person_id";
	private static final String COLUMN_SOURCE = "source";
	private static final String COLUMN_TEXT = "text";
	private static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
	private static final String COLUMN_DATE_OF_DEATH = "date_of_death";
	private static final String COLUMN_GENDER = "gender";
	private static final String COLUMN_LOCATION_ID_BIRTH = "location_id_birth";
	private static final String COLUMN_LOCATION_ID_DEATH = "location_id_death";
	private static final String COLUMN_OCCUPATION_ID = "occupation_id";
	
	// Database connection object
	private static Connection connection = DatabaseConnection.getConnection();
	
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
	
	// Checks if the media attribute is present
	boolean isAttributePresent(int id) throws SQLException {
		return QueryUtility.isRowPresent(TABLE_PERSON_ATTRIBUTE, COLUMN_PERSON_ID, id);
	}
	
	// Inserts a location into the database
	private int insertLocation(String name) throws SQLException {
		return QueryUtility.insertIntoOneColumnStringTable(TABLE_LOCATION, COLUMN_NAME, name);
	}
	
	// Inserts a occupation into the database
	private int insertOccupation(String name) throws SQLException {
		return QueryUtility.insertIntoOneColumnStringTable(TABLE_OCCUPATION, COLUMN_NAME, name);
	}
	
	void updatePersonAttributes(int personId, String dateOfBirth, String dateOfDeath, String gender, int locationOfBirthId, String locationOfBirth, int locationOfDeathId, String locationOfDeath, int occupationId, String occupation, Map<String, Integer> locations, Map<String, Integer> occupations) throws SQLException {
		// Set auto commit to false as insertion in the tables(person_attribute, location, occupation)
		// should complete successfully
		connection.setAutoCommit(false);
		try  {
			// If the location id is not present, the insert the location into the location table and 
			// get its locationId
			if (locationOfBirth != null && locationOfBirthId == 0) {
				locationOfBirthId = insertLocation(locationOfBirth);
			}
			
			if (locationOfDeath != null && locationOfDeathId == 0) {
				locationOfDeathId = insertLocation(locationOfDeath);
			}
			
			if (occupation != null && occupationId == 0) {
				occupationId = insertOccupation(occupation);
			}
			
			String updateString = "";
			// Parse date of birth value
			if (dateOfBirth != null) {
				updateString += COLUMN_DATE_OF_BIRTH + "='" + dateOfBirth + "',";
			}
			// Parse date of death value
			if (dateOfDeath != null) {
				updateString += COLUMN_DATE_OF_DEATH + "='" + dateOfDeath + "',";
			}
			// Parse date of gender value
			if (gender != null) {
				updateString += COLUMN_GENDER + "='" + gender + "',";
			}
			// Parse date of occupation value
			if (occupation != null) {
				updateString += COLUMN_OCCUPATION_ID + "=" + occupationId + ",";
			}
			// Parse date of location of birth value
			if (locationOfBirth != null) {
				updateString += COLUMN_LOCATION_ID_BIRTH + "=" + locationOfBirthId + ",";
			}
			// Parse date of location of death value
			if (locationOfDeath != null) {
				updateString += COLUMN_LOCATION_ID_DEATH + "=" + locationOfDeathId + ",";
			}
			
			// Update values
			String query = String.format("UPDATE %s SET %s WHERE %s=%d", TABLE_PERSON_ATTRIBUTE, updateString.substring(0, updateString.length() - 1), COLUMN_PERSON_ID, personId);
		    PreparedStatement ps = connection.prepareStatement(query);
		    ps.executeUpdate();
			
			// Complete the transaction
	        connection.commit();
	        if (locationOfBirth != null) {
	        	locations.put(locationOfBirth, locationOfBirthId);
	        }
	        if (locationOfDeath != null) {
	        	locations.put(locationOfDeath, locationOfDeathId);
	        }
	        if (occupation != null) {
	        	occupations.put(occupation, occupationId);
	        }
	        
	    } catch (SQLException e) {
	    	// Roll back on failure
	        connection.rollback();
	        connection.setAutoCommit(true);
	        throw e;
	    } 
		connection.setAutoCommit(true);
	}
	
	void insertPersonAttributes(int personId, String dateOfBirth, String dateOfDeath, String gender, int locationOfBirthId, String locationOfBirth, int locationOfDeathId, String locationOfDeath, int occupationId, String occupation, Map<String, Integer> locations, Map<String, Integer> occupations) throws SQLException {
		// Set auto commit to false as insertion in the tables(person_attribute, location, occupation)
		// should complete successfully
		connection.setAutoCommit(false);
		try  {
			// If the location id is not present, the insert the location into the location table and 
			// get its locationId
			if (locationOfBirth != null && locationOfBirthId == 0) {
				locationOfBirthId = insertLocation(locationOfBirth);
			}
			
			if (locationOfDeath != null && locationOfDeathId == 0) {
				locationOfDeathId = insertLocation(locationOfDeath);
			}
			
			if (occupation != null && occupationId == 0) {
				occupationId = insertOccupation(occupation);
			}
			
			
			// Insert values
			String query = String.format("INSERT INTO %s(%s,%s,%s,%s,%s,%s,%s) VALUES (?, ?, ?, ?, ?, ?, ?)", TABLE_PERSON_ATTRIBUTE, COLUMN_DATE_OF_BIRTH, COLUMN_DATE_OF_DEATH, COLUMN_GENDER, COLUMN_LOCATION_ID_BIRTH, COLUMN_LOCATION_ID_DEATH, COLUMN_OCCUPATION_ID, COLUMN_PERSON_ID);
		    PreparedStatement ps = connection.prepareStatement(query);
		    // Date of birth
		    Date dateOfBirthValue = null;
		    if (dateOfBirth != null) {
		    	dateOfBirthValue = Date.valueOf(dateOfBirth);
		    }
		    ps.setDate(1, dateOfBirthValue);
		    // Date of death
		    Date dateOfDeathValue = null;
		    if (dateOfDeath != null) {
		    	dateOfDeathValue = Date.valueOf(dateOfDeath);
		    }
		    ps.setDate(2, dateOfDeathValue);
		    // Gender
		    ps.setString(3, gender);
		    
		    if (locationOfBirthId == 0) {
		    	ps.setString(4, null);
		    } else {
		    	ps.setInt(4, locationOfBirthId);
		    }
		    
		    if (locationOfDeathId == 0) {
		    	ps.setString(5, null);
		    } else {
		    	ps.setInt(5, locationOfDeathId);
		    }
		    
		    if (occupationId == 0) {
		    	ps.setString(6, null);
		    } else {
		    	ps.setInt(6, occupationId);
		    }
		    
		    ps.setInt(7, personId);
		    
		    ps.executeUpdate();
			// Complete the transaction
	        connection.commit();
	        if (locationOfBirth != null) {
	        	locations.put(locationOfBirth, locationOfBirthId);
	        }
	        if (locationOfDeath != null) {
	        	locations.put(locationOfDeath, locationOfDeathId);
	        }
	        if (occupation != null) {
	        	occupations.put(occupation, occupationId);
	        }
	        
	    } catch (SQLException e) {
	    	// Roll back on failure
	        connection.rollback();
	        connection.setAutoCommit(true);
	        throw e;
	    } 
		connection.setAutoCommit(true);
	}
}
