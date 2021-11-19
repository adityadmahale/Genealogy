import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MediaManagement {
	
	// Maximum string length of the tag name
	private static final int MAX_TAG_NAME_LENGTH = 255;
	
	// Maximum string length of the file location
	private static final int MAX_FILE_LOCATION_LENGTH = 500;
	
	// Map for storing tag and its corresponding tag id
	private Map<String, Integer> tags;
	
	// Map for storing file location and its corresponding FileIdentifier
	private Map<String, FileIdentifier> files;
	
	// Object for accessing media related database tables
	private MediaDatabaseAccess mediaAccess = new MediaDatabaseAccess();
	
	public MediaManagement() {
		// Retrieve all the tags in the database and store it in the tags map
		try {
			files = mediaAccess.getFiles();
			tags = mediaAccess.getTags();
		} catch (SQLException e) {
			throw new IllegalStateException();
		}
	}
	
	FileIdentifier addMediaFile(String fileLocation) {
		
		// Handle illegal arguments
		if (fileLocation == null || fileLocation.equals("") || fileLocation.length() > MAX_FILE_LOCATION_LENGTH) {
			throw new IllegalArgumentException();
		}
		
		// If the file location already exists, then throw an exception
		if (files.containsKey(fileLocation)) {
			throw new IllegalArgumentException("File location already exists");
		}
		
		// Create a FileIdentifier variable
		FileIdentifier fileIdentifier;
		try {
			
			// Insert the media into the media table
			int id = mediaAccess.insertMedia(fileLocation);
			
			// Instantiate the file identifier class
			fileIdentifier = new FileIdentifier(id, fileLocation);
			
			// Add the file location and identifier to the files map
			files.put(fileLocation, fileIdentifier);
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		return fileIdentifier;
	}
	
	Boolean recordMediaAttributes(FileIdentifier fileIdentifier, Map<String, String> attributes) {
		return false;
	}
	
	Boolean peopleInMedia(FileIdentifier fileIdentifier, List<PersonIdentity> people) {
		// Handle invalid input for the fileIdentifier
		if (fileIdentifier == null) {
			throw new IllegalArgumentException();
		}
		
		try {
			// Link people and media
			mediaAccess.linkPeopleAndMedia(fileIdentifier, people);
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		
		return true;
	}
	
	Boolean tagMedia(FileIdentifier fileIdentifier, String tag) {
		
		// Handle invalid input for the tag
		if (tag == null || tag.equals("") || tag.length() > MAX_TAG_NAME_LENGTH) {
			throw new IllegalArgumentException();
		}
		
		// Handle invalid input for the fileIdentifier
		if (fileIdentifier == null) {
			throw new IllegalArgumentException();
		}
		
		// Get the media id
		int mediaId = fileIdentifier.getFileId();
		
		// Get the tag id
		int tagId = getTagId(tag);
		
		try {
			// Link the tag and media
			mediaAccess.linkTagAndMedia(tagId, mediaId, tag);
		} catch (SQLException e) {
			throw new IllegalStateException(e.getMessage());
		}
		 
		return true;
	}
	
	// Get the tag id corresponding to a tag name
	private int getTagId(String tag) {
		int tagId = 0;
		
		// If the tag is already present then get that tagId
		if (tags.containsKey(tag)) {
			tagId = tags.get(tag);
		}
		return tagId;
	}
}
