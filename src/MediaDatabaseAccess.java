import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


class MediaDatabaseAccess {
	// Media related table names
	private static final String TABLE_MEDIA = "media";
	private static final String TABLE_TAG = "tag";
	private static final String TABLE_CITY = "city";
	private static final String TABLE_MEDIA_TAG = "media_tag";
	private static final String TABLE_PERSON_IN_MEDIA = "person_in_media";
	private static final String TABLE_MEDIA_ATTRIBUTE = "media_attribute";
	
	// Media related column names
	private static final String COLUMN_FILE_LOCTION = "file_location";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_MEDIA_ID = "media_id";
	private static final String COLUMN_PERSON_ID = "person_id";
	private static final String COLUMN_YEAR = "year";
	private static final String COLUMN_DATE = "date";
	private static final String COLUMN_CITY_ID = "city_id";
	
	// Database connection object
	private static Connection connection = DatabaseConnection.getConnection();
	
	// Inserts a media into the database
	int insertMedia(String fileLocation) throws SQLException {
		return QueryUtility.insertIntoOneColumnStringTable(TABLE_MEDIA, COLUMN_FILE_LOCTION, fileLocation);
	}
	
	// Inserts a tag into the database
	private int insertTag(String name) throws SQLException {
		return QueryUtility.insertIntoOneColumnStringTable(TABLE_TAG, COLUMN_NAME, name);
	}
	
	// Inserts a city into the database
	private int insertCity(String name) throws SQLException {
		return QueryUtility.insertIntoOneColumnStringTable(TABLE_CITY, COLUMN_NAME, name);
	}
	
	// Links tag and media
	void linkTagAndMedia(int tagId, int mediaId, String tagName, Map<String, Integer> tags) throws SQLException {
		// Set auto commit to false as insertion in both the tables(tag and media_tag)
		// should complete successfully
		connection.setAutoCommit(false);
		try  {
			// If the tag id is not present, the insert the tag into the tag table and 
			// get its tagId
			if (tagId == 0) {
				tagId = insertTag(tagName);
			}
			
			// Insert values into the link table: media_tag
			QueryUtility.insertIntoLinkTable(TABLE_MEDIA_TAG, tagId, mediaId);
			
			// Complete the transaction
	        connection.commit();
	        tags.put(tagName, tagId);
	    } catch (SQLException e) {
	    	// Roll back on failure
	        connection.rollback();
	        connection.setAutoCommit(true);
	        throw e;
	    } 
		connection.setAutoCommit(true);
	}
	
	// Links people and media
	void linkPeopleAndMedia(FileIdentifier fileIdentifier, List<PersonIdentity> people) throws SQLException {
		// Get mediaId
		int mediaId = fileIdentifier.getFileId();
		
		// Define values variable
		String values = "";
		
		// Iterate over the people list to extract ids
		for (var person: people) {
			if (person == null) {
				continue;
			}
			
			// Append the values section of the query
			values += String.format("(%d,%d),", person.getPersonId(), mediaId);
		}
		
		// If no person exists, then return
		if (values.equals("")) {
			return;
		}
		
		// Final query
		String query = String.format("INSERT INTO %s(%s, %s) VALUES%s", TABLE_PERSON_IN_MEDIA, COLUMN_PERSON_ID, COLUMN_MEDIA_ID, values.substring(0, values.length() - 1));
		PreparedStatement ps = connection.prepareStatement(query);
		ps.executeUpdate();
	}
	
	void addMediaAttributes(int fileId, int cityId, String city, String date, String year, Map<String, Integer> cities) throws SQLException {
		// Set auto commit to false as insertion in both the tables(city and media_attribute)
		// should complete successfully
		connection.setAutoCommit(false);
		try  {
			// If the city id is not present, the insert the city into the city table and 
			// get its cityId
			if (cityId == 0) {
				cityId = insertCity(city);
			}
			
			// Insert values into the link table: media_tag
			String query = String.format("INSERT INTO %s(%s,%s,%s,%s) VALUES (?, ?, ?, ?)", TABLE_MEDIA_ATTRIBUTE, COLUMN_MEDIA_ID, COLUMN_YEAR, COLUMN_DATE, COLUMN_CITY_ID);
		    PreparedStatement ps = connection.prepareStatement(query);
		    ps.setInt(1, fileId);
		    ps.setString(2, year);
		    Date dateValue = null;
		    if (date != null) {
		    	dateValue = Date.valueOf(date);
		    }
		     
		    ps.setDate(3, dateValue);
		    ps.setInt(4, cityId);
		    ps.executeUpdate();
			
			// Complete the transaction
	        connection.commit();
	        cities.put(city, cityId);
	    } catch (SQLException e) {
	    	// Roll back on failure
	        connection.rollback();
	        connection.setAutoCommit(true);
	        throw e;
	    } 
		connection.setAutoCommit(true);
	}
	
}
