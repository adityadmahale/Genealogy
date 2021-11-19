import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class MediaDatabaseAccess {
	
	// Database connection object
	private static Connection connection = DatabaseConnection.getConnection();
	
	// Inserts a media into the database
	public int insertMedia(String fileLocation) throws SQLException {
		return CommonQueries.insertIntoOneColumnStringTable("media", "file_location", fileLocation);
	}
	
	// Returns all the tags present in the database
	public Map<String, Integer> getTags() throws SQLException {
		
		// Get the result set
	    ResultSet rs = CommonQueries.getAllColumnsAndRows("tag");
	    
	    // Iterate over the result set and store the values in the map
	    // with tag name as the key and tag id as the value
	    Map<String, Integer> tags = new HashMap<>();
	    while(rs.next()) {
	    	tags.put(rs.getString("name"), rs.getInt("tag_id"));
	    }
	    
	    return tags;
	}
	
	// Returns all the tags present in the database
		public Map<String, FileIdentifier> getFiles() throws SQLException {
			
			// Get the result set
		    ResultSet rs = CommonQueries.getAllColumnsAndRows("media");
		    
		    // Iterate over the result set and store the values in the map
		    // with the file location as the key and file identifier as the value
		    Map<String, FileIdentifier> files = new HashMap<>();
		    while(rs.next()) {
		    	String file_location = rs.getString("file_location");
		    	int media_id = rs.getInt("media_id");
		    	files.put(file_location, new FileIdentifier(media_id, file_location));
		    }
		    
		    return files;
	}
	
	// Inserts a tag into the database
	private int insertTag(String name) throws SQLException {
		return CommonQueries.insertIntoOneColumnStringTable("tag", "name", name);
	}
	
	// Links tag and media
	public void linkTagAndMedia(int tagId, int mediaId, String tagName) throws SQLException {
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
			CommonQueries.insertIntoLinkTable("media_tag", tagId, mediaId);
			
			// Complete the transaction
	        connection.commit();
	    } catch (SQLException e) {
	    	// Roll back on failure
	        connection.rollback();
	        connection.setAutoCommit(true);
	        throw e;
	    } 
		connection.setAutoCommit(true);
	}
	
	
}
