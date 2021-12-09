import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportingDatabaseAccess {
	// Columns
	private static final String COLUMN_FILE_LOCATION = "file_location";
	private static final String COLUMN_SOURCE = "source";
	private static final String COLUMN_MA_DATE = "ma.date";
	private static final String COLUMN_DATE = "date";
	
	// Queries
	// Union query between notes and references
	private static final String notesAndReferencesQuery = 
			"select * from ( "
			+ "select source, record_time, person_id "
			+ "from reference "
			+ "union "
			+ "select text, record_time, person_id "
			+ "from note) a "
			+ "where person_id = %d "
			+ "order by record_time";
	
	// Find media by tag query
	private static final String mediaByTagQuery = 
			"select m.file_location "
			+ "from media m "
			+ "join media_tag mt on m.media_id = mt.media_id "
			+ "left join media_attribute ma on m.media_id = ma.media_id "
			+ "where mt.tag_id = %d and ((%s) or ma.date is null) ";
	
	// Find media by location query
	private static final String mediaByLocationQuery = 
			"select m.file_location "
			+ "from media m "
			+ "join media_attribute ma on m.media_id = ma.media_id "
			+ "where ma.city_id = %d and ((%s) or ma.date is null)";
	
	// Find media by individuals
	private static final String mediaByIndividualsQuery = 
			"with "
			+ "pim as (select m.file_location, pm.person_id, ma.date "
			+ "from media m "
			+ "join person_in_media pm on m.media_id = pm.media_id "
			+ "left join media_attribute ma on m.media_id = ma.media_id "
			+ "where pm.person_id in (%s)) "
			+ "select distinct file_location from (select * from pim "
			+ "where %s "
			+ "order by date, file_location) a "
			+ "union "
			+ "select distinct file_location from (select * from pim "
			+ "where date is null "
			+ "order by file_location) b";
	
	// Find media by children query
	private static final String mediaByChildrenQuery = 
			"with "
			+ "pim as (select m.file_location, pm.person_id, ma.date "
			+ "from media m "
			+ "join person_in_media pm on m.media_id = pm.media_id "
			+ "left join media_attribute ma on m.media_id = ma.media_id "
			+ "where pm.person_id in (%s)) "
			+ "select distinct file_location from (select * from pim "
			+ "where date is not null "
			+ "order by date, file_location) a "
			+ "union "
			+ "select distinct file_location from (select * from pim "
			+ "where date is null "
			+ "order by file_location) b;";
	
	// Database connection object
	private static Connection connection = DatabaseConnection.getConnection();
	
	void getNotesAndReferences(PersonIdentity person, List<String> result) throws SQLException {
		// Parse final query
		String query = String.format(notesAndReferencesQuery, person.getPersonId());
	    PreparedStatement ps = connection.prepareStatement(query);
	    // Execute query
	    ResultSet rs = ps.executeQuery();
	    
	    // Add the text to the list
	    while (rs.next()) {
	    	result.add(rs.getString(COLUMN_SOURCE));
	    }
	}
	
	// Parse date conditions
	private String getDateCondition(String columnName, String startDate, String endDate) {
		String startDateCondition = "";
		String endDateCondition = "";
		
		// When start date is null
		if (startDate != null) {
			startDateCondition = columnName + " >= '" + startDate + "'";
		}
		
		// When end date is null
		if (endDate != null) {
			endDateCondition = columnName + " <= '" + endDate + "'";
		}
		
		// Check for all possible date conditions
		if (startDate != null && endDate != null) {
			return startDateCondition + " and " + endDateCondition;
		} else if (startDate == null && endDate != null) {
			return endDateCondition;
		} else if (startDate != null && endDate == null) {
			return startDateCondition;
		} else {
			return columnName + " is not null";
		}
	}
	
	void getMediaByTag(int tagId, String startDate, String endDate, Set<FileIdentifier> result, Map<String, FileIdentifier> files) throws SQLException {
		// Parse final query
		String query = String.format(mediaByTagQuery, tagId, getDateCondition(COLUMN_MA_DATE, startDate, endDate));
	    PreparedStatement ps = connection.prepareStatement(query);
	    // Execute query
	    ResultSet rs = ps.executeQuery();
	    
	    // Add the file identifier to the result set
	    while (rs.next()) {
	    	result.add(files.get(rs.getString(COLUMN_FILE_LOCATION)));
	    }
	}
	
	void getMediaByLocation(int cityId, String startDate, String endDate, Set<FileIdentifier> result, Map<String, FileIdentifier> files) throws SQLException {
		// Parse final query
		String query = String.format(mediaByLocationQuery, cityId, getDateCondition(COLUMN_MA_DATE, startDate, endDate));
	    PreparedStatement ps = connection.prepareStatement(query);
	    // Execute query
	    ResultSet rs = ps.executeQuery();
	    
	    // Add the file identifier to the result set
	    while (rs.next()) {
	    	result.add(files.get(rs.getString(COLUMN_FILE_LOCATION)));
	    }
	}
	
	void getMediaByIndividuals(Set<PersonIdentity> people, String startDate, String endDate, List<FileIdentifier> result, Map<String, FileIdentifier> files) throws SQLException {
		// Get the ids of the individuals
		StringBuilder sb = new StringBuilder();
		for (var person: people) {
			// If the object is null then continue
			if (person == null) {
				continue;
			}
			// Otherwise, append the person id
			sb.append(person.getPersonId()).append(",");
		}
		
		// If no ids are present, then return
		String ids = sb.toString();
		if (ids.length() == 0) {
			return;
		}
		
		// Parse final query
		String query = String.format(mediaByIndividualsQuery, ids.substring(0, ids.length() - 1), getDateCondition(COLUMN_DATE, startDate, endDate));
	    PreparedStatement ps = connection.prepareStatement(query);
	    // Execute query
	    ResultSet rs = ps.executeQuery();
	    
	    // Add the file identifier to the result list
	    while (rs.next()) {
	    	result.add(files.get(rs.getString(COLUMN_FILE_LOCATION)));
	    }
	}
	
	void getMediaByBiologicalChildren(PersonIdentity person, List<FileIdentifier> result, Map<String, FileIdentifier> files) throws SQLException {
		// Get the ids of the children
		StringBuilder sb = new StringBuilder();
		for (var child: person.getChildren()) {
			// Otherwise, append the child id
			sb.append(child.getPersonId()).append(",");
		}
		
		// Get the string
		String ids = sb.toString();
		
		// Parse final query
		String query = String.format(mediaByChildrenQuery, ids.substring(0, ids.length() - 1));
	    PreparedStatement ps = connection.prepareStatement(query);
	    // Execute query
	    ResultSet rs = ps.executeQuery();
	    
	    // Add the file identifier to the result list
	    while (rs.next()) {
	    	result.add(files.get(rs.getString(COLUMN_FILE_LOCATION)));
	    }
	}
}
